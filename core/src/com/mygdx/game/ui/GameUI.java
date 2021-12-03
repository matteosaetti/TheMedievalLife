package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.MyGdxGame;

public class GameUI extends Table {
    public GameUI(final MyGdxGame context) {
        super(context.getSkin());
        setFillParent(true);

        add(new TextButton("Blub", context.getSkin(), "huge"));
    }
}
