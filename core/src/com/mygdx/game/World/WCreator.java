package com.mygdx.game.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import javax.swing.border.Border;

import static com.badlogic.gdx.utils.JsonValue.ValueType.object;

public class WCreator {
    private final World world;
    private Array<Body> bodies;

    public WCreator(World world, TiledMap map) {
        this.world = world;
        bodies = new Array<>();

        for(MapObject object : map.getLayers().get("Solids").getObjects().getByType(RectangleMapObject.class)){
            new WallAndBox(world, map, object);
        }

        for(PolygonMapObject object : map.getLayers().get("solids").getObjects().getByType(PolygonMapObject.class)){
            new Wborder(world, map, object);
        }

        for(MapObject object : map.getLayers().get("portals").getObjects().getByType(RectangleMapObject.class)){
            new Portal(world, map, object);
        }
    }

    public void bodyDestroyer(){
       world.getBodies(bodies);
       int deleted = 0;
       int max = world.getBodyCount();
       for(Body body : bodies){
           if(body.getUserData().equals("mapObject") || body.getUserData().equals("NPC")){
                world.destroyBody(body);
                deleted++;
           }
       }
        Gdx.app.debug(this.getClass().getSimpleName(), deleted + " out of " + max + "; remained " + world.getBodyCount());
    }
}
