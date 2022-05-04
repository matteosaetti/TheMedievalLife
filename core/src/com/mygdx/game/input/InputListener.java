package com.mygdx.game.input;

public interface InputListener {
    void keyPressed(final InputManager manager, final GameKeys key);

    void keyUp(final InputManager manager, final GameKeys key);

    void scroll(final InputManager manager, final float amount);
}
