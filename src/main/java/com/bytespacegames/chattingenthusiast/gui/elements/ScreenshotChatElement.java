package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

import static com.bytespacegames.gui.GuiUtil.drawRect;

public class ScreenshotChatElement extends AbstractGuiElement {
    public ScreenshotChatElement(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, visible);
    }
    @Override
   public void render(GuiManager graphics) {
        GuiManager graphics = GuiManager.INSTANCE;
        int color = baseButtonColor;
        int iconColor = baseIconColor;
        boolean hovering = isHovering(GuiManager.getMouseX(), GuiManager.getMouseY());
        if (hovering) {
            color = hoveredButtonColor;
            iconColor = hoveredIconColor;
        }
        int rX = x + (width - 7)/2;
        int rY = y + (height - 7)/2;
        if (ChattingSettingsManager.INSTANCE.getSettingToggledById("buttonbackgrounds")) {
            graphics.fill(x,y,x+width,y+width, getButtonFillColor(hovering, color));
        }

        drawRect(rX,rY,2,1,iconColor);
        drawRect(rX,rY,1,2,iconColor);
        drawRect(rX + 5,rY,2,1,iconColor);
        drawRect(rX + 6,rY,1,2,iconColor);
        drawRect(rX,rY + 6,2,1,iconColor);
        drawRect(rX,rY + 5,1,2,iconColor);
        drawRect(rX + 5,rY + 6,3,1,iconColor);
        drawRect(rX + 6,rY + 5,1,3,iconColor);
    }
    @Override
    public void onClick() {
        ArrayList<GuiMessage.Line> lines = new ArrayList<>();
        List<GuiMessage.Line> effectiveLines = ChattingEnthusiast.filter().getEffectiveLines();
        for (int i = ((IChatComponentAccessor)Minecraft.getInstance().gui.getChat()).getChatScrollbarPos();
             i < effectiveLines.size() &&
                     i < Minecraft.getInstance().gui.getChat().getLinesPerPage() + ((IChatComponentAccessor)Minecraft.getInstance().gui.getChat()).getChatScrollbarPos();
             i++) {
            lines.addFirst(effectiveLines.get(i));
        }
        //ChatCopyUtil.copyImage(lines, Minecraft.getInstance());
    }
}
