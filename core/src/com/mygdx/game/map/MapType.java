package com.mygdx.game.map;

import com.badlogic.gdx.math.Vector2;

public enum MapType {
    WORLD("map/EsternoCastello.tmx", 9.5f,14, "Bosco");
    //CASTLE("map/InternoCastello.tmx",9.5f,14,"Interno del castello");

    private final String filePath;
    private final float y;
    private final float x;
    private final String desc;


    MapType(final String filePath, final float x, final float y, final String desc){

        this.filePath = filePath;
        this.y = y;
        this.x = x;
        this.desc = desc;

    }

    public static MapType getMapTypeByName(String MapTypeName) {
        for(MapType mapType : MapType.values()){
            if(mapType.name().equals(MapTypeName)){
                return mapType;
            }
        }
        return null;
    }

    public Vector2 getSpawn(){

        return new Vector2(x,y);
    }

    public String getDescription(){

        return desc;
    }
    public String getFilePath() {

        return filePath;
    }
}
