package com.bytespacegames.config.gui;

import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class CategoryLabelElement extends AbstractGuiElement {
    private String text;
    public CategoryLabelElement(String text, int x, int y, int width, boolean visible) {
        super(x, y, width, 7, visible);
        this.text = text;
    }

    @Override
    public void render(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        g.drawCenteredString(mc.font,text, x + (width/2), y, 0xFFFFFFFF);
        int stringWidth = mc.font.width(text);
        int barWidth = (width - stringWidth) / 2;
        g.fill(x,y + height/3 + 1, x + barWidth - 5, y + height/3 + 2, ConfigGui.SECONDARY_COLOR);
        g.fill(x + barWidth + stringWidth + 5,y + height/3 + 1, x + stringWidth + (barWidth * 2), y + height/3 + 2, ConfigGui.SECONDARY_COLOR);
    }

    @Override
    public void onClick() {

    }
}
