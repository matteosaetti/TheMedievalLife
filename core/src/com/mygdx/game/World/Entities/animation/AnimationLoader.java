package com.mygdx.game.World.Entities.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AnimationLoader {
    private static String[] paths = {
            //player
            "player/idle_back.atlas",
            "player/idle.atlas",
            "player/idle_right.atlas",
            "player/idle_left.atlas",
            "player/walk_left.atlas",
            "player/walk.atlas",
            "player/walk_back.atlas",
            "player/walk_right.atlas",
            //NPC
            //Cavalier_1
            "NPC/Cavalier/idle_left.atlas",
            //Cavalier_2
            //"NPC/Cavalier/idle_right.atlas",
            //King
            "NPC/King/king.atlas",
            //Queen
            "NPC/Queen/idle.atlas",
            //Wizard
            "NPC/Wizard/idle1.atlas",
            //Merchant_1
            "NPC/Merchant/idle.atlas",
            //Merchant_2
            "NPC/Merchant/idle.atlas",
    };

    public static void loadAllAnimations(AssetManager assetManager) {
        for (String path : paths) {
            assetManager.load(path, TextureAtlas.class);
        }
    }

}
