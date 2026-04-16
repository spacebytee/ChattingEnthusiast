package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
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
    public void render(GuiManager graphics) {
        int color = baseButtonColor;
        int iconColor = baseIconColor;
        boolean hovering = isHovering(GuiManager.getMouseX(), GuiManager.getMouseY());
        if (hovering) {
            color = hoveredButtonColor;
            iconColor = hoveredIconColor;
        }

        if (ChattingSettingsManager.INSTANCE.getSettingToggledById("buttonbackgrounds")) {
            graphics.fill(x,y,x+width,y+width, getButtonFillColor(color));
        }
        int bx = x + 1 + (int) ((width-9)/2f);
        int by = y + 1 + (int) ((height-9)/2f);
        drawRect(bx,by + 2,1,1,iconColor);
        drawRect(bx + 1,by + 3,6,1,iconColor);
        drawRect(bx + 4,by + 1,1,5,iconColor);
        drawRect(bx + 5,by + 2,1,3,iconColor);
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
