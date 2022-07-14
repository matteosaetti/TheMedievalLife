package com.mygdx.game.ui.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.Item;
import com.mygdx.game.World.Entities.Player;

public class Inventory extends Table {
    private final int ROWS = 3;
    private final int COLUMNS = 5;
    private final int NO_BOTTOM_SLOTS = 4;
    private final int HELMET_SLOT_INDEX = ROWS*COLUMNS;
    private final int ARMOR_SLOT_INDEX = ROWS*COLUMNS+1;
    private final int PANTS_SLOT_INDEX = ROWS*COLUMNS+2;
    private final int BOOTS_SLOT_INDEX = ROWS*COLUMNS+3;
    private final int WEAPON_SLOT_INDEX = ROWS*COLUMNS+4;
    private final Array<InventorySlot> inventory;
    private int gold=0;

    private final DragAndDrop dragAndDrop;

    public Inventory(Skin skin, Player player, MyGdxGame context){
        super(skin);
        setVisible(false);

        dragAndDrop = new DragAndDrop();
        inventory = new Array<>(COLUMNS*ROWS+NO_BOTTOM_SLOTS);
        final Stack title;
        final Table inventorySlotsTable;
        final Table inventoryBottomSlotsTable;

        // Title Setup
        title = new Stack();
        title.add(new Image(skin.getDrawable("inventoryTop")));
        Label titleLabel = new Label("INVENTARIO", skin, "small-white");
        titleLabel.setAlignment(1);
        titleLabel.setColor(1,1,1,1);
        titleLabel.setFontScale(1.5f);
        title.add(titleLabel);

        // Main inventory slots setup
        inventorySlotsTable = new Table(skin);
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLUMNS; c++){
                String name = "inventory";
                if(r == 0)
                    name+="_top";
                else if(r == ROWS-1)
                    name+="_bottom";
                else
                    name+="_mid";

                if(c == 0)
                    name+="left";
                else if(c == COLUMNS-1)
                    name+="right";
                else
                    name+="mid";


                //add(new Image(skin.getDrawable(typeOfSlot)));
                InventorySlot inventorySlot = new InventorySlot(name, skin);

                inventorySlotsTable.add(inventorySlot);
                inventory.add(inventorySlot);
                dragAndDrop.addSource(new SlotSource(inventorySlot));
                dragAndDrop.addTarget(new SlotTarget(inventorySlot));

            }
            inventorySlotsTable.row();
        }

        // Accessories slots setup
        inventoryBottomSlotsTable = new Table(skin);
        String[] namesBottomSlots = {"inventory_helmet", "inventory_armor","inventory_pants", "inventory_boots", "inventory_weapon"};

        for(String bottomSlotName : namesBottomSlots){
            InventorySlot inventorySlot = new InventorySlot(bottomSlotName, skin, bottomSlotName.split("_")[1]);
            inventoryBottomSlotsTable.add(inventorySlot);
            inventory.add(inventorySlot);
            dragAndDrop.addSource(new SlotSource(inventorySlot));
            dragAndDrop.addTarget(new SlotTarget(inventorySlot));
        }

        // Complete inventory setup
        add(title);
        row();
        add(inventorySlotsTable);
        row();
        add(inventoryBottomSlotsTable);

        addItemToInventoryAtIndex(new Item("helmet_01", 46, 1, "helmet"), HELMET_SLOT_INDEX);
    }


    public void open(){
        setVisible(true);
    }

    public void close(){
        setVisible(false);
    }

    /**
     * @return true if the InventoryUI is open
     */
    public boolean isOpened(){
        return isVisible();
    }

    /**
     * Print for debug purposes of inventory
     */
    public void printInventory(){
        int index = 0;
        System.out.println("Inventory\n{");
        for(InventorySlot slot : inventory){
            System.out.print("\t" + index + ": ");
            if(slot.containsItems())
                System.out.println("["+slot.getItem()+"]; ");
            else
                System.out.println("[];");

            index++;
        }
        System.out.println("}\n");
    }

    /**
     * @param item item to be found
     * @return index of the InventorySlot containing that item
     */
    private int indexOf(Item item){
        int index = 0;

        for(InventorySlot slot : inventory){
            if(slot.containsItems() && slot.getItem().equals(item))
                return index;
            index++;
        }
        return -1;
    }


    /**
     * Add item to the inventory, in the slot containing that item, if possible, or in the first one found
     * @param item item to be added
     * @return new quantity of the item
     */
    public int addItemToInventory(Item item){
        //if there's a slot containing that item, add the item there
        if(indexOf(item) != -1){
            InventorySlot slot = inventory.get(indexOf(item));
            slot.addItem(item);
            return slot.getItem().getQuantity();
        }

        //if there's not, add the the first empty slot
        for(InventorySlot slot : inventory){
            if(!slot.containsItems()){
                slot.addItem(item);
                return item.getQuantity();
            }
        }
        return 0;
    }

    /**
     * Add an item to a specified slot
     *
     * @param item item to be added
     * @param index index where item will be put
     * @return the new quantity of the Item
     */
    public int addItemToInventoryAtIndex(Item item, int index){
        inventory.get(index).addItem(item);
        return item.getQuantity();
    }

    public Array<Item> getItemsArray(){
        Array<Item> array = new Array<>();
        int index = 0;

        for(InventorySlot slot : inventory){
            if(slot.containsItems()){
                Item currSavingItem = slot.getItem().copy();
                currSavingItem.setIndexInInv(index);
                array.add(currSavingItem);
            }
            index++;
        }

        return array;
    }



    /**
     * get array of Items, clear inventory and put them into it
     */
    public void loadInv(Array<Item> items){
        clearInventory();
        for(Item item : items){
            inventory.get(item.getIndexInInv()).addItem(item);
        }
    }

    /**
     * remove items of the Inventory' slots
     */
    private void clearInventory(){
        for(InventorySlot slot : inventory){
            slot.removeItem();
        }
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int goldAmount) {
        gold += goldAmount;
    }


    /**
     * SlotSource is an InventorySlot Source for the DragAndDrop of our inventory.
     *
     * Extends DragAndDrop.Source and define dragStart()
     */
    class SlotSource extends DragAndDrop.Source{
        public SlotSource (InventorySlot inventorySlot){
            super(inventorySlot);
        }

        /**
         * If a slot is dragged and contains some Item, sets the payload object as the InventorySlot of the source
         * and set the dragActor as a copy of the Image representing the Item
         *
         * @return the payload setted
         */
        @Override
        public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
            DragAndDrop.Payload payload = new DragAndDrop.Payload();
            InventorySlot slotSource = (InventorySlot) getActor();

            if(slotSource.containsItems()){
                payload.setObject(slotSource);
                payload.setDragActor(slotSource.getItem().getItemImage(getSkin()));
                return payload;
            }
            else
                return null;
        }
    }

}


/**
 * SlotTarget is an InventorySlot Target for the DragAndDrop of our inventory.
 *
 * Extends DragAndDrop.Target and define drag() and drop() in our case
 */
class SlotTarget extends DragAndDrop.Target{
    public SlotTarget (InventorySlot inventorySlot){
        super(inventorySlot);
    }

    /**
     * if the target currently pointed is valid for drop()
     *
     * discard following cases:
     * -The slot pointed is the source slot
     * -The slot pointed accepts specific ItemType and does not match with the payload's Item
     *
     * @param source dragAndDrop source
     * @param payload payload of current dragged slot (source)
     * @param x x of pointer
     * @param y y of pointer
     * @param pointer pointer
     * @return true if the target currently pointed is valid for drop()
     */
    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        InventorySlot draggedSlot = (InventorySlot) payload.getObject();
        InventorySlot targetSlot = (InventorySlot) getActor();

        return !draggedSlot.equals(targetSlot) && targetSlot.isItemAccepted(draggedSlot.getItem());
    }

    /**
     * Drop the Item of Payload/Source to the new ItemSlot(SlotTarget).
     * The Item is droppable because already checked in drag()
     *
     * If target slot is empty or contains the same Item: move the Item from source(add to target and remove from source)
     * else: swap the Items between source and target
     */
    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        InventorySlot sourceSlot = (InventorySlot) source.getActor();
        InventorySlot targetSlot = (InventorySlot) getActor();
        if (!targetSlot.containsItems() || targetSlot.getItem().equals(sourceSlot.getItem())){
            targetSlot.addItem(sourceSlot.getItem());
            sourceSlot.removeItem();
        }
        else{
            Item tempSwap = new Item(sourceSlot.getItem().getName(), sourceSlot.getItem().getID(), sourceSlot.getItem().getQuantity());
            sourceSlot.addItem(targetSlot.getItem());
            targetSlot.addItem(tempSwap);
        }

    }
}
