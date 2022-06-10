package com.mygdx.game.World.Entities.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.World.Entities.Player;

public class PlayerAnimation {
    private Player player;
    private boolean direction = true;
    private Animation<TextureAtlas.AtlasRegion> walkRightAnimation;
    private Animation<TextureAtlas.AtlasRegion> walkLeftAnimation;
    private Animation<TextureAtlas.AtlasRegion> idleRightAnimation;
    private Animation<TextureAtlas.AtlasRegion> idleLeftAnimation;
    private AssetManager assetManager;

    /**
     * The 4(till now) possible status of the player
     * */
    private enum Status {
        IDLEL, //stand left
        IDLER, //stand right
        WALKL, //walk left
        WALKR //walk right
    }

    public PlayerAnimation(Player player, AssetManager assetManager){
        this.player = player;
        this.assetManager = assetManager;
        animationDef();
    }


    /**
     * Init TextureAtlas frames and related Animations
     */
    private void animationDef(){
        TextureAtlas idleRight=assetManager.get("player/idle_right.atlas", TextureAtlas.class);
        TextureAtlas idleLeft=assetManager.get("player/idle_left.atlas", TextureAtlas.class);
        TextureAtlas walkLeft=assetManager.get("player/walk_left.atlas", TextureAtlas.class);
        TextureAtlas walkRight=assetManager.get("player/walk_right.atlas", TextureAtlas.class);

        walkLeftAnimation=new Animation <>(1/8f, walkLeft.getRegions());
        walkRightAnimation=new Animation<>(1/8f, walkRight.getRegions());
        idleLeftAnimation=new Animation <>(1/5f, idleLeft.getRegions());
        idleRightAnimation=new Animation <>(1/5f, idleRight.getRegions());
    }


    /**
     * return current status of the player considering last direction and if it's moving
     */
    private PlayerAnimation.Status getStatus(){
        if(!player.B2DBody.getLinearVelocity().isZero()){ //Walk
            if(player.B2DBody.getLinearVelocity().x>0){ //right
                direction = true;
                return Status.WALKR;
            }
            else{ //left
                direction = false;
                return Status.WALKL;
            }

        }
        else{ //Idle
            if(direction)   return Status.IDLER;
            else            return Status.IDLEL;
        }
    }


    /**
     * return the animation for the current state of the player
     */
    public Animation<TextureAtlas.AtlasRegion> getCurrentAnimation(){
        switch (getStatus()){
            case IDLEL:
                return idleLeftAnimation;
            default:
            case IDLER:
                return idleRightAnimation;
            case WALKL:
                return walkLeftAnimation;
            case WALKR:
                return walkRightAnimation;
        }
    }
}
