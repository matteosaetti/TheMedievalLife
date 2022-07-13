package com.mygdx.game.ui;

public enum ActionType {
    CHAT("Parla", "E-Key", "action_chat"),
    PORTAL("Entra", "Space-Key", "action_portal");

    private final String text;
    private final String key;
    private final String action;

    ActionType(String text, String key, String action) {
        this.action=action;
        this.key=key;
        this.text=text;
    }

    public String getText(){
        return text;
    }

    public String getKey(){
        return key;
    }

    public String getAction(){
        return action;
    }
}
