package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;

public enum ScreenType {
    GAME(com.mygdx.game.screen.GameScreen.class),
    LOADING(LoadingScreen.class),
    SETTINGS(SettingsScreen.class),
    MAINMENU(MainmenuScreen.class);

    private final Class<? extends AbstractScreen> screenClass;

    ScreenType(final Class<? extends AbstractScreen> screenClass){

        this.screenClass = screenClass;
    }
    public Class<? extends Screen> getScreenClass() {

        return screenClass;
    }
}
