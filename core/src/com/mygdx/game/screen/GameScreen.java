package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;

import com.mygdx.game.MyGdxGame;

import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.map.CollisionArea;
import com.mygdx.game.map.Map;

import static com.mygdx.game.MyGdxGame.*;


public class GameScreen  extends AbstractScreen<GameUI>{

    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    private boolean directionChange;
    private int xFactor;
    private int yFactor;

    private  Body player;
    private final AssetManager assetManager;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera gameCamera;
    private final GLProfiler profiler;
    private final Map map;

    public GameScreen(final MyGdxGame context) {
        super(context);
        assetManager = context.getAssetManager();
        this.gameCamera = context.getGameCamera();
        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getSpriteBatch());
        profiler = new GLProfiler(Gdx.graphics);
         //    profiler.enable();

        bodyDef = new BodyDef();
        fixtureDef= new FixtureDef();

        final TiledMap tiledMap = assetManager.get("map/map/..", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);

        spawnCollisionAreas();
        spawnPlayer();
    }

    @Override
    protected GameUI getScreenUI(final MyGdxGame context) {
        return new GameUI(context);
    }

    private void spawnPlayer() {
        resetBodyAndFixtureDefinitions();

        //create a PLAYER
        bodyDef.position.set(map.getStartLocation());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        player = world.createBody(bodyDef);
        player.setUserData("PLAYER");

        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = pShape;
        player.createFixture(fixtureDef);
        pShape.dispose();

    }

    private void resetBodyAndFixtureDefinitions(){
        bodyDef.position.set(0,0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = false;

        fixtureDef.density = 0;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.shape = null;

    }
    private void  spawnCollisionAreas(){
        for(final CollisionArea collisionArea : map.getCollisionAreas()){
            resetBodyAndFixtureDefinitions();

            //create a room
            bodyDef.position.set(collisionArea.getX() + 0.5f, collisionArea.getY() + 0.5f);
            final Body body = world.createBody(bodyDef);
            body.setUserData("GROUND");

            fixtureDef.filter.categoryBits = BIT_GROUND;
            fixtureDef.filter.maskBits = -1;
            final ChainShape chainShape = new ChainShape();
            chainShape.createLoop(collisionArea.getVertices());
            fixtureDef.shape = chainShape;
            body.createFixture(fixtureDef);
            chainShape.dispose();

        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(directionChange) {
            player.applyLinearImpulse(
                    (xFactor - player.getLinearVelocity().x) * player.getMass(),
                    (yFactor - player.getLinearVelocity().y) * player.getMass(),
                    player.getWorldCenter().x, player.getWorldCenter().y, true

            );
        }

        viewport.apply(true);
        mapRenderer.setView(gameCamera);
        mapRenderer.render();
        box2DDebugRenderer.render(world, viewport.getCamera().combined);
        Gdx.app.debug("RenderInfo", "Bindings " + profiler.getTextureBindings());
        Gdx.app.debug("RenderInfo", "Draw calls " + profiler.getDrawCalls());
        profiler.reset();
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
        mapRenderer.dispose();
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        switch (key){
            case LEFT:
                directionChange = true;
                xFactor = -1;
                break;
            case RIGHT:
                directionChange = true;
                xFactor = 1;
                break;
            case UP:
                directionChange = true;
                yFactor = 1;
                break;
            case DOWN:
                directionChange = true;
                yFactor = -1;
                break;
            default:
                //nothing
                return;
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {
        switch (key) {
            case LEFT:
                directionChange = true;
                xFactor = manager.isKeyPressed(GameKeys.RIGHT) ? 1 : 0;
                break;
            case RIGHT:
                directionChange = true;
                xFactor = manager.isKeyPressed(GameKeys.LEFT) ? -1 : 0;
                break;
            case UP:
                directionChange = true;
                yFactor = manager.isKeyPressed(GameKeys.UP) ? -1 : 0;
                break;
            case DOWN:
                directionChange = true;
                yFactor = manager.isKeyPressed(GameKeys.DOWN) ? 1 : 0;
                break;
            default:
                //nothing
                return;
        }
    }
}
