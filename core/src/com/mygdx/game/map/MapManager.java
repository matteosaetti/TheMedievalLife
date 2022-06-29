package com.mygdx.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.World.Entities.Player;
import com.mygdx.game.World.WCreator;



public class MapManager {
    /**
      * public static final String TAG = MapManager.class.getSimpleName();
     */

    private final World world;
    private final AssetManager assetManager;
    private final MapType currentMapType;
    private TiledMap currentMap;
    private WCreator currentWCreator;
    private final Array<MapListener> listeners;
    private MapType safeMapLoader= null;
    private boolean safeMapLoaderUpdate = true;

    public MapManager(AssetManager assetManager, World world) {
        this.world = world;
        this.assetManager = assetManager;
        this.currentMapType = null;
        this.currentMap = null;
        listeners = new Array<>();
    }

    public MapType getCurrentMapType() {

        return currentMapType;
    }

    public TiledMap getCurrentMap() {

        return currentMap;
    }

    public void setMap(final MapType type){
        if(currentMapType == type){
            //map is already set
            return;
        }
        //set currentMap
        if(assetManager.isLoaded(type.getFilePath()))
            currentMap = assetManager.get(type.getFilePath(), TiledMap.class);
        else
            Gdx.app.error(MapManager.class.getSimpleName(), type.getFilePath() + "not loaded in AssetManager");

        //destroy bodies of old map if there are
        if(currentWCreator != null){
            currentWCreator.bodyDestroyer();
        }
        currentWCreator = new WCreator(world, currentMap);

        for(MapListener listener : listeners){
            listener.mapChange();
        }

        //debug
        Gdx.app.debug(MapManager.class.getSimpleName(), "Moving to new map: " + currentMapType.name());

    }

    public void playerAtSpawnMap(Player player){
        player.teleportTo(currentMapType.getSpawn());
    }

    public void addMapListener(final MapListener listener){

        listeners.add(listener);
    }

    public void loadMapSafe(MapType nextMap){
        this.safeMapLoader = nextMap;
        safeMapLoaderUpdate = false;
    }

    public void setSafeMapLoader() {
        if(!safeMapLoaderUpdate && !world.isLocked()){
            setMap(safeMapLoader);
            safeMapLoaderUpdate = true;
        }
    }

    public interface MapListener {
        void mapChange();
    }
}
