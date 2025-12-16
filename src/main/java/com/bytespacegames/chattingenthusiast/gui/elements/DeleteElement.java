package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.ARGB;

import static com.bytespacegames.chattingenthusiast.gui.GuiUtil.drawRect;

public class DeleteElement extends AbstractGuiElement {
    public DeleteElement(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, false);
    }
    int messageIndex;
    public void setMessage(int index) {
        messageIndex = index;
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
        cca.getTrimmedMessages().remove(messageIndex + cca.getChatScrollbarPos());

        /*GuiMessage.Line attemptedDelete = cca.getTrimmedMessages().get(messageIndex + cca.getChatScrollbarPos());
        for (GuiMessage msg : cca.getAllMessages()) {
            if (msg.addedTime() != attemptedDelete.addedTime())  {
                System.out.println(msg.addedTime() + ", " + attemptedDelete.addedTime());
                continue;
            }
            if (msg.tag() != attemptedDelete.tag()) {
                System.out.println(msg.tag() + ", " + attemptedDelete.tag());
                continue;
            }
            System.out.println("attempt delete");
            cc.deleteMessage(msg.signature());
        }*/
    }
}
