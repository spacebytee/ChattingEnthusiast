package com.bytespacegames.chattingenthusiast.gui;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import net.minecraft.client.gui.GuiGraphics;

public abstract class AbstractGuiElement {
    protected int x;
    protected int y;
    protected boolean visible;
    protected int width;
    protected int height;
    public AbstractGuiElement(int x, int y, int width, int height, boolean visible) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean isVisible() {
        return visible;
    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setConfig(int x, int y, int width, int height, boolean visible) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = visible;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        int rOX =  + ChattingEnthusiast.chatting.renderOffsetX;
        int rOY =  + ChattingEnthusiast.chatting.renderOffsetY;
        return mouseX >= x + rOX && mouseX < x + rOX + width && mouseY >= y + rOY && mouseY <= y + rOY + height;
    }
    public abstract void render(GuiGraphics g);
    public abstract void onClick();

    public void handleClick(int mouseX, int mouseY) {
        if (!isVisible()) return;
        if (!isHovering(mouseX,mouseY)) return;
        onClick();
    }
}
