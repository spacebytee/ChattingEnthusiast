package com.bytespacegames.chattingenthusiast.gui;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyElement extends AbstractGuiElement {
    public CopyElement(int x, int y, int width, int height, boolean visible) {
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
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) (cc);
    }

    @Override
    public void onClick() {
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) (cc);

        Minecraft.getInstance().keyboardHandler.setClipboard(getPlainText(cca.getTrimmedMessages().get(messageIndex + cca.getChatScrollbarPos()).content()));
    }

    public static String getPlainText(FormattedCharSequence seq) {
        StringBuilder sb = new StringBuilder();

        seq.accept((index, style, codePoint) -> {
            sb.appendCodePoint(codePoint);
            return true;
        });

        return sb.toString();
    }
}
