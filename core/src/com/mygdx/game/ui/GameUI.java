package com.mygdx.game.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.World.Entities.Player;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;

public class GameUI extends Table implements InputListener {
    private static GameUI uniqueInstance;
    final ActionUI action;
    public static GameUI getInstance(final MyGdxGame context, final Skin skin, Player player){
        uniqueInstance = new GameUI(context, skin, player);
        return uniqueInstance;
    }
    public static GameUI getInstance(){
        if(uniqueInstance == null) {
            Gdx.app.error(GameUI.class.getSimpleName(), "tried to access to class GameUI, but no initialized yet ");
        }
        return uniqueInstance;
    }
    private GameUI(final MyGdxGame context, final Skin skin, Player player) {
        super(skin);

        //properties
        setDebug(false);
        pad(20);
        setFillParent(true);

    action = new ActionUI(skin);
    }


    @Override
    public void keyPressed(InputManager manager, GameKeys key) {

    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    public ActionUI getAction() {

        return action;
    }


    public void setBars(float v, float v1, float v2) {
    }
}
