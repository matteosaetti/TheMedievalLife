package com.mygdx.game.World.Entities.NPC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.World.Entities.Player;
import com.mygdx.game.World.Entities.animation.OnlyIdleAnimation;
import com.mygdx.game.ui.GameUI;

public class Merchant extends NPC{
    public Merchant(World world, Vector2 coords, AssetManager assetManager){
        super(world,0.5f,0.55f,coords);

        setNPCname("Merchant");
        setConversationConfigPath("NPC/merchant/dialog.json");
        setNpcAnimation(new OnlyIdleAnimation( assetManager,"NPC/Merchant/idle.atlas"));
    }

    @Override
    public void actionTriggered(Player player) {
        //if in dialogue range, load conversation
        if(player.isInConversationRadius(this) && !GameUI.getInstance().getDialogue().isVisible()){
            GameUI.getInstance().getDialogue().loadConversation(this,null);
        }
        B2DBody.applyLinearImpulse(
                -B2DBody.getLinearVelocity().x,
                -B2DBody.getLinearVelocity().y,
                B2DBody.getWorldCenter().x,B2DBody.getWorldCenter().y,
                true);
    }

    @Override
    public void draw(Batch batch, float elapsedTime) {
        if(batch.isDrawing()){
            batch.draw(getNpcAnimation().getCurrentAnimation().getKeyFrame(elapsedTime,true),
                    B2DBody.getPosition().x -0.5f,
                    B2DBody.getPosition().y -0.55f,
                    1f,1.4f);
        }
        else{
            Gdx.app.error(this.getClass().getSimpleName(),"batch if not drawing");
        }
    }
}
