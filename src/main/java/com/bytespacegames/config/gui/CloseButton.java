package com.bytespacegames.config.gui;

import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.RectangleElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class CloseButton extends RectangleElement {
    public CloseButton(int x, int y, boolean visible) {
        super(x, y, 16, 16, 0xFFFF0055, visible);
    }

    public void render(GuiGraphics g) {
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            g.fill(x,y,x+width,y+height,0xFFFF568E);
            return;
        }
        super.render(g);
    }

    public void onClick() {
        if (Minecraft.getInstance().screen == null) return;
        Minecraft.getInstance().screen.onClose();
    }
}
