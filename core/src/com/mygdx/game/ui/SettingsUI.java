package com.mygdx.game.ui;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.MyGdxGame;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsUI extends Table {
    private Slider musicVolume;
    private Slider effectsVolume;
    private Label musicValue;
    private Label effectsValue;
    private Sound sound;

    public SettingsUI(Skin skin, final MyGdxGame context) {
        super(skin);
        setFillParent(true);

        Label settingsMenu=new Label("SETTINGS MENU", skin, "huge");
        final Label music = new Label("MUSIC", skin, "huge");
        Label effects=new Label("EFFECTS", skin, "huge");

        musicValue=new Label("0",skin,"normal");
        effectsValue=new Label("0",skin,"normal");

        musicVolume=new Slider(0,1,0.01f,false,skin,"default");
        musicVolume.addListener(new ChangeListener() {


            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                musicValue.setText((int) (musicVolume.getValue() * 100));
                context.getAudioManager().getCurrentMusic().setVolume(musicVolume.getValue());
            }

        });
        effectsVolume=new Slider(0,1,0.01f,false,skin,"default");
        effectsVolume.addListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                effectsValue.setText((int) (effectsVolume.getValue() * 100));
                context.getAudioManager().setVolumeSound(effectsVolume.getValue());
            }
        });

        setDebug(false);

        add(settingsMenu).colspan(3).top().center();
        row().padTop(100);

        add(music).colspan(3).center();
        row();
        add(musicValue).colspan(3).center();
        row();
        add(new Image(skin.getDrawable("SliderBegin"))).right();
        add(musicVolume).fillX();
        add(new Image(skin.getDrawable("SliderBegin"))).left();

        row().padTop(20);

        add(effects).colspan(3).center();
        row();
        add(effectsValue).colspan(3).center();
        row();
        add(new Image(skin.getDrawable("SliderBegin"))).right();
        add(effectsVolume).fillX();
        add(new Image(skin.getDrawable("SliderBegin"))).left();

        musicVolume.setValue(1);
        effectsVolume.setValue(1);
    }

}

