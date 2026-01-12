package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.chattingenthusiast.gui.containers.BasicContainer;
import com.bytespacegames.chattingenthusiast.gui.containers.BottomRightOriginatingContainer;
import com.bytespacegames.chattingenthusiast.gui.elements.ClearChatElement;
import com.bytespacegames.chattingenthusiast.gui.elements.CopyElement;
import com.bytespacegames.chattingenthusiast.gui.elements.DeleteElement;
import com.bytespacegames.chattingenthusiast.gui.elements.WidgetElement;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.chattingenthusiast.mixin.IChatScreenAccessor;
import com.bytespacegames.chattingenthusiast.utils.CharacterUtils;
import com.bytespacegames.chattingenthusiast.utils.TimerUtils;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.List;

public class ChattingComponent {
    private int mouseX,mouseY;
    private final Minecraft mc;
    private double chatOffset = 0;
    public int desiredScrollbarPos;
    public boolean ignoreScroll = false;
    // when true, apply the inverse of the gui transformations (scale by chat scale, transform 4 pixels) for chat, to offset the desync between hitboxes and the actual render positions
    private boolean mouseTransformations = false;

    private final TimerUtils scrollTimer = new TimerUtils();
    private final TimerUtils animationTimer = new TimerUtils();

    public BasicContainer lineContainer;
    public BasicContainer chatContainer;
    public BottomRightOriginatingContainer chatControls;

    CopyElement copy;
    DeleteElement delete;
    public WidgetElement search;

    public List<GuiMessage.Line> getEffectiveLines() {
        if (ChattingEnthusiast.filter().unfiltered()) {
            IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
            return cca.getTrimmedMessages();
        }
        return ChattingEnthusiast.filter().getEffectiveLines();
    }

    public ChattingComponent() {
        this.mc = Minecraft.getInstance();
        lineContainer = new BasicContainer(0,0,true);
        chatContainer = new BasicContainer(0,0,true);
        copy = new CopyElement(0,0,0,0);
        delete = new DeleteElement(0,0,0,0);
        lineContainer.addElement(copy);
        lineContainer.addElement(delete);
        chatControls = new BottomRightOriginatingContainer(0,0,1,true);
        chatControls.addElement(new ClearChatElement(0,0,13,13, true));
        chatContainer.addElement(chatControls);
    }
    public void renderCustomLine(GuiGraphics graphics, int x, int mx, int nx, int lineIndex, float opacity) {
        if (animationTimer.hasTimeElapsed(ChattingEnthusiast.ANIMATION_INTERVAL, true)) chatOffset /= 1.3;
        IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
        int scaleOffset = Mth.ceil(cca.mixin$getWidth() / mc.options.chatScale().get());
        float baseBackgroundOpacity = mc.options.textBackgroundOpacity().get().floatValue();
        // lineIndex is relative to the bottom chat message as scrolled, so we need to account for scroll position
        int hoveredIndex = ChattingEnthusiast.INSTANCE.getHoveredMessage(mouseX,mouseY) - cca.getChatScrollbarPos();
        int color = lineIndex == hoveredIndex ? 0xFFFFFFFF : 0xFF000000;
        graphics.fill(x - 4, mx, x + scaleOffset + 4 + 4, nx, ARGB.color(opacity * baseBackgroundOpacity, color));
        if (lineIndex != hoveredIndex) return;
        copy.setConfig(x + scaleOffset + 8 + 1, mx, cca.mixin$getLineHeight(), cca.mixin$getLineHeight(), true);
        copy.setMessage(lineIndex);
        delete.setConfig(x + scaleOffset + 8 + 2 + cca.mixin$getLineHeight(), mx, cca.mixin$getLineHeight(), cca.mixin$getLineHeight(), true);
        delete.setMessage(lineIndex);
        //graphics.fill(x + scaleOffset + 4 + 5, mx, x + scaleOffset + 4 + 5 + cca.mixin$getLineHeight(), mx + cca.mixin$getLineHeight(), ARGB.color(opacity * baseBackgroundOpacity, 0xFF000000));
        //graphics.fill(x + scaleOffset + 4 + 6 + cca.mixin$getLineHeight(), mx, x + scaleOffset + 4 + 6 + cca.mixin$getLineHeight() * 2, mx + cca.mixin$getLineHeight(), ARGB.color(opacity * baseBackgroundOpacity, 0xFF000000));

        int hovered = ChattingEnthusiast.INSTANCE.getHoveredMessage(mouseX,mouseY);
        if (hovered == -1) {
            copy.setVisible(false);
            delete.setVisible(false);
        }
        mouseTransformations = true;
        lineContainer.render(graphics);
        mouseTransformations = false;
    }
    public void updateMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }
    public int getMouseX() {
        if (!mouseTransformations) return mouseX;
        return (int) (mouseX / ((IChatComponentAccessor)mc.gui.getChat()).mixin$getScale()) - 4;
    }
    public int getMouseY() {
        if (!mouseTransformations) return mouseY;
        return (int) (mouseY / ((IChatComponentAccessor)mc.gui.getChat()).mixin$getScale());
    }

    // these methods come from ChatScreen, and therefore only are called when chat is focused
    public void render(GuiGraphics g) {
        if (search == null) {
            search = new WidgetElement(0,0, new EditBox(mc.font,-1,-1,172,14, Component.literal("Search...")), false);
            chatContainer.addElement(search);
        }
        chatControls.setRelativePosition(mc.getWindow().getGuiScaledWidth() - 3, mc.getWindow().getGuiScaledHeight() - 16);
        // 15 meaning the chat text bar height of 12 + 2 padding from the bottom of the screen + 1 padding from the top of the search bar.
        // yes im using a magic number here because the original code does that too.
        search.setRelativePosition(mc.getWindow().getGuiScaledWidth() - 230, mc.getWindow().getGuiScaledHeight() - 15 - search.getHeight());
        chatContainer.render(g);
        if (!scrollTimer.hasTimeElapsed(ChattingEnthusiast.ANIMATION_INTERVAL, true)) return;
        IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
        int scrollDelta = Math.min(ChattingEnthusiast.SCROLLING_INTERVAL,Math.max(-ChattingEnthusiast.SCROLLING_INTERVAL, desiredScrollbarPos - cca.getChatScrollbarPos()));
        ignoreScroll = true;
        mc.gui.getChat().scrollChat(scrollDelta);
    }

    public void onClick() {
        mouseTransformations = true;
        lineContainer.onClick();
        mouseTransformations = false;

        chatContainer.onClick();
    }

    public void keyPressed(KeyEvent keyEvent) {
        lineContainer.keyPressed(keyEvent);
        chatContainer.keyPressed(keyEvent);

        System.out.println(keyEvent.modifiers());
        CharacterEvent ce = new CharacterEvent(CharacterUtils.scancodeToCodepoint(keyEvent.scancode(), keyEvent.modifiers() > 0), keyEvent.modifiers());
        charTyped(ce);
    }
    public void charTyped(CharacterEvent characterEvent) {
        lineContainer.charTyped(characterEvent);
        chatContainer.charTyped(characterEvent);
    }
    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {
        lineContainer.mouseDragged(mouseButtonEvent, d, e);
        chatContainer.mouseDragged(mouseButtonEvent, d, e);
    }

    public void tick() {
        if (search == null) return;
        if (!(mc.screen instanceof ChatScreen)) return;
        IChatScreenAccessor screen = (IChatScreenAccessor) mc.screen;
        if (search.getWidget().isFocused()) {
            screen.getInput().setCanLoseFocus(true);
            screen.getInput().setFocused(false);
        } else {
            screen.getInput().setCanLoseFocus(false);
            screen.getInput().setFocused(true);
        }
    }
    public double getChatOffset() {
        return chatOffset;
    }
    public void setChatOffset(double offset) {
        this.chatOffset = offset;
    }

    public enum TabFilter {
        NONE,PARTY,GUILD,PM
    }
}
