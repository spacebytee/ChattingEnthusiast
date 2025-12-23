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

public class ClearChatElement extends AbstractGuiElement {
    public ClearChatElement(int x, int y, int width, int height, boolean visible) {
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
            Minecraft.getInstance().gui.getChat().addMessage(Component.literal("§c§lClick again to clear the chat!"));
            lastClicked = System.currentTimeMillis();
        } else {
            Minecraft.getInstance().gui.getChat().clearMessages(false);
            ChattingEnthusiast.filter.clear();
        }
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
