package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ecs.ECSEngine;
import com.mygdx.game.ecs.component.AnimationComponent;

public class AnimationSystem extends IteratingSystem {
    public AnimationSystem(MyGdxGame context) {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
        if(animationComponent.aniType!=null){
            animationComponent.aniTime += deltaTime;
        }
    }
}
