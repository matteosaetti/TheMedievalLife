package com.mygdx.game.ui.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.ui.Item;

public class InventoryStack extends Stack {
    private Item item = null;
    private final Skin skin;
    private String typeOfItemAccepted;

    public InventoryStack(String typeOfSlot, Skin skin){
        this.skin = skin;
        add(new Image(skin.getDrawable(typeOfSlot)));
    }

    public InventoryStack(String typeOfSlot, Skin skin, String typeOfItemAccepted){
        this.skin = skin;
        add(new Image(skin.getDrawable(typeOfSlot)));
        this.typeOfItemAccepted = typeOfItemAccepted;
    }

    public boolean addItem(Item item){
        if(item != null){
            if(typeOfItemAccepted != null && !item.getItemType().equals(typeOfItemAccepted))
                return false;

            if(item.equals(this.item))
                item.setQuantity(item.getQuantity() + this.item.getQuantity());

            Label quantityLabel = new Label(Integer.valueOf(item.getQuantity()).toString(), skin, "small-white");
            quantityLabel.setAlignment(Align.bottomRight);

            removeItem();
            this.add(item.getItemImage(skin));
            this.add(quantityLabel);
        }
        this.item = item;
        return false;
    }

    public void removeItem(){
        if(item != null){
            this.getChild(2).remove();
            this.getChild(1).remove();
            item = null;
        }
    }

    public Item getItem() {
        return item;
    }

    public boolean containsItems(){
        return item != null;
    }

    public boolean isItemAccepted(Item item){
        if(item == null)
            return false;

        if(typeOfItemAccepted == null)
            return true;
        else{
            if(item.getItemType() == null)
                return false;
            else
                return item.getItemType().equals(typeOfItemAccepted);
        }
    }
}
