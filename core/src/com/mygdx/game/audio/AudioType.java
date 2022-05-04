package com.mygdx.game.audio;
enum TypeOfSound {
    //Only one can be played at time; looping; instance of Gdx.audio.Music; should be used for background > 2 sec
    MUSIC,
    //Different and same sound can be played at the same time; not looping; instance of Gdx.audio.Sound; should be used for effects <2 sec
    SOUND,
    //Different sound can be played at the same time, not the same; looping; instance of Gdx.audio.Sound; should be used for looping effects <2 sec
    LOOPINGSOUND;
}

/**
 * The AudioTypes are NOT the type of sound (as TypeOfSound), but are containers of the info of the sounds, that can be
 * identified from the AudioManager thanks to filePath and in general by the name of the objects of the enum
 */
public enum AudioType {
    THEME1("audio/background-theme-1.wav",TypeOfSound.MUSIC, 0.5f),
    THEME2("audio/background-theme-2.wav", TypeOfSound.MUSIC, 0.5f),
    AMBIENT_PALATINO("audio/background-ambient-palatino.wav", TypeOfSound.MUSIC, 0.5f),
    CLICK1_LIGHT("audio/click_light.wav", TypeOfSound.SOUND, 0.5f),
    CLICK2_HEAVY("audio/click_heavy.wav", TypeOfSound.SOUND, 0.5f),
    CLICK3_SUCCESS("audio/click_success.wav", TypeOfSound.SOUND, 0.5f),
    FOOTSTEPS_STONE("audio/footsteps/footsteps-stone.wav",TypeOfSound.LOOPINGSOUND,1f),
    FOOTSTEPS_CLEAN("audio/footsteps/footsteps-clean.wav",TypeOfSound.LOOPINGSOUND,0.5f);

    private final String filePath;
    private final TypeOfSound typeOfSound;
    private final float volume;

    /**
     * Constructor
     *
     * @param filePath relative path for internal assets of the audio track
     * @param typeOfSound identifier of the type of sound and so how the AudioManager will behave
     * @param volume float from 0 to 1, percentage of the volume of that sound
     */
    AudioType(final String filePath, final TypeOfSound typeOfSound, final  float volume){
        this.filePath = filePath;
        this.typeOfSound = typeOfSound;
        this.volume = volume;
    }

    public String getFilePath() {
        return filePath;
    }

    public TypeOfSound getTypeOfSound() {
        return typeOfSound;
    }

    /**
     * Can be used to know the class of the object considered by the AudioManager, for example to load the asset in the
     * AssetManager (Music.class if true, Sound.class if false)
     *
     * @return true if it's a Music, false otherwise
     */
    public boolean isMusic(){
        return typeOfSound == TypeOfSound.MUSIC;
    }

    public float getVolume() {
        return volume;
    }
}