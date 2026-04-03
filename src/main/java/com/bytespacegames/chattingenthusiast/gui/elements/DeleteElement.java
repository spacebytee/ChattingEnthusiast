package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import java.util.List;

import static com.bytespacegames.gui.GuiUtil.drawRect;

public class DeleteElement extends AbstractGuiElement {
    public DeleteElement(int x, int y, int width, int height) {
        super(x, y, width, height, false);
    }
    int messageIndex;
    public void setMessage(int index) {
        messageIndex = index;
    }
    @Override
    public void render() {
        GuiManager graphics = GuiManager.INSTANCE;
        int color = 0xFFFFFFFF;
        int iconColor = 0xFFFFFFFF;
        boolean hovering = isHovering(GuiManager.getMouseX(), GuiManager.getMouseY());
        if (hovering) {
            color = 0xFFFFFF00;
            iconColor = 0xFFFFFF00;
        }

        if (ChattingSettingsManager.INSTANCE.getSettingToggledById("buttonbackgrounds")) {
            graphics.fill(x,y,x+width,y+width, getButtonFillColor(hovering, color));
        }
        int gX = x + (int) ((width-9)/2f);
        int gY = y + (int) ((height-9)/2f);
        drawRect(gX+3,gY+1,3,1,iconColor);
        drawRect(gX+1,gY+2,7,1,iconColor);
        for (int i = 0; i < 3; i++) {
            drawRect(gX+2 + 2*i,gY+3,1,4,iconColor);
        }
        drawRect(gX+2,gY+7,5,1,iconColor);
    }

    @Override
    public void onClick() {
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) (cc);
        List<GuiMessage.Line> effLines = ChattingEnthusiast.chatting().getEffectiveLines();
        List<GuiMessage.Line> trimmedLines = cca.getTrimmedMessages();

        GuiMessage.Line line = effLines.get(messageIndex + cca.getChatScrollbarPos());
        effLines.remove(messageIndex + cca.getChatScrollbarPos());
        if (effLines == trimmedLines) return;
        trimmedLines.remove(line);
    }
}
