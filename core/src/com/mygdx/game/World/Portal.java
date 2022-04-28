package com.mygdx.game.World;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.map.MapType;
public class Portal {

    private static final  float UNIT_SCALE = 1/32f ;
    private BodyDef bodyDef;
    private Body body;
    private Fixture fixture;
    private Rectangle rect;
    private String destinationStr;
    private MapType destionationMapType;

    public Portal(World world, TiledMap map, MapObject object){
        bodyDef = new BodyDef();
        rect = ((RectangleMapObject) object).getRectangle();

        bodyDef.gravityScale = 0;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rect.getX() + rect.getWidth() / 2 )* UNIT_SCALE,
                (rect.getY() + rect.getHeight()/2 )*UNIT_SCALE);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth()/2*UNIT_SCALE, rect.getHeight()/2*UNIT_SCALE);
        fixture = body.createFixture(shape, 0f);
        body.setUserData("mapObject");

        shape.dispose();

        fixture.setUserData(this);
        destinationStr = object.getName();
        destionationMapType = MapType.getMapTypeByName(destinationStr);

    }

    public MapType getDestionationMapType(){
        return  destionationMapType;
    }

}
