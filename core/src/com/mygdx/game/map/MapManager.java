package com.mygdx.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.map.MapType;
import com.mygdx.game.World.Entities.Player;
import com.mygdx.game.World.WCreator;

public class MapManager {
    private MapType currentMapType;
    private TiledMap currentMap;
    private final AssetManager assetManager;
    private final World world;
    private WCreator currentWorldCreator;
    private final Array<MapListener> mapListeners;
    private MapType safeMapLoaderNextMap = null;
    private boolean safeMapLoaderUpdated = true;

    public MapManager(AssetManager assetManager, World world){
        this.assetManager = assetManager;
        this.world = world;
        mapListeners = new Array<>();
        currentMapType = null;
        currentMap = null;
    }

    public MapType getCurrentMapType() {
        return currentMapType;
    }

    public TiledMap getCurrentMap() {
        return currentMap;
    }

    public void loadMap(MapType mapType) {
        if(currentMapType == null || !mapType.getFilePath().equals(currentMapType.getFilePath())){
            //set current MapType
            currentMapType = mapType;

            //set currentMap
            if(assetManager.isLoaded(mapType.getFilePath()))
                currentMap = assetManager.get(mapType.getFilePath(), TiledMap.class);
            else
                Gdx.app.error(MapManager.class.getSimpleName(), mapType.getFilePath() + "not loaded in AssetManager");

            //destroy bodies of old map if there are
            if(currentWorldCreator != null){
                currentWorldCreator.bodyDestroyer();
            }
            currentWorldCreator = new WCreator(world, currentMap);

            for(MapListener listener : mapListeners){
                listener.mapChanged();
            }

            //debug
            Gdx.app.debug(MapManager.class.getSimpleName(), "Moving to new map: " + currentMapType.name());

        }
    }

    public void playerAtSpawnMap(Player player){
        player.teleportTo(currentMapType.getSpawn());
    }

    public void addMapListener(final MapListener mapListener) {
        mapListeners.add(mapListener);
    }

    public void loadMapSafe(MapType nextMap){
        this.safeMapLoaderNextMap = nextMap;
        safeMapLoaderUpdated = false;
    }

    public void safeMapLoader(){
        if (!safeMapLoaderUpdated && !world.isLocked()){
            loadMap(safeMapLoaderNextMap);
            safeMapLoaderUpdated = true;
        }
    }

    public interface MapListener {
        void mapChanged();
    }
}
