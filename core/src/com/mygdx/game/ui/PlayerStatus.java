package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

class PlayerStatus extends Table {
    ProgressBar health;
    ProgressBar mana;
    ProgressBar exp;

    public PlayerStatus(Skin skin){
        super(skin);
        setDebug(false);

        //Progress Bar
        health=new ProgressBar(0,1,0.1f,false,skin,"health");
        mana=new ProgressBar(0,1,0.1f,false,skin,"mana");
        exp=new ProgressBar(0,1,0.1f,false,skin,"exp");
        health.setValue(1);
        mana.setValue(1);
        exp.setValue(1);

        VerticalGroup bars = new VerticalGroup();
        bars.pad(2,0,2,0);
        bars.space(6);
        bars.addActor(health);
        bars.addActor(mana);
        bars.addActor(exp);

        add(new Image(skin.getDrawable("payerStatusLeft")));
        add((bars).expand().fill());
        add(new Image(skin.getDrawable("playerStatusRight")));

    }

    public void setHealth(final float value) {
        health.setValue(value);
    }

    public void setExp(final float value) {
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
