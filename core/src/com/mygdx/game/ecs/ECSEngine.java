package com.mygdx.game.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ecs.component.B2DComponent;
import com.mygdx.game.ecs.component.PlayerComponent;
import com.mygdx.game.ecs.system.PlayerMovementSystem;

import static com.mygdx.game.MyGdxGame.BIT_GROUND;
import static com.mygdx.game.MyGdxGame.BIT_PLAYER;

public class ECSEngine extends PooledEngine {
    public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<B2DComponent> b2dCmpMapper = ComponentMapper.getFor(B2DComponent.class);

    private final World world;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    public ECSEngine(final MyGdxGame context){
        super();

        world = context.getWorld();
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        this.addSystem(new PlayerMovementSystem(context));
    }


    private void resetBodyAndFixtureDefinition(){
        bodyDef.position.set(0,0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = false;

        fixtureDef.density = 0;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.shape = null;
    }
        //create a PLAYER
    public void createPlayer(final Vector2 playerSpawnLocation, final float width, final float height){
        final Entity player = this.createEntity();

        //player component
        final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed.set(3,3);
        player.add(playerComponent);

        //box2d component
        resetBodyAndFixtureDefinition();

        final B2DComponent b2DComponent = this.createComponent(B2DComponent.class);

        bodyDef.position.set(playerSpawnLocation.x, playerSpawnLocation.y + height * 0.5f);
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2DComponent.body = world.createBody(bodyDef);
        b2DComponent.body.setUserData("PLAYER");
        b2DComponent.width = width;
        b2DComponent.height = height;

        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(width * 0.5f, height * 0.5f);
        fixtureDef.shape = pShape;
        b2DComponent.body.createFixture(fixtureDef);
        pShape.dispose();

        player.add(b2DComponent);
        this.addEntity(player);
    }
}
