package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.ecs.ECSEngine;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.screen.AbstractScreen;
import com.mygdx.game.screen.ScreenType;
import com.badlogic.gdx.math.Vector2;


import java.util.EnumMap;


public class MyGdxGame extends Game {
	public static final String TAG = MyGdxGame.class.getSimpleName();
	private OrthographicCamera gameCamera;
	private SpriteBatch spriteBatch;
	private EnumMap<ScreenType, AbstractScreen> screenCache;
	private FitViewport screenViewport;

	private World world;
	private Box2DDebugRenderer box2DDebugRenderer;

	private WorldContactListener worldContactListener;

	private static final float FIXED_TIME_STEP = 1/ 60f;
	private  float accumulator;

	private AssetManager assetManager;

	private AudioManager audioManager;

	private Stage stage;
	private Skin skin;

	private I18NBundle i18NBundle;
	private ECSEngine ecsEngine;

	private InputManager inputManager;

	public static final float UNIT_SCALE = 1/32f;

	public static final short BIT_PLAYER = 1 << 0;
	public static final short BIT_GROUND = 1 << 1;


	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		spriteBatch = new SpriteBatch();


		//box2d stuff
		accumulator =  0;
		Box2D.init();
		world = new World(Vector2.Zero, true);
		worldContactListener = new WorldContactListener();
		world.setContactListener(worldContactListener);
		box2DDebugRenderer = new Box2DDebugRenderer();


		//initialize assetManager
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
		initializeSkin();
		stage = new Stage(new FitViewport(1280,720), spriteBatch);

		//audio
		audioManager = new AudioManager(this);

		//input
		inputManager = new InputManager();
		Gdx.input.setInputProcessor(new InputMultiplexer(inputManager, stage));

		//ecs engine
		ecsEngine = new ECSEngine(this);


		//set first screen
		gameCamera = new OrthographicCamera();
		screenViewport = new FitViewport(9,16);
		screenCache = new EnumMap<ScreenType, AbstractScreen>(ScreenType.class);
		setScreen(ScreenType.LOADING);


	}

	private void initializeSkin() {
		//setup markup colors
		Colors.put("Red", Color.RED);
		Colors.put("Blu", Color.BLUE);


		//generate ttf bitmaps
		final ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
		final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
		final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.minFilter = Texture.TextureFilter.Linear;
		fontParameter.magFilter = Texture.TextureFilter.Linear;
		final int[] sizeToCreate = {16,20,24,32};
		for(int size : sizeToCreate){
			fontParameter.size = size;
			final BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
			bitmapFont.getData().markupEnabled = true;
			resources.put("font_" + size, bitmapFont);
		}
		fontGenerator.dispose();

		//load skin
		final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("ui/hud.atlas", resources);
		assetManager.load("ui/...", Skin.class, skinParameter);
		assetManager.load("ui/strings", I18NBundle.class);
		assetManager.finishLoading();
		skin = assetManager.get("ui/...", Skin.class);
		i18NBundle = assetManager.get("ui/strings", I18NBundle.class);
	}

	public ECSEngine getEcsEngine() {
		return ecsEngine;
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public I18NBundle getI18NBundle() {
		return i18NBundle;
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

		return gameCamera;
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


	public void setScreen(final ScreenType screenType){

		final Screen screen = screenCache.get(screenType);
		if(screen==null){
				//screen not created it -> create it
			try {
				Gdx.app.debug(TAG, "Creating new screen " + screenType);
				final AbstractScreen newScreen = (AbstractScreen) ClassReflection.getConstructor(screenType.getScreenClass(), MyGdxGame.class).newInstance(this);
				screenCache.put(screenType, newScreen);
				setScreen(newScreen);
			} catch (ReflectionException e ) {
				throw new GdxRuntimeException("Screen " + screenType + "could not be created", e);
				}
		} else{
			Gdx.app.debug(TAG, "switching to screen: " + screenType );
			setScreen(screen);
		}
	}

	@Override
	public void render() {
		super.render();

		ecsEngine.update(Gdx.graphics.getDeltaTime());
		accumulator += Math.min(0.25f, Gdx.graphics.getDeltaTime());
		while(accumulator >= FIXED_TIME_STEP){
				world.step(FIXED_TIME_STEP,6,2);
				accumulator -= FIXED_TIME_STEP;
		}
		//final float alpha = accumulator / FIXED_TIME_STEP;
		stage.getViewport().apply();
		stage.act();
		stage.draw();
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

