package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Stack;

public class Hotbar extends Table {
    private final int NUMBER_OF_SLOT = 8;
    private int currentPointSlot = 0;
    ArrayList<Stack> stacks;
    ArrayList<Cell<Image>> cells;

    public Hotbar(Skin skin){
        super(skin);
        stacks = new ArrayList<>(NUMBER_OF_SLOT);
        cells = new ArrayList<>(NUMBER_OF_SLOT);

        setScale(1.5f);
        setDebug(false);

        add(new Image(skin.getDrawable("hotbarBegin"))).expandX().bottom().right();
        for(int i=0; i<NUMBER_OF_SLOT;i++){
            Stack currentStack = new Stack();
            currentStack.add(new Image(skin.getDrawable("hotbarMid")));
            add((CharSequence) currentStack);
            stacks.add(currentStack);
        }
        add(new Image(skin.getDrawable("hotbarEnd"))).bottom().expandX().left();

        row();
        add();
        for(int i=0; i<NUMBER_OF_SLOT;i++){
            cells.add(this.add(new Image(skin, "hotbarCursor")));
            cells.get(i).getActor().setVisible(false);
        }

        cells.get(currentPointSlot).getActor().setVisible(true);
    }

    void changeSlot(int slotIndex){
        cells.get(currentPointSlot).getActor().setVisible(false);
        currentPointSlot = slotIndex;
        cells.get(currentPointSlot).getActor().setVisible(true);

    }
    public int getCurrentPointSlot(){
        return currentPointSlot;
    }
    public int getNUMBER_OF_SLOT(){
        return NUMBER_OF_SLOT;
    }

}
