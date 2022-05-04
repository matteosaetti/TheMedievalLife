package com.mygdx.game.input;
import com.badlogic.gdx.Input;

public enum GameKeys {
    UP(Input.Keys.W, Input.Keys.UP),
    DOWN(Input.Keys.S, Input.Keys.DOWN),
    LEFT(Input.Keys.A, Input.Keys.LEFT),
    RIGHT(Input.Keys.D, Input.Keys.RIGHT),
    NEXT(Input.Keys.SPACE, Input.Keys.ENTER),
    INTERACT(Input.Keys.E),
    INVENTORY(Input.Keys.I),
    BACK(Input.Keys.ESCAPE),
    DEBUG(Input.Keys.TAB),
    DEBUG7(Input.Keys.NUMPAD_7),
    DEBUG8(Input.Keys.NUMPAD_8),
    DEBUG9(Input.Keys.NUMPAD_9),
    NUM1(Input.Keys.NUM_1),
    NUM2(Input.Keys.NUM_2),
    NUM3(Input.Keys.NUM_3),
    NUM4(Input.Keys.NUM_4),
    NUM5(Input.Keys.NUM_5),
    NUM6(Input.Keys.NUM_6),
    NUM7(Input.Keys.NUM_7),
    NUM8(Input.Keys.NUM_8);

    final int[] keyCode;

    GameKeys(final int... keyCode){
        this.keyCode = keyCode;
    }

    public int[] getKeyCode(){
        return keyCode;
    }
}