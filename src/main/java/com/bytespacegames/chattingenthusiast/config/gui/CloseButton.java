package com.bytespacegames.chattingenthusiast.config.gui;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.gui.elements.RectangleElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class CloseButton extends RectangleElement {
    public CloseButton(int x, int y, boolean visible) {
        super(x, y, 16, 16, 0xFFFF0055, visible);
    }

    public void render(GuiGraphics g) {
        ChattingComponent c = ChattingEnthusiast.chatting();
        if (isHovering(c.getMouseX(),c.getMouseY())) {
            g.fill(x,y,x+width,y+height,0xFFFF568E);
            return;
        }
        super.render(g);
    }

    public void onClick() {
        Minecraft.getInstance().setScreen(null);
    }
}
