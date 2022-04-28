package com.mygdx.game.World;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.MyGdxGame.UNIT_SCALE;

public class Wborder {
    private BodyDef mapBorders;
    private Body borders;
    private FixtureDef fixtureDef;
    private float[] vertices;


    public void WBorder(World world, TiledMap map, PolygonMapObject polygonMapObject) {
        mapBorders = new BodyDef();
        fixtureDef = new FixtureDef();

        Polygon polygon = polygonMapObject.getPolygon();
        mapBorders.gravityScale = 0;
        mapBorders.type = BodyDef.BodyType.StaticBody;
        mapBorders.position.set(polygon.getX() * UNIT_SCALE, polygon.getY() * UNIT_SCALE);
        borders = world.createBody(mapBorders);

        ChainShape chainShape = new ChainShape();

        vertices = polygon.getVertices().clone();
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = UNIT_SCALE * vertices[i];
        }

        chainShape.createLoop(vertices);
        fixtureDef.shape = chainShape;
        borders.createFixture(fixtureDef).setUserData(this);
        borders.setUserData("mapObject");

        chainShape.dispose();
    }
}