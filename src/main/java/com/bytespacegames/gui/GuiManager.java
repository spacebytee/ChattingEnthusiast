package com.bytespacegames.gui;

import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class GuiManager {
    public static GuiManager INSTANCE;
    private int mouseX, mouseY;
    // when true, apply the inverse of the gui transformations (scale by chat scale, transform 4 pixels) for chat, to offset the desync between hitboxes and the actual render positions
    public boolean mouseTransformations = false;
    public double scale = 1;
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
        return (int) (INSTANCE.mouseX / INSTANCE.scale) - 4;
    }
    public static int getMouseY() {
        if (!INSTANCE.mouseTransformations) return INSTANCE.mouseY;
        return (int) (INSTANCE.mouseY / INSTANCE.scale);
    }

    public void disableScissor(GuiGraphics g) {
        g.disableScissor();
    }
    private int x1,y1,x2,y2;
    public void recoverScissor(GuiGraphics g) {
        g.enableScissor(x1,y1,x2,y2);
    }
    public void enableScissor(GuiGraphics g, int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        g.enableScissor(x1,y1,x2,y2);
    }
}
