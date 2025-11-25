package com.bytespacegames.chattingenthusiast.gui;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.ARGB;

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
        int color = 0xFF000000;
        if (isHovering(c.getMouseX(),c.getMouseY())) {
            color = 0xFFFFFFFF;
        }
        graphics.fill(x,y,x+width,y+width, ARGB.color(baseBackgroundOpacity, color));
    }

    @Override
    public void onClick() {
        System.out.println("clicky click");
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
