package com.mygdx.game.World;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.MyGdxGame.UNIT_SCALE;

public class WallAndBox {
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Body body;
    private Rectangle rect;

    public WallAndBox(World world, TiledMap map, MapObject object){
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        rect = ((RectangleMapObject) object).getRectangle();

        bodyDef.gravityScale=0;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rect.getX() + rect.getWidth() / 2)* UNIT_SCALE,
                (rect.getY() + rect.getHeight() / 2)*UNIT_SCALE);
        body = world.createBody(bodyDef);

        //debug
        System.out.println("body created: " + bodyDef.position.x + ";" + bodyDef.position.y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth()/2*UNIT_SCALE, rect.getHeight()/2*UNIT_SCALE);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData("mapObject");

        shape.dispose();
    }
}

