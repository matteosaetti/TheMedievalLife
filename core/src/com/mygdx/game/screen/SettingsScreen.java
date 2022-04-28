package com.mygdx.game.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;

public class SettingsScreen extends AbstractScreen implements InputListener {

    AudioManager audioManager;

    public SettingsScreen(MyGdxGame context) {
        super(context);

        audioManager = context.getAudioManager();
    }

    @Override
    protected Table getScreenUI(Skin skin) {

        return new SettingUI(skin, context);
    }


    @Override
    public void keyPressed(InputManager manager, GameKeys key) {

    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }
}
