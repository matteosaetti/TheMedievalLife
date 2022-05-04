package com.mygdx.game.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;



public class AudioManager {
    private AudioType currentMusicType;
    private Music currentMusic;
    private final AssetManager assetManager;

    private float volumeSound = 1;

    private final ArrayList<AudioType> loopSoundsPlaying_at;
    private final ArrayList<LoopingSound> loopSoundsPlaying_ls;

    public AudioManager(final MyGdxGame context) {
        this.assetManager = context.getAssetManager();
        currentMusic = null;
        currentMusicType = null;

        loopSoundsPlaying_at = new ArrayList<>();
        loopSoundsPlaying_ls = new ArrayList<>();

    }

    public Sound getCurrentMusic() {
        return (Sound) currentMusic;
    }

    public void setVolumeSound(float volumeSoundModifier) {
        this.volumeSound = volumeSoundModifier;
    }

    public void playAudio(final AudioType type) {
        switch (type.getTypeOfSound()) {
            case MUSIC:
                //play music
                if (currentMusicType == type) {
                    //given audio type is already playing
                    return;
                } else if (currentMusicType != null) {
                    currentMusic.stop();
                }
                currentMusicType = type;
                currentMusic = assetManager.get(type.getFilePath(), Music.class);
                currentMusic.setLooping(true);
                currentMusic.setVolume(type.getVolume());
                currentMusic.play();
                break;
            case SOUND:
                //play sound
                assetManager.get(type.getFilePath(), Sound.class).play(type.getVolume());
                break;
            case LOOPINGSOUND:
                if (!loopSoundsPlaying_at.contains(type)) {
                    LoopingSound loopingSound = new LoopingSound(type, assetManager);
                    loopingSound.setVolumeSoundModifier(volumeSound);

                    loopSoundsPlaying_at.add(type);
                    loopSoundsPlaying_ls.add(loopingSound);
                    loopingSound.play();
                }
        }
    }


    public void stopCurrentMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
            currentMusicType = null;
        }
    }


    public void stopLoopingSound(AudioType audioType) {
        int index = loopSoundsPlaying_at.indexOf(audioType);
        if (index != -1) {
            loopSoundsPlaying_ls.get(index).stop();
            loopSoundsPlaying_at.remove(index);
            loopSoundsPlaying_ls.remove(index);
        }
    }
}
     /* Class for objects that manages the audioType.typeOfSound LOOPINGSOUND
 */
    class LoopingSound {
         private Sound sound;
         private long soundId;
         private final AudioType audioType;
         private float volumeSoundModifier = 1;

         public void setVolumeSoundModifier(float volumeSoundModifier) {
             this.volumeSoundModifier = volumeSoundModifier;
         }

         public LoopingSound(AudioType audioType, AssetManager assetManager) {
             this.audioType = audioType;
             this.sound = assetManager.get(audioType.getFilePath(), Sound.class);
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

         void play() {
             soundId = sound.play();
             sound.setLooping(soundId, true);
             sound.setVolume(soundId, audioType.getVolume() * volumeSoundModifier);
         }

         void stop() {
             sound.stop();
         }

         /**
          * Needed for the method contains() of the array of LoopingSounds
          * <p>
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