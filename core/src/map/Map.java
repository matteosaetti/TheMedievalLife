package map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import javax.print.attribute.standard.RequestingUserName;

public class Map {
    public static final String TAG = Map.class.getSimpleName();
    private final TiledMap tiledMap;
    private final Array<CollisionArea> collisionAreas;


    public Map(final TiledMap tiledMap){
        this.tiledMap = tiledMap;
        collisionAreas = new Array<CollisionArea>();


        parseCollisionLayer();

    }

    private void parseCollisionLayer() {
       final MapLayer collisionLayer = tiledMap.getLayers().get("collision");
       if(collisionLayer==null){
           Gdx.app.debug(TAG, "there is no collision Layer");
           return;
       }
        final MapObjects mapObjects = collisionLayer.getObjects();
        if(mapObjects==null){
            Gdx.app.debug(TAG, "there are no collision mapobjects defined");
        }
       for( MapObject mapObj : mapObjects){
            if(mapObj instanceof RectangleMapObject){
                final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObj;
                final Rectangle rectangle = rectangleMapObject.getRectangle();
                final float[] rectVertices = new float[10];

                //left bot
                rectVertices[0] = 0;
                rectVertices[1] = 0;

                //left top
                rectVertices[2] = 0;
                rectVertices[3] = rectangle.height;
                //right top
                rectVertices[4] = rectangle.width;
                rectVertices[5] = rectangle.height;
                //right bottom
                rectVertices[6] = rectangle.width;
                rectVertices[7] = 0;
                //left bottom
                rectVertices[8] = 0;
                rectVertices[9] = 0;

                collisionAreas.add(new CollisionArea(rectangle.x, rectangle.y, rectVertices));
            }
            else if(mapObj instanceof PolygonMapObject){
                final PolylineMapObject polylineMapObject = (PolylineMapObject) mapObj;
                final Polyline polyline= polylineMapObject.getPolyline();
                collisionAreas.add(new CollisionArea(polyline.getX(), polyline.getY(), polyline.getVertices()));
            }
            else{
                Gdx.app.debug(TAG, "MapObject of type " + mapObj + " is not supported for collision layer!");
            }
        }
    }

    public Array<CollisionArea> getCollisionAreas() {
        return collisionAreas;
    }
}
