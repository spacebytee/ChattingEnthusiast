package com.bytespacegames.gui;

import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.client.Minecraft;

public class GuiManager {
    public static GuiManager INSTANCE;
    private int mouseX, mouseY;
    // when true, apply the inverse of the gui transformations (scale by chat scale, transform 4 pixels) for chat, to offset the desync between hitboxes and the actual render positions
    public boolean mouseTransformations = false;
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
        if (!INSTANCE.mouseTransformations) return INSTANCE.mouseX;
        return (int) (INSTANCE.mouseX / ((IChatComponentAccessor) Minecraft.getInstance().gui.getChat()).mixin$getScale()) - 4;
    }
    public static int getMouseY() {
        if (!INSTANCE.mouseTransformations) return INSTANCE.mouseY;
        return (int) (INSTANCE.mouseY / ((IChatComponentAccessor) Minecraft.getInstance().gui.getChat()).mixin$getScale());
    }
}
