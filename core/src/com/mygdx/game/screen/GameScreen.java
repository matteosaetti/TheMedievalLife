package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.World.Entities.Player;
import com.mygdx.game.World.Portal;
import com.mygdx.game.World.PortalListener;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapListener;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapManager;


public class GameScreen  extends AbstractScreen implements MapListener, InputListener, MapListener, PortalListener {

    //player
    private final Player playerB2D;

    //map
    private final MapManager mapManager;

    //camera

    public GameScreen(final MyGdxGame context) {
        super(context);

        //create player



        //initialized camera

        //map
        mapManager = context.getMapManager();
        mapManager.addMapListener(this);
       // mapManager.setMap(MapType.);


    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return new GameUI.getInstance(context,skin,playerB2D);
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

    @Override
    public void PortalCrossed(Portal portal) {

    }
}
