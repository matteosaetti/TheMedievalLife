package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.screen.ScreenType;
import com.badlogic.gdx.math.Vector2;


import java.util.EnumMap;


public class MyGdxGame extends Game {
	public static final float UNIT_SCALE = 1/32f;
	private static final float FIXED_TIME_STAMP = 1/60f ;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private EnumMap<ScreenType, Screen> screenCache = new EnumMap<ScreenType, Screen>(ScreenType.class);
	private FitViewport screenViewport;

	//b2D variables and constants
	private static final float FIXED_TIME_STEP = 1/60f;
	public static final short BIT_CIRCLE = 1;
	public static final short BIT_BOX = 1 << 1;
	public static final short BIT_GROUND = 1 << 2;
	private Box2DDebugRenderer box2DDebugRenderer;
	private World world;
	private float accumulator;

	//asset manager
	private AssetManager assetManager;

	//skin
	private Stage stage;
	private Skin skin;


	//inputManager
	private InputManager inputManager;

	//audioManager
	private AudioManager audioManager;

	//map manager
	private MapManager mapManager;



	@Override
	public void create () {
		//box2D stuff
		Box2D.init();
		world = new World(new Vector2(0, -9.81f), true);
		box2DDebugRenderer = new Box2DDebugRenderer();
		// Invisible hitboxes
		box2DDebugRenderer.setDrawBodies(false);
		accumulator = 0;

		//Camera, viewport, batch
		camera = new OrthographicCamera(16, 9);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
		screenViewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
		spriteBatch = new SpriteBatch();

		//AssetManager
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));

		//MapManager
		mapManager = new MapManager(assetManager, world);

		//Scene2D
		initializeSkin();
		stage = new Stage(new FitViewport(1280, 720));

		//AudioManager
		audioManager = new AudioManager(assetManager);

		//input
		inputManager = new InputManager();
		Gdx.input.setInputProcessor(new InputMultiplexer(inputManager, stage));

		Gdx.app.setLogLevel(Application.LOG_ERROR);
		setScreen(ScreenType.LOADING);
	}
	public void setScreen(final ScreenType screenType){
		//screen conterrà lo Screen di tipo screenType dagli screenAvailable, oppure null se non ancora creato
		final Screen screen = screenCache.get(screenType);

		if (screen == null) {
			//si crea lo Screen di tipo screenType
			try {
				final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), MyGdxGame.class).newInstance(this);
				screenCache.put(screenType, newScreen);
				setScreen(newScreen);
			} catch (ReflectionException error) {
				throw new GdxRuntimeException("lo screen " + screenType + " e' inesistente a causa: " + error);

			}
		}
		else
			//si usa lo Screen già presente
			setScreen(screen);
	}

	private void initializeSkin() {

		final ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/MinimalPixelFont.ttf"));
		final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.minFilter = Texture.TextureFilter.Linear;
		fontParameter.magFilter = Texture.TextureFilter.Linear;
		final int[] sizeToCreate = {16,20,26,32};
		for(int size : sizeToCreate){
			fontParameter.size = size;
			resources.put("font_" + size, fontGenerator.generateFont(fontParameter));
		}
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/MinimalPixelFont.ttf"));
		fontParameter.size = 16;
		resources.put("font2_16", fontGenerator.generateFont(fontParameter));
		fontGenerator.dispose();

		//load skin
		final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("ui/hud.atlas", resources);
		assetManager.load("ui/hud.json", Skin.class, skinParameter);
		assetManager.finishLoading();
		skin = assetManager.get("ui/hud.json", Skin.class);


	}
	@Override
	public void render() {
		super.render();

		//Timestamp fixed for B2D simulation
		accumulator += Math.min(0.25f, Gdx.graphics.getDeltaTime());
		while(accumulator>=FIXED_TIME_STAMP){
			world.step(FIXED_TIME_STAMP, 6, 2);
			accumulator -= FIXED_TIME_STAMP;
		}

		//final float alpha = accumulator/FIXED_TIME_STAMP;
		stage.getViewport().apply();
		stage.act();
		stage.draw();
	}
	public MapManager getMapManager() {
		return mapManager;
	}


	public AudioManager getAudioManager() {
		return audioManager;
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public Stage getStage() {
		return stage;
	}

	public Skin getSkin() {
		return skin;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public OrthographicCamera getGameCamera() {
		return camera;
	}

	public  FitViewport getScreenViewport(){
		return screenViewport;
	}

	public World getWorld(){
		return world;
	}

	public Box2DDebugRenderer getBox2DDebugRenderer() {
		return box2DDebugRenderer;
	}





	@Override
	public void dispose() {
		super.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
		assetManager.dispose();
		spriteBatch.dispose();
		stage.dispose();
	}
}