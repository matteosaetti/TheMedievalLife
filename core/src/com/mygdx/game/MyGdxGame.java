package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
	private EnumMap<ScreenType, AbstractScreen> screenCache;
	private FitViewport screenVieport;

	private World world;
	private Box2DDebugRenderer box2DDebugRenderer;

	public static final short BIT_CIRCLE = 1 << 0;
	public static final short BIT_BOX = 1 << 1;
	public static final short BIT_GROUND = 1 << 2;


	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Box2D.init();
		world= new World(new Vector2(0, 9.81f), true);
		box2DDebugRenderer = new Box2DDebugRenderer();

		screenVieport=new FitViewport(9,16);
		screenCache = new EnumMap<ScreenType, AbstractScreen>(ScreenType.class);
		setScreen(ScreenType.LOADING);
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
	public void dispose() {
		super.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
	}
}

