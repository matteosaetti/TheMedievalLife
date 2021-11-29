package map;

import com.badlogic.gdx.math.Vector2;

import static com.mygdx.game.MyGdxGame.UNIT_SCALE;

public class CollisionArea {
    final float x;
    final float y;
    private final float[] vertices;

   public CollisionArea(final float x, final float y, final float[] vertices){
       this.x = x * UNIT_SCALE;
       this.y = y * UNIT_SCALE;
       this.vertices = vertices;
       for(int i=0; i<vertices.length; i+=2){
           vertices[i] = vertices[i + 1] * UNIT_SCALE;
       }
   }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float[] getVertices() {
        return vertices;
    }
}