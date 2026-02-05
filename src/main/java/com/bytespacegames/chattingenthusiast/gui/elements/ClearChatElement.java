package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import static com.bytespacegames.gui.GuiUtil.drawRect;

public class ClearChatElement extends AbstractGuiElement {
    public ClearChatElement(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, visible);
    }
    @Override
    public void render(GuiGraphics graphics) {
        float baseBackgroundOpacity = Minecraft.getInstance().options.textBackgroundOpacity().get().floatValue();

        int color = baseButtonColor;
        int iconColor = baseIconColor;
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            color = hoveredButtonColor;
            iconColor = hoveredIconColor;
        }

        graphics.fill(x,y,x+width,y+width, ARGB.color(baseBackgroundOpacity, color));

        drawRect(graphics,x+4,y+2,5,1,iconColor);
        drawRect(graphics,x+2,y+3,9,1,iconColor);
        for (int i = 0; i < 4; i++) {
            drawRect(graphics,x+3 + 2*i,y+4,1,7,iconColor);
        }
        drawRect(graphics,x+3,y+10,7,1,iconColor);
    }
    long lastClicked = 0;
    @Override
    public void onClick() {
        if (System.currentTimeMillis() - lastClicked > 3000) {
            Minecraft.getInstance().gui.getChat().addMessage(Component.translatable("chattingenthusiast.clear"));
            lastClicked = System.currentTimeMillis();
        } else {
            Minecraft.getInstance().gui.getChat().clearMessages(false);
            ChattingEnthusiast.filter().clear();
        }
    }
}
