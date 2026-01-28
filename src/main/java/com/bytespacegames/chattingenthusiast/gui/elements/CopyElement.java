package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.chattingenthusiast.utils.ChatUtil;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.ARGB;
import org.lwjgl.glfw.GLFW;

import static com.bytespacegames.gui.GuiUtil.drawRect;

public class CopyElement extends AbstractGuiElement {
    public CopyElement(int x, int y, int width, int height) {
        super(x, y, width, height, false);
    }
    int messageIndex;
    public void setMessage(int index) {
        messageIndex = index;
    }

    @Override
    public void render(GuiGraphics graphics) {
        float baseBackgroundOpacity = Minecraft.getInstance().options.textBackgroundOpacity().get().floatValue();
        int color = 0xFF000000;
        int iconColor = 0xFF7F7F7F;
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            color = 0xFFFFFFFF;
            iconColor = 0xFFFFFFFF;
        }
        graphics.fill(x,y,x+width,y+width, ARGB.color(baseBackgroundOpacity, color));
        int gx = x + 1;
        int gy = y + 1;
        drawRect(graphics, gx,gy,5,1,iconColor);
        drawRect(graphics, gx,gy,1,5,iconColor);
        drawRect(graphics, gx,gy + 4,2,1,iconColor);
        drawRect(graphics, gx + 4,gy,1,2,iconColor);
        gx = x + 3;
        gy = y + 3;
        drawRect(graphics, gx,gy,5,1,iconColor);
        drawRect(graphics, gx,gy,1,5,iconColor);
        drawRect(graphics, gx,gy + 4,5,1,iconColor);
        drawRect(graphics, gx + 4,gy,1,5,iconColor);
    }
    public void setClipboard(String s) {
        Minecraft.getInstance().keyboardHandler.setClipboard(s.replaceAll("§.",""));
    }
    @Override
    public void onClick() {
        Window window = Minecraft.getInstance().getWindow();
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) (cc);
        // single line
        if (InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_CONTROL)) {
            setClipboard(ChatUtil.getPlainText(ChattingEnthusiast.chatting().getEffectiveLines().get(messageIndex + cca.getChatScrollbarPos()).content()));
            return;
        }
        GuiMessage message = ChatUtil.getMessageFromLine(ChattingEnthusiast.chatting().getEffectiveLines().get(messageIndex + cca.getChatScrollbarPos()));
        if (message == null) return;
        setClipboard(message.content().getString());
    }
}
