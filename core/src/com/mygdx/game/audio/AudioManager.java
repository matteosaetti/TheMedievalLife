package com.mygdx.game.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

/**
 * Class that manage to play (and stop) sounds like instances of Music, Sound, LoopingSound
 * @see TypeOfSound
 * and store music and LoopingSound that are currently playing
 *
 * The caller must provide an AssetManager with these audio loaded, and this manager will allow to use the methods
 * playAudio(), stopCurrentMusic(), stopLoopingSound()
 */
public class AudioManager {
    private AudioType currentMusicType;
    private Music currentMusic;
    private final AssetManager assetManager;

    private float volumeSoundModifier=1;

    //array of AudioType
    private final ArrayList<AudioType> loopSoundsPlaying_at;
    //arrays of LoopingSound associated to their AudioType
    private final ArrayList<LoopingSound> loopSoundsPlaying_ls;

    /**
     * Constructor that init the vars used
     *
     * @param assetManager AssetManager where are loaded the audio tracks
     */
    public AudioManager(final AssetManager assetManager){
        this.assetManager = assetManager;
        currentMusicType = null;
        currentMusic = null;

        loopSoundsPlaying_at = new ArrayList<>();
        loopSoundsPlaying_ls = new ArrayList<>();
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public void setVolumeSound(float volumeSoundModifier) {
        this.volumeSoundModifier = volumeSoundModifier;
    }




    /**
     * Play the audio track depending on the typeOfSound:
     * music: stop the current music, update it, and play the retrieved instance of Gdx.audio.Music from assetManager
     * sound: play the retrieved instance of Gdx.audio.Sound from assetManager
     * loopingSound: update the arrays of loopingSounds playing, create LoopingSound and plays it
     * @see LoopingSound
     *
     * @param audioType AudioType of the audio that will be played
     * @see AudioType
     */
    public void playAudio(AudioType audioType){
        switch(audioType.getTypeOfSound()){
            case MUSIC:
                if(currentMusicType == audioType){
                    return;
                }

                if(currentMusic != null){
                    currentMusic.stop();
                }

                currentMusicType = audioType;
                currentMusic = assetManager.get(audioType.getFilePath(), Music.class);
                currentMusic.setLooping(true);
                currentMusic.setVolume(audioType.getVolume());
                currentMusic.play();
                break;

            case SOUND:
                assetManager.get(audioType.getFilePath(), Sound.class).play(audioType.getVolume()*volumeSoundModifier);
                break;

            case LOOPINGSOUND:
                if(!loopSoundsPlaying_at.contains(audioType)){
                    LoopingSound loopingSound = new LoopingSound(audioType, assetManager);
                    loopingSound.setVolumeSoundModifier(volumeSoundModifier);

                    loopSoundsPlaying_at.add(audioType);
                    loopSoundsPlaying_ls.add(loopingSound);
                    loopingSound.play();
                }
        }

    }

    /**
     * Method for the caller to stop the current music stored that is playing
     */
    public void stopCurrentMusic(){
        if(currentMusic != null){
            currentMusic.stop();
            currentMusicType = null;
            currentMusic = null;
        }
    }

    /**
     * Stops a LoopingSound that is playing, updating the arrays
     *
     * @param audioType that identifies the LoopingSound instance that we want to stop
     */
    public void stopLoopingSound(AudioType audioType){
        int index = loopSoundsPlaying_at.indexOf(audioType);
        if(index != -1){
            loopSoundsPlaying_ls.get(index).stop();
            loopSoundsPlaying_at.remove(index);
            loopSoundsPlaying_ls.remove(index);
        }
    }


}



/**
 * Class for objects that manages the audioType.typeOfSound LOOPINGSOUND
 */
class LoopingSound{
    private Sound sound;
    private long soundId;
    private final AudioType audioType;
    private float volumeSoundModifier = 1;

    public void setVolumeSoundModifier(float volumeSoundModifier){
        this.volumeSoundModifier = volumeSoundModifier;
    }

    public LoopingSound(AudioType audioType, AssetManager assetManager) {
        this.audioType = audioType;
        this.sound =  assetManager.get(audioType.getFilePath(), Sound.class);
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public AudioType getAudioType() {
        return audioType;
    }

    void play(){
        soundId = sound.play();
        sound.setLooping(soundId, true);
        sound.setVolume(soundId, audioType.getVolume() * volumeSoundModifier);
    }

    void stop(){
        sound.stop();
    }

    /**
     * Needed for the method contains() of the array of LoopingSounds
     *
     * A loopingSound is equal to an other one if their sounds are equal
     *
     * @param obj comparing object
     * @return true if this object is equal to obj, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final LoopingSound other = (LoopingSound) obj;

        return this.sound.equals(other.sound);
    }


}