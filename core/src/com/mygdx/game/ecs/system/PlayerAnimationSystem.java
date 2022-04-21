package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ecs.ECSEngine;
import com.mygdx.game.ecs.component.AnimationComponent;
import com.mygdx.game.ecs.component.B2DComponent;
import com.mygdx.game.ecs.component.PlayerComponent;
import com.mygdx.game.ui.AnimationType;

public class PlayerAnimationSystem extends IteratingSystem {
    public PlayerAnimationSystem(MyGdxGame context) {
        super(Family.all(AnimationComponent.class, PlayerComponent.class, B2DComponent.class).get());
    }



    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);

        if(b2DComponent.body.getLinearVelocity().equals(Vector2.Zero)){
            //don't move
            animationComponent.aniTime = 0;

        }else if(b2DComponent.body.getLinearVelocity().x > 0){
            //player moves to the right
            animationComponent.aniType = AnimationType.HERO_MOVE_RIGHT;
        }else if(b2DComponent.body.getLinearVelocity().x < 0){
            //player moves to the right
            animationComponent.aniType = AnimationType.HERO_MOVE_LEFT;
        }else if(b2DComponent.body.getLinearVelocity().y > 0){
            //player moves to the right
            animationComponent.aniType = AnimationType.HERO_MOVE_UP;
        }else if(b2DComponent.body.getLinearVelocity().y < 0){
            //player moves to the right
            animationComponent.aniType = AnimationType.HERO_MOVE_DOWN;
        }

    }
}
