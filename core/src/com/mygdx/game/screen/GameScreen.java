package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.World.Entities.NPC.NPC_handler;
import com.mygdx.game.World.Entities.Player;
import com.mygdx.game.World.Portal;
import com.mygdx.game.World.PortalListener;
import com.mygdx.game.World.WorldContactListener;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.ui.inventory.Inventory;
import com.mygdx.game.utils.JsonProfile;

import static com.mygdx.game.MyGdxGame.UNIT_SCALE;


public class GameScreen  extends AbstractScreen implements MapManager.MapListener, InputListener, PortalListener {

    //player
    private final Player playerB2D;
    private final Vector2 savePCoordinates = new Vector2(11,12.5f);
    float elapsedTime=0;
    private boolean newMovementInput = false;
    private NPC_handler npc_handler;
    private Inventory inventory;

    //map
    private final MapManager mapManager;
    public final OrthogonalTiledMapRenderer mapRenderer;
    private final int[] layer_1 ={0,1,2,3};
    private final int[] layer_2={4,5,6,7};
    private MapType portalDest;
    //camera
    private final OrthographicCamera orthographicCamera;

    private final GameUI gameUI;


    public GameScreen(final MyGdxGame context) {
        super(context);
        gameUI = (GameUI) screenUI;


        //create player
        playerB2D = new Player(world, savePCoordinates,gameUI, context.getAssetManager());
        inventory = gameUI.getInventory();
        JsonProfile.loadInventory("mainProfile", inventory);

        // creating NPC_handler
        npc_handler = new NPC_handler(playerB2D);

        //initialized camera
        orthographicCamera = new OrthographicCamera(16,9);
        orthographicCamera.position.set(savePCoordinates,0);
        batch.setProjectionMatrix(orthographicCamera.combined);


        //map init
        mapManager = context.getMapManager();
        mapRenderer= new OrthogonalTiledMapRenderer(null,UNIT_SCALE,batch);
        mapManager.addMapListener(this);

        mapManager.setMap(MapType.WORLD);
        JsonProfile.loadLocation("mainProfile", playerB2D, mapManager);
        WorldContactListener worldContactListener= new WorldContactListener(context);
        worldContactListener.addPortalListener(this);
        world.setContactListener(worldContactListener);


    }

    @Override
    protected  Table getScreenUI(Skin skin){
        return GameUI.getInstance(context,skin,playerB2D);
    }


    @Override
    public void show() {
        super.show();
        inputManager.addInputListener(this);
        inputManager.addInputListener(gameUI);
        audioManager.playAudio(AudioType.AMBIENT_PALATINO);
        JsonProfile.loadLocation("mainProfile", playerB2D, mapManager);

    }

    @Override
    public void render(float delta) {
        elapsedTime = (elapsedTime + Gdx.graphics.getDeltaTime()) % 10;

        //update map
        mapManager.setSafeMapLoader();

        //update player to new speed after moving inputs
        if(newMovementInput){
            playerB2D.B2DBody.applyLinearImpulse(
                    playerB2D.getSpeedX()-playerB2D.B2DBody.getLinearVelocity().x,
                    playerB2D.getSpeedY()-playerB2D.B2DBody.getLinearVelocity().y,
                    playerB2D.B2DBody.getWorldCenter().x, playerB2D.B2DBody.getWorldCenter().y,
                    true);
        }
        //camera follows player
        orthographicCamera.position.x = playerB2D.B2DBody.getPosition().x;
        orthographicCamera.position.y = playerB2D.B2DBody.getPosition().y;
        orthographicCamera.update();

        //footsteps sound if player is moving
       // if(playerB2D.B2DBody.getLinearVelocity().isZero())
           // audioManager.stopLoopingSound(AudioType.FOOTSTEPS_STONE);
        //else
            //audioManager.playAudio(AudioType.FOOTSTEPS_STONE);

        //update NPCs
        npc_handler.update();

        renderDraw(delta);
    }

    void renderDraw(float delta){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT| GL20.GL_DEPTH_BUFFER_BIT);

        //map render background layers
        if(mapRenderer.getMap() != null){
            mapRenderer.setView(orthographicCamera);
            mapRenderer.render(layer_1);
        }
        //drawing player
        batch.begin();
        npc_handler.draw(batch, elapsedTime);
        playerB2D.draw(batch, elapsedTime);
        batch.end();


        //drawing last layers of the map
        mapRenderer.render(layer_2);

        //World of B2D
        world.step(delta, 6, 2);
        box2DDebugRenderer.render(world, orthographicCamera.combined);
    }



    @Override
    public void resize(int width, int height) {
        JsonProfile.saveProfile("mainProfile", playerB2D, inventory, mapManager.getCurrentMapType());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        super.hide();
        inputManager.removeInputListener(this);
        inputManager.removeInputListener(gameUI);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }

    public void keyPressed(InputManager manager, GameKeys key) {
        switch (key) {
            /*
             * Player Movement
             */
            case UP:
                playerB2D.setSpeedY(playerB2D.NOMINAL_SPEED);
                newMovementInput = true;
                break;
            case DOWN:
                newMovementInput = true;
                playerB2D.setSpeedY(-playerB2D.NOMINAL_SPEED);
                break;
            case LEFT:
                newMovementInput = true;
                playerB2D.setSpeedX(-playerB2D.NOMINAL_SPEED);
                break;
            case RIGHT:
                newMovementInput = true;
                playerB2D.setSpeedX(playerB2D.NOMINAL_SPEED);
                break;

            case NEXT:
                if(portalDest != null){
                    context.getMapManager().loadMapSafe(portalDest);

                    GameUI.getInstance().getDialogue().loadConversation(null, portalDest.getDescription());
                    GameUI.getInstance().getDialogue().setVisible(true);
                    Gdx.app.debug(this.getClass().getSimpleName(), "Player passed portal to " + portalDest);
                    portalDest = null;
                }
                break;
            /*
             * BOX2D DEBUG COMMANDS
             */
            case DEBUG7:    //set velocity visible or not
                box2DDebugRenderer.setDrawVelocities(!box2DDebugRenderer.isDrawVelocities());
                break;
            case DEBUG8:    //set contacts visible or not
                box2DDebugRenderer.setDrawContacts(!box2DDebugRenderer.isDrawContacts());
                break;
            case DEBUG9:    //set hitbox visible or not
                box2DDebugRenderer.setDrawBodies(!box2DDebugRenderer.isDrawBodies());
                break;

            /*
             * Screens
             */
            case BACK:
                if (gameUI.getInventory().isOpened() || gameUI.getDialogue().isVisible()){
                    break;
                }
                savePCoordinates.set(playerB2D.B2DBody.getPosition().x, playerB2D.B2DBody.getPosition().y);
                JsonProfile.saveProfile("mainProfile", playerB2D, inventory, mapManager.getCurrentMapType());
                context.setScreen(ScreenType.MAINMENU);

                /*
                 * DEBUG NEW FEATURES
                 */
            case DEBUG:
                break;

            default:
                break;
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {
        switch (key){
            case UP:
                newMovementInput=true;
                playerB2D.setSpeedY(manager.isKeyPressed(GameKeys.DOWN) ? -playerB2D.NOMINAL_SPEED : 0);
                break;
            case DOWN:
                newMovementInput=true;
                playerB2D.setSpeedY(manager.isKeyPressed(GameKeys.UP) ? -playerB2D.NOMINAL_SPEED : 0);
                break;
            case LEFT:
                newMovementInput=true;
                playerB2D.setSpeedX(manager.isKeyPressed(GameKeys.RIGHT) ? -playerB2D.NOMINAL_SPEED : 0);
                break;
            case RIGHT:
                newMovementInput=true;
                playerB2D.setSpeedX(manager.isKeyPressed(GameKeys.LEFT) ? -playerB2D.NOMINAL_SPEED : 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void scroll(InputManager manager, float amount) {

    }


    @Override
    public void PortalCrossed(Portal portal) {
        portalDest= portal.getDestinationMapType();
    }

    @Override
    public void mapChange() {
        mapRenderer.setMap(mapManager.getCurrentMap());
        mapManager.playerAtSpawnMap(playerB2D);
        npc_handler.clearAllNPCs();
        JsonProfile.loadNPCs(npc_handler,mapManager.getCurrentMapType(),world, context.getAssetManager());

    }
}
