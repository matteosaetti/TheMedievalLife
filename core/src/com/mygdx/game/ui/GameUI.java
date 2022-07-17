package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.World.Portal;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.screen.ScreenType;
import com.mygdx.game.ui.inventory.Inventory;
import com.mygdx.game.World.Entities.Player;


public class GameUI extends Table implements InputListener {
    private static GameUI uniqueInstance;
    final private PlayerStatus playerStatus;
    final private Hotbar hotbar;
    final private Inventory inventory;
    final private Dialogue dialogue;
    final ActionPossibleUI actionPossible;

    /**
     * Singleton constructor of the uniqueInstance of GameUI
     */
    public static GameUI getInstance(final MyGdxGame context, final Skin skin, Player player){
        uniqueInstance = new GameUI(context, skin, player);
        return uniqueInstance;
    }

    /**
     * We use Singleton pattern, this is the getter of the uniqueInstance of GameUI.
     * Use this only if sure that it has been already initialized.
     */
    public static GameUI getInstance() {
        if (uniqueInstance == null) {
            Gdx.app.error(GameUI.class.getSimpleName(), "Tried to access to Singleton class GameUI, but not initialised yet (use getInstance(Gamestarter ..., ...)");
        }
        return uniqueInstance;
    }

    private GameUI(final MyGdxGame context, final Skin skin, Player player) {
        super(skin);

        //table properties
        setFillParent(true);
        pad(20);
        setDebug(false);

        //widgets 1st row
        Button menu = new Button(skin.getDrawable("menuIconInactive"), skin.getDrawable("menuIconActive"));
        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                context.setScreen(ScreenType.MAINMENU);
            }
        });
        Button inventoryButt =new Button(skin.getDrawable("inventoryIconInactive"),skin.getDrawable("inventoryIconActive"));
        //widget 2nd row
        inventory = new Inventory(skin, player, context);
        inventoryButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(inventory.isOpened())
                    inventory.close();
                else
                    inventory.open();
            }
        });

        dialogue=new Dialogue(skin, "PROVA DI DIALOGO PIU LUNGA PER VEDERE COME SI COMPORTANO LE RIGHE", "TIZIO");
        actionPossible =new ActionPossibleUI(skin);


        //widgets 3rd row
        playerStatus = new PlayerStatus(skin);
        hotbar = new Hotbar(skin);

        //first row
        add(menu).top().left();
        add().expandX().top().right();
        add(playerStatus).top();
        playerStatus.pack();
        row();

        //second row
        add(inventory).expandY().center();
        add(dialogue).bottom().left().size(600, 150).padBottom(25);
        add(actionPossible).bottom().right();
        row();

        //third row
        add(inventoryButt).bottom().left().size( 80,70);
        add(hotbar).colspan(2).bottom().padRight(playerStatus.getWidth());
    }

    public void setHealth(final float value){
        playerStatus.setHealth(value);
    }

    public void setExp(final float value){
        playerStatus.setExp(value);
    }

    public void setMana(final float value){
        playerStatus.setMana(value);
    }

    public void setBars(final float healthValue, final float expValue, final float manaValue){
        playerStatus.setBars(healthValue, expValue, manaValue);
    }

    private void changeSlotHotbar(int slotIndex){
        hotbar.changeSlot(slotIndex);
    }

    private void nextSlotHotbar() {
        if(hotbar.getCurrentPointedSlot() != hotbar.getNUMBER_OF_SLOTS() - 1){
            hotbar.changeSlot(hotbar.getCurrentPointedSlot() + 1);
        }
        else{
            hotbar.changeSlot(0);
        }
    }
    private void previousSlotHotbar() {
        if(hotbar.getCurrentPointedSlot() != 0){
            hotbar.changeSlot(hotbar.getCurrentPointedSlot() - 1);
        }
        else{
            hotbar.changeSlot(hotbar.getNUMBER_OF_SLOTS() - 1);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ActionPossibleUI getActionPossible(){
        return actionPossible;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        switch(key){
            case NUM1:
                changeSlotHotbar(0);
                break;
            case NUM2:
                changeSlotHotbar(1);
                break;
            case NUM3:
                changeSlotHotbar(2);
                break;
            case NUM4:
                changeSlotHotbar(3);
                break;
            case NUM5:
                changeSlotHotbar(4);
                break;
            case NUM6:
                changeSlotHotbar(5);
                break;
            case NUM7:
                changeSlotHotbar(6);
                break;
            case NUM8:
                changeSlotHotbar(7);
                break;

            case INVENTORY:
                if(inventory.isOpened())
                    inventory.close();
                else
                    inventory.open();
                break;

            case INTERACT:
                if(actionPossible.isActionPossible(ActionType.CHAT)){
                    dialogue.setVisible(!dialogue.isVisible());
                    break;
                }
                if(actionPossible.isActionPossible(ActionType.PORTAL)){

                }

            case BACK:
                if (inventory.isOpened()){
                    inventory.close();
                    break;
                }
                else if (dialogue.isVisible()){
                    dialogue.setVisible(false);
                }
                break;
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    @Override
    public void scroll(InputManager manager, float amount) {
        if(amount > 0){
            for(int i=0; i<amount; i++){
                nextSlotHotbar();
            }
        }
        else{
            amount *= -1;
            for(int i=0; i<amount; i++){
                previousSlotHotbar();
            }
        }
    }
}
