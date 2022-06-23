package com.mygdx.game.World.Entities.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AnimationLoader {
    private static String[] paths = {
            //player
            "player/idle_right.atlas",
            "player/idle_left.atlas",
            "player/walk_left.atlas",
            "player/walk_right.atlas",




    };

    public static void loadAllAnimations(AssetManager assetManager) {
        for (String path : paths) {
            assetManager.load(path, TextureAtlas.class);
        }
    }

}
