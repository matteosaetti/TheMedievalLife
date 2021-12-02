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


        final TiledMap tiledMap = assetManager.get("map/..", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);

        spawnCollisionAreas();
        context.getEcsEngine().createPlayer(map.getStartLocation(), 1,1);
    }

    @Override
    protected GameUI getScreenUI(final MyGdxGame context) {
        return new GameUI(context);
    }


    private void  spawnCollisionAreas(){
        final BodyDef bodyDef = new BodyDef();
        final FixtureDef fixtureDef = new FixtureDef();
        for(final CollisionArea collisionArea : map.getCollisionAreas()){

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

    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }
}
