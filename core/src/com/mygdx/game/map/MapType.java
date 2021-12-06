package com.mygdx.game.map;

public enum MapType {
    MAP_1("map/..");
    //MAP_2("map/..");

    private String filePath;

    MapType(final String filePath){
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
