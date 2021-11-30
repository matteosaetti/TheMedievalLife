package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameUI extends Table {
    public GameUI(final Skin skin) {
        super(skin);
        setFillParent(true);

        add(new TextButton("Blub", skin, "huge"));
    }
}
