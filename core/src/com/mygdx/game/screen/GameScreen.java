package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.World.Entities.Player;
import com.mygdx.game.World.Portal;
import com.mygdx.game.World.PortalListener;
import com.mygdx.game.World.WorldContactListener;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.map.MapManager;

import static com.mygdx.game.MyGdxGame.UNIT_SCALE;


public class GameScreen  extends AbstractScreen implements MapManager.MapListener, InputListener, PortalListener {

    //player
    private final Player playerB2D;
    private final Vector2 savePCoordinates = new Vector2(11,12.5f);
    float elapsedTime=0;
    private boolean newMovementI = false;


    //map
    private final MapManager mapManager;
    public final OrthogonalTiledMapRenderer mapRenderer;
    private int[] layer_1 ={0,1,2,3};
    private int[] layer_2={4,5,6,7};
    private MapType portalDest;
    //camera
    private final OrthographicCamera orthographicCamera;

    private final GameUI gameUI;


    public GameScreen(Player playerB2D, final MyGdxGame context, OrthogonalTiledMapRenderer mapRenderer, int[] layer_2, MapType portalDest, OrthographicCamera orthographicCamera, GameUI gameUI) {
        super(context);
        this.playerB2D = playerB2D;

        //create player



        //initialized camera
        orthographicCamera = new OrthographicCamera(16,9);
        orthographicCamera.position.set(savePCoordinates,0);
        batch.setProjectMatrix(orthographicCamera.combined);
        //map init
        mapManager = context.getMapManager();
        mapRenderer= new OrthogonalTiledMapRenderer(null,UNIT_SCALE,batch);
        mapManager.addMapListener(this);

        mapManager.setMap(MapType.);

        WorldContactListener worldContactListener = new WorldContactListener(context);
        worldContactListener.addPortalListener(this);
        world.setContactListener(worldContactListener);


    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return new GameUI.getInstance(context,skin,playerB2D);
    }

    @Override
    public void show() {
        super.show();
        inputManager.addInputListener(this);
        inputManager.addInputListener(gameUI);
        //audioManager.playAudio();

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
    public void PortalCrossed(Portal portal) {

    }

    @Override
    public void mapChange() {

    }
}
