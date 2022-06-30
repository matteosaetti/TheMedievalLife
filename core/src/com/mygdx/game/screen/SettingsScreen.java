package com.mygdx.game.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.SettingsUI;

public class SettingsScreen extends AbstractScreen implements InputListener {

    AudioManager audioManager;

    public SettingsScreen(MyGdxGame context) {
        super(context);

        audioManager = context.getAudioManager();
    }
    @Override
    public void show() {
        super.show();
        ScreenUtils.clear(0,0,0,1);
        inputManager.addInputListener(this);
    }
    @Override
    public void render(float delta) {

        ScreenUtils.clear(1, 1, 1, 1);
    }

    @Override
    protected Table getScreenUI(Skin skin) {

        return new SettingsUI(skin, context);
    }


    public void keyPressed(InputManager manager, GameKeys key) {
        if (key == GameKeys.BACK) { //set screen 'MAINMENU'
            context.setScreen(ScreenType.MAINMENU);
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    @Override
    public void scroll(InputManager manager, float amount) {

    }
    public void hide(){
        super.hide();
        inputManager.removeInputListener(this);
    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
