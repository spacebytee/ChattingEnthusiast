package com.bytespacegames.gui;

public class GuiUtil {
    public static void drawRect(int x, int y, int width, int height, int color) {
        GuiManager.INSTANCE.fill(x, y, x + width, y + height, color);
    }
}
