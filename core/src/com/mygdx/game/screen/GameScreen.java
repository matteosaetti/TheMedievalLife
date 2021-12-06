package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapListener;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapManager;


public class GameScreen  extends AbstractScreen<GameUI> implements MapListener {

    private final MapManager mapManager;

    public GameScreen(final MyGdxGame context) {
        super(context);

        mapManager = context.getMapManager();
        mapManager.addMapListener(this);
       // mapManager.setMap(MapType.);

        context.getEcsEngine().createPlayer(mapManager.getCurrentMap().getStartLocation(), 0.75f,0.75f);
    }

    @Override
    protected GameUI getScreenUI(final MyGdxGame context) {
        return new GameUI(context);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //TODO remove map change test stuff
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
           // mapManager.setMap(MapType.MAP_1);

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
            // mapManager.setMap(MapType.MAP_2);
        }
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

    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    @Override
    public void mapChange(Map currentMap) {

    }
}
