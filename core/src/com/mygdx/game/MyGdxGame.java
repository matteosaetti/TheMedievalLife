package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
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

import java.util.EnumMap;

public class MyGdxGame extends Game {
	//Game vars
	public static final float UNIT_SCALE = 1/32f;
	private EnumMap<ScreenType, Screen> screenAvailable = new EnumMap<ScreenType, Screen>(ScreenType.class);
	private OrthographicCamera camera;
	private FitViewport viewport;
	private SpriteBatch batch;

	//box2D vars and constants
	private float accumulator;
	private static final float FIXED_TIME_STAMP = 1/60f;
	public static final short BIT_CIRCLE = 1;
	public static final short BIT_BOX= 1 << 1;
	public static final short BIT_GROUND = 1 << 2;
	private World world;
	private Box2DDebugRenderer box2DDebugRenderer;

	//assetManager
	private AssetManager assetManager;

	//mapManager
	private MapManager mapManager;

	//Skin
	private Stage stage;
	private Skin skin;

	//other manager
	private AudioManager audioManager;
	private InputManager inputManager;

	@Override
	public void create () {
		//box2d stuff
		Box2D.init();
		world = new World(new Vector2(0,-9.81f), true);
		box2DDebugRenderer = new Box2DDebugRenderer();
		// Invisible hitboxes
		box2DDebugRenderer.setDrawBodies(false);
		accumulator = 0;

		//Camera, viewport, batch
		camera = new OrthographicCamera(16, 9);
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		camera.update();
		viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
		batch = new SpriteBatch();

		//AssetManager
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));


		//MapManager
		mapManager = new MapManager(assetManager, world);

		//Scene2D
		skinInit();
		stage = new Stage(new FitViewport(1280, 720));

		//AudioManager
		audioManager = new AudioManager(assetManager);

		//input
		inputManager= new InputManager();
		Gdx.input.setInputProcessor(new InputMultiplexer(inputManager,stage));

		Gdx.app.setLogLevel(Application.LOG_ERROR);
		setScreen(ScreenType.LOADING);
	}

	public void setScreen(final ScreenType screenType){
		//screen conterrà lo Screen di tipo screenType dagli screenAvailable, oppure null se non ancora creato
		final Screen screen = screenAvailable.get(screenType);
		if(screen == null){
			//si crea lo Screen di tipo screenType
			try{
				final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), MyGdxGame.class).newInstance(this);
				screenAvailable.put(screenType, newScreen);
				setScreen(newScreen);
			} catch (ReflectionException error){
				throw new GdxRuntimeException("Lo Screen " + screenType + " non e' stato creato per: ", error);
			}
		}
		else
			//si usa lo Screen già presente
			setScreen(screen);
	}


	/**
	 * Getter of World
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Getter of camera
	 */
	public OrthographicCamera getGameCamera() {
		return camera;
	}

	/**
	 * Getter of viewport
	 */
	public FitViewport getScreenViewport() {
		return viewport;
	}

	/**
	 * Getter of batch
	 */
	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	/**
	 * Getter of B2DDebugRenderer
	 */
	public Box2DDebugRenderer getBox2DDebugRenderer(){
		return box2DDebugRenderer;
	}

	/**
	 * Getter of assetManager
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Getter of inputManager
	 */
	public InputManager getInputManager() {
		return inputManager;
	}

	/**
	 * Getter of inputManager
	 */
	public AudioManager getAudioManager() {
		return audioManager;
	}

	public Stage getStage() {
		return stage;
	}

	public Skin getSkin() {
		return skin;
	}

	public MapManager getMapManager(){
		return mapManager;
	}

	private void skinInit(){
		final ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
		FreeTypeFontGenerator FTFontGen = new FreeTypeFontGenerator(Gdx.files.internal("ui/MinimalPixel v2.ttf"));
		final FreeTypeFontGenerator.FreeTypeFontParameter FTFontPar= new FreeTypeFontGenerator.FreeTypeFontParameter();
		FTFontPar.minFilter = Texture.TextureFilter.Linear;
		FTFontPar.magFilter = Texture.TextureFilter.Linear;
		final int[] sizesFont = {16,20,26,32};
		for(int size : sizesFont){
			FTFontPar.size = size;
			resources.put("font_"+size, FTFontGen.generateFont(FTFontPar));
		}

		FTFontGen = new FreeTypeFontGenerator(Gdx.files.internal("ui/MinimalPixel v2.ttf"));
		FTFontPar.size = 16;
		resources.put("font2_16", FTFontGen.generateFont(FTFontPar));
		FTFontGen.dispose();

		final SkinLoader.SkinParameter skinPar = new SkinLoader.SkinParameter ("ui/hud.atlas", resources);
		assetManager.load("ui/hud.json", Skin.class, skinPar);
		assetManager.finishLoading();
		skin = assetManager.get("ui/hud.json", Skin.class);
	}

	/**
	 * Each frame will update World of Box2D of a fixed timestamp
	 */
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

	@Override
	public void dispose(){
		super.dispose();
		batch.dispose();
		stage.dispose();
		world.dispose();
		box2DDebugRenderer.dispose();
		assetManager.dispose();
	}
}