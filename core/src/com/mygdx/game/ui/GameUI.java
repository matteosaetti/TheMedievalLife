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
    private static GameUI uniqueIstance;
    final ActionUI action;
    public static GameUI getInstance(final MyGdxGame context, final Skin skin, Player player){
        uniqueIstance = new GameUI(context, skin, player);
        return uniqueIstance;
    }
    public static GameUI getInstance(){
        if(uniqueIstance == null) {
            Gdx.app.error(GameUI.class.getSimpleName(), "tried to access to class GameUI, but no initialized yet ");
        }
        return uniqueIstance;
    }
    public GameUI(final MyGdxGame context, final Skin skin, Player player) {
        super(skin);

        //properties
        setDebug(false);
        pad(20);
        setFillParent(true);


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
}
