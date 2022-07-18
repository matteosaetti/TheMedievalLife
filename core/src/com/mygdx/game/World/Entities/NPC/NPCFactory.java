package com.mygdx.game.World.Entities.NPC;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class NPCFactory {
    public NPC createNPC(String name, float x, float y, World world, AssetManager assetManager){
        NPC npc = null;
        if(name.equals("Merchant_1")){
         npc = new Merchant(world, new Vector2(x, y), assetManager);
        } else if (name.equals("Merchant_2")){
            npc = new Merchant(world, new Vector2(x, y), assetManager);
        } else if (name.equals("King")){
            npc = new King(world, new Vector2(x, y), assetManager);
        } else if (name.equals("Cavalier_1")){
            npc = new Cavalier(world, new Vector2(x, y), assetManager);
        }// else if (name.equals("Cavalier_2")){
           // npc = new Cavalier(world, new Vector2(x, y), assetManager);
        //}
        else if (name.equals("Wizard")) {
            npc = new Wizard(world, new Vector2(x, y), assetManager);
        } else if (name.equals("Queen")){
            npc = new Queen(world, new Vector2(x, y), assetManager);
        }
        else return null;
        return npc;
    }
}
