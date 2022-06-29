package com.mygdx.game.screen;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.World.Entities.animation.AnimationLoader;
import com.mygdx.game.World.Entities.animation.PlayerAnimation;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.input.GameKeys;

import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.LoadingUI;

public class LoadingScreen extends AbstractScreen  {
    private final AssetManager assetManager;
    private final Color color;

    public LoadingScreen(final MyGdxGame context){
        super(context);
        this.color = context.getSkin().getColor("neroOpaco");

        //asset manager
        assetManager= context.getAssetManager();
        //load maps
        for(MapType map : MapType.values()){
            context.getAssetManager().load(map.getFilePath(), TiledMap.class);
        }
        //load audio

        for(final AudioType audioType : AudioType.values()){
            if(audioType.isMusic()) {
                assetManager.load(audioType.getFilePath(), audioType.isMusic() ? Music.class : Sound.class);
            }
            else{
                assetManager.load(audioType.getFilePath(), Sound.class);
            }
        }
        //load Animation
        AnimationLoader.loadAllAnimations(assetManager);


    }


    @Override
    protected Table getScreenUI(Skin skin){
        return new LoadingUI(skin);
    }

    @Override
    public void show() {
        super.show();
        ScreenUtils.clear(color);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(color);

       if(assetManager.update()){
            context.setScreen(ScreenType.MAINMENU);
        }
        ((LoadingUI) screenUI).setLoadingStatus(assetManager.getQueuedAssets());
        ((LoadingUI) screenUI).setProgressBar(assetManager.getProgress());


    }
    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }



    @Override
    public void hide() {
        super.hide();

    }

    @Override
    public void dispose() {

    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {

    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    @Override
    public void scroll(InputManager manager, float amount) {

    }
}
