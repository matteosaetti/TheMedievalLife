package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import screen.AbstractScreen;
import screen.ScreenType;
import com.badlogic.gdx.math.Vector2;


import java.util.EnumMap;

public class MyGdxGame extends Game {
	public static final String TAG = MyGdxGame.class.getSimpleName();
	private OrthographicCamera gameCamera;
	private SpriteBatch spriteBatch;
	private EnumMap<ScreenType, AbstractScreen> screenCache;
	private FitViewport screenVieport;

	private World world;
	private Box2DDebugRenderer box2DDebugRenderer;

	private WorldContactListener worldContactListener;

	private static final float FIXED_TIME_STEP = 1/ 60f;
	private  float accumulator;

	private AssetManager assetManager;

	public static final float UNIT_SCALE = 1/32f;

	public static final short BIT_PLAYER = 1 << 0;
	public static final short BIT_GROUND = 1 << 2;


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

		//set first screen
		gameCamera = new OrthographicCamera();
		screenVieport = new FitViewport(9,16);
		screenCache = new EnumMap<ScreenType, AbstractScreen>(ScreenType.class);
		setScreen(ScreenType.LOADING);


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

	public  FitViewport getScreenVieport(){ return screenVieport; }

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

		Gdx.app.debug(TAG, "" + Gdx.graphics.getRawDeltaTime());

		accumulator += Math.min(0.25f, Gdx.graphics.getRawDeltaTime());
		while(accumulator >= FIXED_TIME_STEP){
				world.step(FIXED_TIME_STEP,6,2);
				accumulator -= FIXED_TIME_STEP;
		}
		//final float alpha = accumulator / FIXED_TIME_STEP;
	}

	@Override
	public void dispose() {
		super.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
		assetManager.dispose();
	}
}

