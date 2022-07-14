package com.mygdx.game.World.Entities.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.World.Entities.Player;

public class PlayerAnimation implements NpcAnimation{
    private Player player;
    private boolean direction_x = true;
    private boolean direction_y = true;
    private Animation<TextureAtlas.AtlasRegion> walkRightAnimation;
    private Animation<TextureAtlas.AtlasRegion> walkAnimation;
    private Animation<TextureAtlas.AtlasRegion> walkBackAnimation;
    private Animation<TextureAtlas.AtlasRegion> walkLeftAnimation;
    private Animation<TextureAtlas.AtlasRegion> idleRightAnimation;
    private Animation<TextureAtlas.AtlasRegion> idleAnimation;
    private Animation<TextureAtlas.AtlasRegion> idleBackAnimation;
    private Animation<TextureAtlas.AtlasRegion> idleLeftAnimation;
    private AssetManager assetManager;

    /**
     * The 4(till now) possible status of the player
     * */
    private enum Status {
        IDLEL, //stand left

        IDLE, //stand
        IDLEB, //stand back

        IDLER, //stand right
        WALKL, //walk left

        WALK, //walk
        WALKB, //walk back
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
        TextureAtlas idle = assetManager.get("player/idle.atlas", TextureAtlas.class);
        TextureAtlas idleBack = assetManager.get("player/idle_back.atlas", TextureAtlas.class);
        TextureAtlas idleRight = assetManager.get("player/idle_right.atlas", TextureAtlas.class);
        TextureAtlas idleLeft=assetManager.get("player/idle_left.atlas", TextureAtlas.class);
        TextureAtlas walkLeft=assetManager.get("player/walk_left.atlas", TextureAtlas.class);
        TextureAtlas walkRight=assetManager.get("player/walk_right.atlas", TextureAtlas.class);
        TextureAtlas walk=assetManager.get("player/walk.atlas", TextureAtlas.class);
        TextureAtlas walkBack=assetManager.get("player/walk_back.atlas", TextureAtlas.class);

        walkBackAnimation = new Animation<>(1/8f, walkBack.getRegions());
        walkAnimation = new Animation<>(1/8f, walk.getRegions());
        walkLeftAnimation=new Animation <>(1/8f, walkLeft.getRegions());
        walkRightAnimation=new Animation<>(1/8f, walkRight.getRegions());
        idleLeftAnimation=new Animation <>(1/5f, idleLeft.getRegions());
        idleRightAnimation=new Animation <>(1/5f, idleRight.getRegions());
        idleAnimation= new Animation<>(1/5f, idle.getRegions());
        idleBackAnimation = new Animation<>(1/5f, idleBack.getRegions());
    }


    /**
     * return current status of the player considering last direction and if it's moving
     */
    private PlayerAnimation.Status getStatus(){
        if(!player.B2DBody.getLinearVelocity().isZero()){ //Walk
            if(player.B2DBody.getLinearVelocity().x>0){ //right
                direction_x = true;
                return Status.WALKR;
            }
            else if (player.B2DBody.getLinearVelocity().x<0){ //left
                direction_x = false;
                return Status.WALKL;
            }
            else if(player.B2DBody.getLinearVelocity().y<0){
                direction_y = true;
                return Status.WALK;
            }
            else{
                direction_y = false;
                return Status.WALKB;
            }

        }
        else{ //Idle
            if(direction_x)   return Status.IDLER;
            else if(!direction_y)         return Status.IDLEL;
            else if(direction_y)    return Status.IDLE;
            else return Status.IDLEB;

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
            case IDLE:
                return idleAnimation;
            case IDLEB:
                return idleBackAnimation;
            case WALKL:
                return walkLeftAnimation;
            case WALK:
                return walkAnimation;
            case WALKB:
                return walkBackAnimation;
            case WALKR:
                return walkRightAnimation;
        }
    }
}