package com.mygdx.game.ui;

public enum AnimationType {
    HERO_MOVE_UP("characters_and_effects/..", "hero", 0.05f, 0),
    HERO_MOVE_DOWN("characters_and_effects/..", "hero", 0.05f, 2),
    HERO_MOVE_RIGHT("characters_and_effects/..", "hero", 0.05f, 3),
    HERO_MOVE_LEFT("characters_and_effects/..", "hero", 0.05f, 1);

    private final String atlasPath;
    private final String atlasKey;
    private final float frameTime;
    private final int rawIndex;


    AnimationType(String atlasPath, String atlasKey, float frameTime, int rawIndex) {
        this.atlasPath = atlasPath;
        this.atlasKey = atlasKey;
        this.frameTime = frameTime;
        this.rawIndex = rawIndex;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public String getAtlasKey() {
        return atlasKey;
    }

    public int getRawIndex() {
        return rawIndex;
    }

    public float getFrameTime() {
        return frameTime;
    }
}
