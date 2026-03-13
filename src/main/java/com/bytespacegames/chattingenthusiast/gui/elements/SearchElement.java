package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ARGB;
import static com.bytespacegames.gui.GuiUtil.drawRect;

public class SearchElement extends AbstractGuiElement {
    public SearchElement(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, visible);
    }
    @Override
    public void render() {
        GuiManager graphics = GuiManager.INSTANCE;
        float baseBackgroundOpacity = Minecraft.getInstance().options.textBackgroundOpacity().get().floatValue();

        int color = baseButtonColor;
        int iconColor = baseIconColor;
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            color = hoveredButtonColor;
            iconColor = hoveredIconColor;
        }

        graphics.fill(x,y,x+width,y+width, ARGB.color(baseBackgroundOpacity, color));

        drawRect(x+4,y+2,3,1,iconColor);
        drawRect(x+4,y+8,3,1,iconColor);
        drawRect(x+2,y+4,1,3,iconColor);
        drawRect(x+8,y+4,1,3,iconColor);
        for (int rx = 0; rx <= 4; rx+=4) {
            for (int ry = 0; ry <= 4; ry+=4) {
                drawRect(x+3 + rx,y+3 + ry,1,1,iconColor);
            }
        }
        for (int i = 0; i < 3; i++) {
            drawRect(x+8 + i,y+8 + i,1,1,iconColor);
        }
    }
    @Override
    public void onClick() {
        ChattingEnthusiast.chatting().search.setVisible(!ChattingEnthusiast.chatting().search.isVisible());
        ChattingEnthusiast.chatting().search.setFocused(ChattingEnthusiast.chatting().search.isVisible());
    }
}
