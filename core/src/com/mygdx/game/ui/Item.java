package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Item {
    private final String name;
    private final int ID;
    private int quantity;
    private String type;
    private int indexInInv;

    public Item(String name, int ID){
        this.name = name;
        this.ID = ID;
        this.quantity = 1;
        this.type = null;
    }

    public Item(String name, int ID, int quantity){
        this.name = name;
        this.ID = ID;
        this.quantity = quantity;
    }

    public Item(String name, int ID, int quantity, String type){
        this.name = name;
        this.ID = ID;
        this.quantity = quantity;
        this.type = type;
    }

    public Item(){
        this.name = null;
        this.ID = -1;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getItemType(){
        return type;
    }

    public int getIndexInInv() {
        return indexInInv;
    }

    public Image getItemImage(Skin skin) {
        return new Image(skin.getDrawable("item_" + ID));
    }

    public void incrementQuantity(){
        quantity++;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setIndexInInv(int indexInInv) {
        this.indexInInv = indexInInv;
    }

    /**
     * @return a new Item with the same attributes of this Instance
     */
    public Item copy(){
        return new Item(name, ID, quantity, type);
    }

    @Override
    public String toString(){
        return "item " + ID + " (" + name + "): " + quantity;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Item){
            return ((Item) obj).ID == this.ID;
        }
        return false;
    }
}

