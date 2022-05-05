package com.mygdx.game.World.Entities.NPC;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.World.Entities.Entity;
import com.mygdx.game.World.Entities.Player;

public abstract class NPC extends Entity {

    private Vector2 coords;
    private float actionRadius = 10;
    private float dialogueRadius=1.5f;
    //float NPCspeed = NOMINAL_SPEED;
    private String mapLocation;
    private String NPCname = "NPC";
    private String conversationConfigPath;

    /**
     * Constructor of NPC
     * <p>
     * Create the Box2D Body of the npc and init the animations of its texture
     *
     * @param world  Box2D World where the NPC will be defined
     * @param coords coordinates of where the NPC will be spawned
     */
    public NPC(World world, float hWidth, float hHeight, Vector2 coords) {
        super(world, coords, hWidth, hHeight);

        B2DBody.setUserData("NPC");
        this.coords=coords;
    }


    /**
     * getter of actionRadius
     *
     * @return the radius in which the npc is aware of the player
     */
    public float getActionRadius() {
        return actionRadius;
    }

    /**
     * setter of actionRadius
     */
    public void setActionRadius(float actionRadius) {
        this.actionRadius = actionRadius;
    }

    public float getDialogueRadius(){
        return dialogueRadius;
    }

    public String getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(String mapLocation) {
        this.mapLocation = mapLocation;
    }

    public Vector2 getCoords() {
        return coords;
    }

    public String getNPCname() {
        return NPCname;
    }

    public void setNPCname(String NPCname) {
        this.NPCname = NPCname;
    }

    public String getConversationConfigPath() {
        return conversationConfigPath;
    }

    public void setConversationConfigPath(String conversationConfigPath) {
        this.conversationConfigPath = conversationConfigPath;
    }

    public abstract void actionTriggered(Player player);

    public abstract void draw(Batch batch, float elapsedTime);

    /**
     * The NPC_handler will notify the NPC when the player enters in his action radius by this method
     *
     * This NPC just follows the player
     *
     * @param player player whom the npc will interact
     */
}

