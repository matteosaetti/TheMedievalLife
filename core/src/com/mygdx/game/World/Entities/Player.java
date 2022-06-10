package com.mygdx.game.World.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.World.Entities.NPC.NPC;
import com.mygdx.game.World.Entities.animation.NpcAnimation;
import com.mygdx.game.World.Entities.animation.PlayerAnimation;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.utils.JsonProfile;

public class Player extends  Entity{
    private final World world;
    private final float B2D_BODY_DEF_WIDTH = 0.4f;
    private final float B2D_BODY_DEF_HEIGHT = 0.6f;
    private final GameUI gameUI;

    private float maxHealth;
    private float maxMana;
    private float maxExp;

    private float health;
    private float mana;
    private float exp;
    private int level;

    /**
     * Constructor of Player
     *
     * Create the Box2D Body of the player and init the animations of its texture
     *
     * @param world Box2D World where the player will be defined
     * @param coords coordinates of where the player will be spawned
     * */
    public Player(World world, Vector2 coords, GameUI gameUI, AssetManager assetManager){
        //offset to leg = - heightEntireBox + heightLegs / 2
        super(world, coords, 0.4f, 0.15f, -0.6f +0.15f/2);
        this.world = world;
        this.gameUI = gameUI;

        JsonProfile.loadStats("mainProfile", this);
        updateGameUI();
        playerBodyDef();
        setNpcAnimation((NpcAnimation) new PlayerAnimation(this,assetManager));
    }

    void playerBodyDef(){
        PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(B2D_BODY_DEF_WIDTH, B2D_BODY_DEF_HEIGHT);
        fixtureDef.shape = sensorShape;
        fixtureDef.isSensor = true;
        B2DBody.createFixture(fixtureDef).setUserData("player_sensor");

        fixtureDef.isSensor = false;
        sensorShape.dispose();
    }

    private void updateGameUI(){
        gameUI.setBars(health/maxHealth, exp/maxExp, mana/maxMana);
    }

    public void fillHealth(){
        health = maxHealth;
    }

    public void fillMana(){
        mana = maxMana;
    }

    public void levelUp(){
        level += 1;
        maxHealth += 50;
        maxMana += 10;
        maxExp *= 1.5f;
        fillHealth();
        fillMana();
        updateGameUI();
    }

    public void setHealth(float health) {
        if(health >= 0 && health <= maxHealth)
            this.health = health;
        else
            throw new IllegalArgumentException("Tried to set health to \"" + health + "\", but not in range: 0-" + maxHealth);
    }

    public void setMana(float mana) {
        if(mana >= 0 && mana <= maxMana)
            this.mana = mana;
        else
            throw new IllegalArgumentException("Tried to set mana to \"" + mana + "\", but not in range: 0-" + maxMana);
    }

    public void addExp(float exp) {
        if(exp < 0)
            throw new IllegalArgumentException("exp must be a positive value");
        this.exp += exp;

        //recursive call addExp if exp exceed
        if(exp > maxExp){
            float expInExceed = exp-maxExp;
            levelUp();
            addExp(expInExceed);
        }
    }

    public void setStats(float health, float maxHealth, float mana, float maxMana, float exp, float maxExp, int level){
        this.health = health;
        this.maxHealth = maxHealth;
        this.mana = mana;
        this.maxMana = maxMana;
        this.exp = exp;
        this.maxExp = maxExp;
        this.level = level;
    }

    public float getLevel() {
        return level;
    }

    public float getHealth() {
        return health;
    }

    public float getMana() {
        return mana;
    }

    public float getExp() {
        return exp;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getMaxMana() {
        return maxMana;
    }

    public float getMaxExp() {
        return maxExp;
    }

    public void draw(Batch batch, float elapsedTime) {
        if (batch.isDrawing()) {
            batch.draw(getNpcAnimation().getCurrentAnimation().getKeyFrame(elapsedTime, true),
                    B2DBody.getPosition().x - 0.65f,
                    B2DBody.getPosition().y - 0.7f,
                    1.3f, 1.6f);
        }
        else
            Gdx.app.error(this.getClass().getSimpleName(), "batch not drawing");
    }


    @Override
    public void teleportTo(Vector2 coordinates){
        world.destroyBody(B2DBody);
        //offset to leg = - heightEntireBox + heightLegs / 2
        entityDef(coordinates, B2D_BODY_DEF_WIDTH, 0.15f, -B2D_BODY_DEF_HEIGHT +0.15f/2);
        playerBodyDef();
    }

    public float getDistanceFromNPC(NPC npc){

        return B2DBody.getPosition().dst(npc.B2DBody.getPosition());
    }

    public boolean isInConversationRadius(NPC npc){
        return getDistanceFromNPC(npc) < npc.getDialogueRadius();
    }
}

