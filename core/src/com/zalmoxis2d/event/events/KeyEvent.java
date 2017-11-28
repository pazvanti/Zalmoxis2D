package com.zalmoxis2d.event.events;

public class KeyEvent extends Event {
    public static final String KEY_DOWN = "keyDown";
    public static final String KEY_UP = "keyUp";

    private int keyCode;

    public KeyEvent(int keyCode, String type) {
        super(type);
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}
