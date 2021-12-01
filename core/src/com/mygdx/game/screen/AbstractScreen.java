package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;

public abstract class AbstractScreen<T extends Table> implements Screen, InputListener {
    protected final MyGdxGame context;
    protected final FitViewport viewport;
    protected final World world;
    protected final Box2DDebugRenderer box2DDebugRenderer;
    protected final T screenUI;
    protected final Stage stage;
    protected final InputManager inputManager;

    public AbstractScreen(final MyGdxGame context){
        this.context = context;
        viewport = context.getScreenVieport();
        this.world = context.getWorld();
        this.box2DDebugRenderer = context.getBox2DDebugRenderer();
        inputManager = context.getInputManager();

        stage = context.getStage();
        screenUI = getScreenUI(context);
    }

    protected abstract T getScreenUI(final MyGdxGame context);

    @Override
    public void show() {
        inputManager.addInputListener(this);
        stage.addActor(screenUI);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        stage.getViewport().update(width,height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        inputManager.removeInputListener(this);
        stage.getRoot().removeActor(screenUI);
    }

    @Override
    public void dispose() {

    }
}
