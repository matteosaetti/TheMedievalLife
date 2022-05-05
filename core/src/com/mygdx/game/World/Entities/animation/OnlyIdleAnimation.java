package com.mygdx.game.World.Entities.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class OnlyIdleAnimation implements NpcAnimation{
    private final AssetManager assetManager;
    private final String animationAtlasPath;
    private Animation<TextureAtlas.AtlasRegion> idleAnimation;

    public OnlyIdleAnimation(AssetManager assetManager, String animationAtlasPath){
        this.assetManager=assetManager;
        this.animationAtlasPath= animationAtlasPath;

        TextureAtlas idle=assetManager.get(animationAtlasPath);
        idleAnimation=new Animation<TextureAtlas.AtlasRegion>(1/3f,idle.getRegions());
    }

    public Animation<TextureAtlas.AtlasRegion> getCurrentAnimation(){
        return idleAnimation;

    }
}
