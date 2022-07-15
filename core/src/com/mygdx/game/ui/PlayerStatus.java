package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

class PlayerStatus extends Table {
    ProgressBar health;
    ProgressBar mana;
    ProgressBar exp;


    public PlayerStatus(Skin skin){
        super(skin);
        setDebug(false);

        //ProgressBars
        health = new ProgressBar(0,1,0.01f, false, skin, "health");
        mana = new ProgressBar(0,1,0.01f, false, skin, "mana");
        exp = new ProgressBar(0,1,0.01f, false, skin, "exp");
        health.setValue(1);
        mana.setValue(1);
        exp.setValue(1);

        //Group bars of ProgressBars
        VerticalGroup bars = new VerticalGroup();
        bars.pad(2,0,2,0);
        bars.space(6);
        bars.addActor(health);
        bars.addActor(exp);
        bars.addActor(mana);


        //PlayerStatus table
        add(new Image(skin.getDrawable("ps_left")));
        add(bars).expandX().fillX();
        add(new Image(skin.getDrawable("ps_right")));
    }

    void setHealth(final float value){
        health.setValue(value);
    }

    void setExp(final float value){
        exp.setValue(value);
    }

    void setMana(final float value){
        mana.setValue(value);
    }

    void setBars(final float healthValue, final float expValue, final float manaValue){
        health.setValue(healthValue);
        exp.setValue(expValue);
        mana.setValue(manaValue);
    }

    float getHealth(){
        return health.getValue();
    }

    float getExp(){
        return exp.getValue();
    }

    float getMana(){
        return mana.getValue();
    }
}
