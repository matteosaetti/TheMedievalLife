package com.mygdx.game.audio;

public enum AudioType {
    INTRO("audio/...",true, 0.3f),
    SELECT("audio/...", false, 0.5f);

    private final String filePath;
    private final boolean isMusic;
    private final float volume;

    AudioType(final String filePath, final boolean isMusic,final float volume){
            this.filePath = filePath;
            this.isMusic = isMusic;
            this.volume = volume;
    }

    public float getVolume() {
        return volume;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isMusic() {
        return isMusic;
    }
}
