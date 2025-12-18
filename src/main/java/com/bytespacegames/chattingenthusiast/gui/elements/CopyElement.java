package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.utils.ChatUtil;
import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.ARGB;
import org.lwjgl.glfw.GLFW;

import static com.bytespacegames.chattingenthusiast.gui.GuiUtil.drawRect;

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
        int iconColor = 0xFF7F7F7F;
        if (isHovering(c.getMouseX(),c.getMouseY())) {
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
        Minecraft.getInstance().keyboardHandler.setClipboard(s);
    }
    @Override
    public void onClick() {
        Window window = Minecraft.getInstance().getWindow();
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) (cc);
        // single line
        if (InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_CONTROL)) {
            setClipboard(ChatUtil.getPlainText(cca.getTrimmedMessages().get(messageIndex + cca.getChatScrollbarPos()).content()));
            return;
        }
        GuiMessage message = ChatUtil.getMessageFromLine(cca.getTrimmedMessages().get(messageIndex + cca.getChatScrollbarPos()));
        if (message == null) return;
        setClipboard(message.content().getString());
    }
}
