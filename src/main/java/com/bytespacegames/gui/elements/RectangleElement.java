package com.bytespacegames.gui.elements;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public class RectangleElement extends AbstractGuiElement {
    private int color;
    public RectangleElement(int x, int y, int width, int height, int color, boolean visible) {
        super(x, y, width, height, visible);
        this.color = color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public int getColor() {
        return color;
    }
    @Override
    public void render(GuiGraphics g) {
        g.fill(x,y,x+width,y+height,color);
    }

    @Override
    public void onClick() {

    }
}
