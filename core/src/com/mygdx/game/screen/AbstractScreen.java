package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;

public abstract class AbstractScreen implements Screen {
    protected final MyGdxGame context;
    protected final FitViewport viewport;
    protected final World world;
    protected final Box2DDebugRenderer box2DDebugRenderer;
    protected final WidgetGroup screenUI;
    protected final Stage stage;
    protected final InputManager inputManager;
    protected final AudioManager audioManager;
    protected final SpriteBatch batch;

    public AbstractScreen(final MyGdxGame context){
        this.context = context;

        viewport = context.getScreenViewport();
        this.world = context.getWorld();
        this.batch = context.getSpriteBatch();
        this.box2DDebugRenderer = context.getBox2DDebugRenderer();
        inputManager = context.getInputManager();
        audioManager = context.getAudioManager();
        stage = context.getStage();
        screenUI = getScreenUI(context.getSkin());
    }

    protected abstract WidgetGroup getScreenUI(Skin skin);

    @Override
    public void show() {

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
        stage.getRoot().removeActor(screenUI);
    }

    @Override
    public void dispose() {

    }

    public abstract void keyPressed(InputManager manager, GameKeys key);

    public abstract void keyUp(InputManager manager, GameKeys key);

    public abstract void scroll(InputManager manager, float amount);
}
