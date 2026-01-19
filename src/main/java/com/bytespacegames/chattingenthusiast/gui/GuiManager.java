package com.bytespacegames.chattingenthusiast.gui;

public class GuiManager {
    public static GuiManager INSTANCE;
    private int mouseX, mouseY;
    public GuiManager() {
        INSTANCE = this;
        mouseX = 0;
        mouseY = 0;
    }
    public void setMouseX(int mX) {
        this.mouseX = mX;
    }
    public void setMouseY(int mY) {
        this.mouseY = mY;
    }
    public static int getMouseX() {
        return INSTANCE.mouseX;
    }
    public static int getMouseY() {
        return INSTANCE.mouseY;
    }
}
