package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.chattingenthusiast.utils.ChatUtil;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void render() {
        GuiManager graphics = GuiManager.INSTANCE;
        float baseBackgroundOpacity = Minecraft.getInstance().options.textBackgroundOpacity().get().floatValue();
        
        int color = 0xFF000000;
        int iconColor = 0xFF7F7F7F;
        boolean hovering = isHovering(GuiManager.getMouseX(), GuiManager.getMouseY());
        if (hovering) {
            color = 0xFFFFFFFF;
            iconColor = 0xFFFFFFFF;
        }
        graphics.fill(x,y,x+width,y+width, ARGB.color(baseBackgroundOpacity, color));
        int gx = x + 1 + (int) ((width-9)/2f);
        int gy = y + 1 + (int) ((height-9)/2f);
        drawRect(gx,gy,5,1,iconColor);
        drawRect(gx,gy,1,5,iconColor);
        drawRect(gx,gy + 4,2,1,iconColor);
        drawRect( gx + 4,gy,1,2,iconColor);
        gx = x + 3 + (int) ((width-9)/2f);
        gy = y + 3 + (int) ((height-9)/2f);
        drawRect(gx,gy,5,1,iconColor);
        drawRect(gx,gy,1,5,iconColor);
        drawRect(gx,gy + 4,5,1,iconColor);
        drawRect(gx + 4,gy,1,5,iconColor);

        if (hovering && ChattingSettingsManager.INSTANCE.getSettingToggledById("tooltip")) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("chattingenthusiast.tooltip.title"));
            tooltip.add(Component.translatable("chattingenthusiast.tooltip.copy"));
            tooltip.add(Component.translatable("chattingenthusiast.tooltip.ctrl"));
            if (FabricLoader.getInstance().isModLoaded("chatshot")) tooltip.add(Component.translatable("chattingenthusiast.tooltip.shift"));
            graphics.getGuiGraphics().setTooltipForNextFrame(Minecraft.getInstance().font,tooltip,Optional.empty(),GuiManager.getMouseX(),GuiManager.getMouseY());
        }
    }
    public void setClipboard(String s) {
        Minecraft.getInstance().keyboardHandler.setClipboard(s.replaceAll("§.",""));
    }
    @Override
    public void onClick() {
        Window window = Minecraft.getInstance().getWindow();
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) (cc);
        // image
        if (InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) && FabricLoader.getInstance().isModLoaded("chatshot")) {
            GuiMessage.Line line = ChattingEnthusiast.chatting().getEffectiveLines().get(messageIndex + cca.getChatScrollbarPos());
            //ChatCopyUtil.copyImage(ChatUtil.getLinesFromMessage(ChatUtil.getMessageFromLine(line)),Minecraft.getInstance());
            return;
        }
        // single line
        if (InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_CONTROL)) {
            setClipboard(ChatUtil.cleanUpMessage(ChatUtil.getPlainText(ChattingEnthusiast.chatting().getEffectiveLines().get(messageIndex + cca.getChatScrollbarPos()).content())));
            return;
        }
        GuiMessage message = ChatUtil.getMessageFromLine(ChattingEnthusiast.chatting().getEffectiveLines().get(messageIndex + cca.getChatScrollbarPos()));
        if (message == null) return;
        setClipboard(ChatUtil.cleanUpMessage(message.content().getString().replaceAll("￼","")));
    }
}
