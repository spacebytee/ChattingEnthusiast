package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import java.util.List;

import static com.bytespacegames.chattingenthusiast.gui.GuiUtil.drawRect;

public class SearchElement extends AbstractGuiElement {
    public SearchElement(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, visible);
    }
    @Override
    public void render(GuiGraphics graphics) {
        ChattingComponent c = ChattingEnthusiast.chatting;
        float baseBackgroundOpacity = Minecraft.getInstance().options.textBackgroundOpacity().get().floatValue();

        int color = baseButtonColor;
        int iconColor = baseIconColor;
        if (isHovering(c.getMouseX(),c.getMouseY())) {
            color = hoveredButtonColor;
            iconColor = hoveredIconColor;
        }

        graphics.fill(x,y,x+width,y+width, ARGB.color(baseBackgroundOpacity, color));

        drawRect(graphics,x+4,y+2,3,1,iconColor);
        drawRect(graphics,x+4,y+8,3,1,iconColor);
        drawRect(graphics,x+2,y+4,1,3,iconColor);
        drawRect(graphics,x+8,y+4,1,3,iconColor);
        for (int rx = 0; rx <= 4; rx+=4) {
            for (int ry = 0; ry <= 4; ry+=4) {
                drawRect(graphics,x+3 + rx,y+3 + ry,1,1,iconColor);
            }
        }
        for (int i = 0; i < 3; i++) {
            drawRect(graphics,x+8 + i,y+8 + i,1,1,iconColor);
        }
    }
    long lastClicked = 0;
    @Override
    public void onClick() {
        ChattingEnthusiast.chatting.search.visible = !ChattingEnthusiast.chatting.search.visible;
        ChattingEnthusiast.chatting.search.getWidget().setFocused(ChattingEnthusiast.chatting.search.visible);
    }

    public void keyPressed(KeyEvent keyEvent) {

    }

    public void charTyped(CharacterEvent characterEvent) {

    }

    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {

    }
    public void clickOff() {

    }
}
