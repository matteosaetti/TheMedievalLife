package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.*;
import com.mygdx.game.World.Entities.NPC.NPC;
import com.mygdx.game.World.Entities.NPC.NPCFactory;
import com.mygdx.game.World.Entities.NPC.NPC_handler;
import com.mygdx.game.World.Entities.Player;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.Item;
import com.mygdx.game.ui.inventory.Inventory;

import java.io.StringWriter;

public class JsonProfile {
    public static void saveProfile(String nameProfile, Player player, Inventory inventory, MapType map){
        Json json = new Json(JsonWriter.OutputType.json);
        StringWriter jsonText = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(jsonText);
        json.setWriter(jsonWriter);


        json.writeObjectStart();
        json.writeValue("coordX", player.B2DBody.getPosition().x);
        json.writeValue("coordY", player.B2DBody.getPosition().y);
        json.writeValue("map", map.toString());
        {
            json.writeObjectStart("stats");
            json.writeValue("health", player.getHealth());
            json.writeValue("maxHealth", player.getMaxHealth());
            json.writeValue("mana", player.getMana());
            json.writeValue("maxMana", player.getMaxMana());
            json.writeValue("exp", player.getExp());
            json.writeValue("maxExp", player.getMaxExp());
            json.writeValue("level", player.getLevel());
            json.writeObjectEnd();
        }
        {
            json.writeObjectStart("inv");
            json.writeValue("gold", 0);
            json.writeValue("items", inventory.getItemsArray(), Array.class);
            json.writeObjectEnd();
        }
        json.writeObjectEnd();

        FileHandle file = Gdx.files.local("data/" + nameProfile + ".json");
        file.writeString(json.prettyPrint(jsonText.toString()), false);
    }

    public static void loadStats(String nameProfile, Player player){
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.local("data/" + nameProfile + ".json"));

        //get the component values
        JsonValue component = base.get("stats");

        //print class value to console
        player.setStats(
                component.getInt("health", 100),
                component.getInt("maxHealth", 100),
                component.getInt("mana", 50),
                component.getInt("maxMana", 100),
                component.getInt("exp", 0),
                component.getInt("maxExp", 100),
                component.getInt("level", 1)
        );
    }


    public static void loadLocation(String nameProfile, Player player, MapManager mapManager){
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.local("data/" + nameProfile + ".json"));

        mapManager.setMap(MapType.getMapTypeByName(base.getString("map")));
        player.teleportTo(new Vector2(base.getFloat("coordX"), base.getFloat("coordY")));
    }

    public static void loadInventory(String nameProfile, Inventory inventory){
        JsonReader jsonReader = new JsonReader();
        Json json = new Json();
        JsonValue base = jsonReader.parse(Gdx.files.local("data/" + nameProfile + ".json"));
        Array<Item> items = new Array<>();

        JsonValue invJson = base.get("inv");
        items = json.fromJson(Array.class, invJson.get("items").toString().substring(6));
        inventory.loadInv(items);
    }

    /**
     * get the NPCs saved in json file
     */
    public static void loadNPCs(NPC_handler npc_handler, MapType mapType, World world, AssetManager assetManager){
        Array<NPC> NPCs = new Array<>();
        String strMap = mapType.name();
        NPCFactory npcFactory = new NPCFactory();

        JsonReader jsonReader = new JsonReader();
        JsonValue JsonBase = jsonReader.parse(Gdx.files.local("data/NPCs_" + strMap + ".json"));

        JsonValue JsonNPC = JsonBase.child();
        while(JsonNPC != null){
            String NPCName = JsonNPC.name();
            float x = JsonNPC.getFloat("x");
            float y =  JsonNPC.getFloat("y");

            NPC npc = npcFactory.createNPC(NPCName, x, y, world, assetManager);
            npc_handler.addNPC(npc);

            JsonNPC = JsonNPC.next();
        }
    }
}

