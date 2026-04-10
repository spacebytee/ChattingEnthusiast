package com.bytespacegames.config.gui;

import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.Minecraft;

public class CategoryLabelElement extends AbstractGuiElement {
    private final String text;
    public CategoryLabelElement(String text, int x, int y, int width, boolean visible) {
        super(x, y, width, 7, visible);
        this.text = text;
    }

    @Override
    public void render(GuiManager gui) {
        Minecraft mc = Minecraft.getInstance();
        gui.drawCenteredString(mc.font,text, x + (width/2), y, 0xFFFFFFFF);
        int stringWidth = mc.font.width(text);
        int barWidth = (width - stringWidth) / 2;
        gui.fill(x,y + height/3 + 1, x + barWidth - 5, y + height/3 + 2, ConfigGui.SECONDARY_COLOR);
        gui.fill(x + barWidth + stringWidth + 5,y + height/3 + 1, x + stringWidth + (barWidth * 2), y + height/3 + 2, ConfigGui.SECONDARY_COLOR);
    }

    @Override
    public void onClick() {

    }
}
