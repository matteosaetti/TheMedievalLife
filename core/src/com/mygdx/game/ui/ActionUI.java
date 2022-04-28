package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class ActionUI extends Table {
    private Skin skin;
    private Array<ActionType> actions;
    private Array<Cell<actionUI>> cells;
    public Object interactingObject;

    public ActionUI(Skin skin){
        this.skin = skin;
        this.setDebug(false);
        actions = new Array<>(ActionType.values());
        cells = new Array<>(ActionType.values().length);

        for (ActionType actionType : ActionType.values()){
            Cell<actionUI> cell = add(new actionUI(skin, actionType));
            cells.add(cell);
            cell.getActor().setVisible(false);
            row();
        }
    }
    public void showAction(ActionType actionType){
        cells.get(actions.indexOf(actionType, true)).getActor().setVisible(true);
    }

    public  void hideAction(ActionType actionType){
        cells.get(actions.indexOf(actionType, true)).getActor().setVisible(false);
    }

    public boolean isActionPossible(ActionType actionType){
        return cells.get(actions.indexOf(actionType, true)).getActor().isVisible();
    }
}
class actionUI extends Table{
    actionUI(Skin skin, ActionType actionType){
        add(new Image(skin.getDrawable(actionType.getKey())));
        add(new Label(actionType.getText(), skin, "debug")).expandX().padLeft(5);
        row();
        add(new Image((skin.getDrawable(actionType.getAction())))).colspan(2);
    }
}

