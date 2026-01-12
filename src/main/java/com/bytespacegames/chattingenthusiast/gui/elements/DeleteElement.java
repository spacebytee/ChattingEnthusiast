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
import net.minecraft.util.ARGB;

import java.util.List;

import static com.bytespacegames.chattingenthusiast.gui.GuiUtil.drawRect;

public class DeleteElement extends AbstractGuiElement {
    public DeleteElement(int x, int y, int width, int height) {
        super(x, y, width, height, false);
    }
    int messageIndex;
    public void setMessage(int index) {
        messageIndex = index;
    }
    @Override
    public void render(GuiGraphics graphics) {
        ChattingComponent c = ChattingEnthusiast.chatting();
        float baseBackgroundOpacity = Minecraft.getInstance().options.textBackgroundOpacity().get().floatValue();

        int color = baseButtonColor;
        int iconColor = baseIconColor;
        if (isHovering(c.getMouseX(),c.getMouseY())) {
            color = hoveredButtonColor;
            iconColor = hoveredIconColor;
        }

        graphics.fill(x,y,x+width,y+width, ARGB.color(baseBackgroundOpacity, color));

        drawRect(graphics,x+3,y+1,3,1,iconColor);
        drawRect(graphics,x+1,y+2,7,1,iconColor);
        for (int i = 0; i < 3; i++) {
            drawRect(graphics,x+2 + 2*i,y+3,1,4,iconColor);
        }
        drawRect(graphics,x+2,y+7,5,1,iconColor);
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

    public void keyPressed(KeyEvent keyEvent) {

    }

    public void charTyped(CharacterEvent characterEvent) {

    }

    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {

    }
    public void clickOff() {

    }
}
