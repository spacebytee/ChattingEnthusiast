package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.util.ARGB;

import static com.bytespacegames.gui.GuiUtil.drawRect;

public class JumpElement extends AbstractGuiElement {
    public JumpElement(int x, int y, int width, int height) {
        super(x, y, width, height, false);
    }
    int messageIndex;
    public void setMessage(int index) {
        messageIndex = index;
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

        int bx = x + 1;
        int by = y + 1;
        drawRect(graphics,bx,by + 2,1,1,iconColor);
        drawRect(graphics,bx + 1,by + 3,6,1,iconColor);
        drawRect(graphics,bx + 4,by + 1,1,5,iconColor);
        drawRect(graphics,bx + 5,by + 2,1,3,iconColor);
    }

    @Override
    public void onClick() {
        GuiMessage.Line line = ChattingEnthusiast.filter().getEffectiveLines().get(messageIndex);
        ((EditBox)ChattingEnthusiast.chatting().search.getWidget()).setValue("");
        ChattingEnthusiast.filter().clear();
        ChattingEnthusiast.chatting().ignoreScroll = true;
        Minecraft.getInstance().gui.getChat().scrollChat(ChattingEnthusiast.filter().getEffectiveLines().indexOf(line));
        ChattingEnthusiast.chatting().ignoreScroll = false;
    }
}
