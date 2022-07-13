package com.mygdx.game.World.npcs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.World.Entities.NPC.NPC;
import com.mygdx.game.World.Entities.Player;

public class Walker extends NPC {
    public Walker(World world, float hWidth, float hHeight, Vector2 coords){
        super(world,hWidth,hHeight,coords);

    }

    @Override
    public void actionTriggered(Player player){

    }
    @Override
    public void draw(Batch batch, float elapsedTime){
        if (batch.isDrawing()){
            batch.draw(getNpcAnimation().getCurrentAnimation().getKeyFrame(elapsedTime, true),
                    B2DBody.getPosition().x -0.3f,
                    B2DBody.getPosition().y -0.3f,
                    1f,1.2f);
        }
    }
}
