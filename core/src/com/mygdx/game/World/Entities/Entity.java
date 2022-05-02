package com.mygdx.game.World.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Entity {
    World world;
    public Body B2DBody;
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    private float bodyHalfWidth;
    private float bodyHalfHeight;
    private Fixture fixture;

    //speed
    public final float NOMINAL_SPEED = 1.3f;
    private float speedX;
    private float speedY;

    public Entity(World world, Vector2 coords, float hWidth, float hHeight, float offset){
        this.world=world;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        this.bodyHalfWidth = hWidth;
        this.bodyHalfHeight = hHeight;

        //Box2d B2DBody
        entityDef(coords, hWidth, hHeight, offset);
    }
    public Entity(World world, Vector2 coords, float hWidth, float hHeight){
        this.world=world;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        this.bodyHalfWidth = hWidth;
        this.bodyHalfHeight = hHeight;

        //Box2d B2DBody
        entityDef(coords, hWidth, hHeight, 0);
    }



    /**
     * Init Box2D Body of entity (BodyDef and FixtureDef related)
     *
     * @param coords coordinates of bodyDef.position
     * */
    public void entityDef(Vector2 coords, float hWidth, float hHeight, float offsetY) {

        bodyDef.position.set(coords.x, coords.y);
        bodyDef.gravityScale=0;
        bodyDef.type= BodyDef.BodyType.DynamicBody;

        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(hWidth,hHeight,new Vector2(0,offsetY),0);
        fixtureDef.shape = playerShape;

        B2DBody = world.createBody(bodyDef);
        fixture = B2DBody.createFixture(fixtureDef);
        fixture.setUserData(this);

        playerShape.dispose();
    }


    /**
     * Getters and setters of SpeedX and SpeedY
     * */
    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setFixtureUserData(Object userData){
        fixture.setUserData(userData);
    }

    /**
     * getter of npcAnimation
     */
    /*public NpcAnimation getNpcAnimation() {
        return npcAnimation;
    }
    */
    /**
     * setter of npcAnimation
     */
   // public void setNpcAnimation(NpcAnimation npcAnimation) {
    //    this.npcAnimation = npcAnimation;
    // }

    public void teleportTo(Vector2 coordinates){
        world.destroyBody(B2DBody);
        entityDef(coordinates, bodyHalfWidth, bodyHalfHeight, 0);
    }
}

