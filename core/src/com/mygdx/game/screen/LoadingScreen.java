package com.mygdx.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.input.GameKeys;

import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI>  {
    private final AssetManager assetManager;

    public LoadingScreen(final MyGdxGame context){

        super(context);

        this.assetManager= context.getAssetManager();
        assetManager.load("map/map/...", TiledMap.class);


    }



    @Override
    protected LoadingUI getScreenUI(final MyGdxGame context) {
        return new LoadingUI(context);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        assetManager.update();
        screenUI.setProgress(assetManager.getProgress());
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        if(assetManager.getProgress() >= 1) {
            context.setScreen(ScreenType.GAME);
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }
}
