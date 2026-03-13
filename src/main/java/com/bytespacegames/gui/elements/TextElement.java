package com.bytespacegames.gui.elements;

import com.bytespacegames.gui.GuiManager;
import net.minecraft.client.Minecraft;

public class TextElement extends AbstractGuiElement {
    private String text;
    private int color;
    public static final int TEXT_HEIGHT = 7;
    public TextElement(String text, int x, int y, int color, boolean visible) {
        super(x, y, 0, TEXT_HEIGHT, visible);
        this.text = text;
        this.color = color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public int getColor() {
        return color;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    @Override
    public void render() {
        GuiManager.INSTANCE.drawString(Minecraft.getInstance().font, text, x,y,color);
    }

    @Override
    public void onClick() {

    }
}
