package com.bytespacegames.chattingenthusiast.gui;

import net.minecraft.client.gui.GuiGraphics;

public class GuiUtil {
    public static void drawRect(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + height, color);
    }
}
