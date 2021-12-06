package com.mygdx.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import java.util.EnumMap;

import static com.mygdx.game.MyGdxGame.BIT_GROUND;


public class MapManager {
    public static final String TAG = MapManager.class.getSimpleName();

    private final World world;
    private final Array<Body> bodies;

    private final AssetManager assetManager;
    private MapType currentMapType;
    private Map currentMap;
    private final EnumMap<MapType, Map> mapCache;
    private final Array<MapListener> listeners;

    public MapManager(final MyGdxGame context) {
        world = context.getWorld();
        assetManager = context.getAssetManager();
        this.currentMapType = null;
        this.currentMap = null;
        bodies = new Array<Body>();
        mapCache = new EnumMap<MapType, Map>(MapType.class);
        listeners = new Array<MapListener>();
    }

    public void addMapListener(final MapListener listener){
        listeners.add(listener);
    }
    public void setMap(final MapType type){
        if(currentMapType == type){
            //map is already set
            return;
        }
        if(currentMap != null){
            //cleanup current map entities/bodies
            world.getBodies(bodies);
            destroyCollisionAreas();
        }
        //set new map
        Gdx.app.debug(TAG, "Changing to map" + type);
        currentMap = mapCache.get(type);
        if(currentMap == null){
            Gdx.app.debug(TAG,"Creating new map of type" + type);
            final TiledMap tiledMap = assetManager.get(type.getFilePath(), TiledMap.class);
            mapCache.put(type, currentMap);
        }
        //create map entities/bodies
        spawnCollisionAreas();

        for(final MapListener listener : listeners){
            listener.mapChange(currentMap);
        }
    }


    private void destroyCollisionAreas() {
        for(final Body body : bodies){
            if("GROUND".equals(body.getUserData())){
                world.destroyBody(body);
            }
        }
    }
    private void spawnCollisionAreas() {
        MyGdxGame.resetBodyAndFixtureDefinition();
        for(final CollisionArea collisionArea : currentMap.getCollisionAreas()){
            MyGdxGame.BODY_DEF.position.set(collisionArea.getX(), collisionArea.getY());
            MyGdxGame.BODY_DEF.fixedRotation = true;
            final Body body = world.createBody(MyGdxGame.BODY_DEF);
            body.setUserData("GROUND");

            MyGdxGame.FIXTURE_DEF.filter.categoryBits = BIT_GROUND;
            MyGdxGame.FIXTURE_DEF.filter.maskBits = -1;
            final ChainShape chainShape = new ChainShape();
            chainShape.createChain(collisionArea.getVertices());
            MyGdxGame.FIXTURE_DEF.shape = chainShape;
            body.createFixture(MyGdxGame.FIXTURE_DEF);
            chainShape.dispose();
        }
    }

    public Map getCurrentMap() {
        return currentMap;
    }
}
