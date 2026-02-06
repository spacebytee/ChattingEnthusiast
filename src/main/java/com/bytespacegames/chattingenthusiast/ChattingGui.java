package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.containers.BasicContainer;
import com.bytespacegames.gui.containers.BottomLeftOriginatingContainer;
import com.bytespacegames.gui.containers.BottomRightOriginatingContainer;
import com.bytespacegames.chattingenthusiast.gui.elements.*;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.chattingenthusiast.mixin.IChatScreenAccessor;
import com.bytespacegames.chattingenthusiast.utils.CharacterUtils;
import com.bytespacegames.chattingenthusiast.utils.TimerUtils;
import com.bytespacegames.gui.elements.WidgetElement;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.List;

public class ChattingGui {
    //public List<GuiMessage.Line> cq;
    private int mouseX,mouseY;
    private final Minecraft mc;
    private double chatOffset = 0;
    public int desiredScrollbarPos;
    public boolean ignoreScroll = false;

    private final TimerUtils scrollTimer = new TimerUtils();
    private final TimerUtils animationTimer = new TimerUtils();

    public BasicContainer lineContainer;
    public BasicContainer chatContainer;
    public BottomRightOriginatingContainer chatControls;
    public BottomLeftOriginatingContainer chatTabs;

    private final CopyElement copy;
    private final DeleteElement delete;
    private final JumpElement jump;
    public WidgetElement search;

    public List<GuiMessage.Line> getEffectiveLines() {
        if (ChattingEnthusiast.filter().unfiltered()) {
            IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
            return cca.getTrimmedMessages();
        }
        return ChattingEnthusiast.filter().getEffectiveLines();
    }

    public ChattingGui() {
        this.mc = Minecraft.getInstance();
        lineContainer = new BasicContainer(0,0,true);
        chatContainer = new BasicContainer(0,0,true);

        copy = new CopyElement(0,0,0,0);
        delete = new DeleteElement(0,0,0,0);
        jump = new JumpElement(0,0,0,0);
        lineContainer.addElement(copy);
        lineContainer.addElement(delete);
        lineContainer.addElement(jump);

        chatControls = new BottomRightOriginatingContainer(0,0,1,true);
        chatControls.addElement(new SearchElement(0,0,13,13, true));
        chatControls.addElement(new ClearChatElement(0,0,13,13, true));

        chatTabs = new BottomLeftOriginatingContainer(0,0,2,false);
        chatTabs.addElement(new TabElement("ALL", ChatFilter.TabFilter.NONE, 0,0,true));
        chatTabs.addElement(new TabElement("PARTY", ChatFilter.TabFilter.PARTY, 0,0,true));
        chatTabs.addElement(new TabElement("GUILD", ChatFilter.TabFilter.GUILD, 0,0,true));
        chatTabs.addElement(new TabElement("PM", ChatFilter.TabFilter.PM, 0,0,true));

        chatContainer.addElement(chatControls);
        chatContainer.addElement(chatTabs);
    }
    public void renderCustomLine(GuiGraphics graphics, int x, int mx, int nx, int lineIndex, float opacity) {
        if (animationTimer.hasTimeElapsed(GuiManager.ANIMATION_INTERVAL, true)) chatOffset /= 1.3;
        IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
        int scaleOffset = Mth.ceil(cca.mixin$getWidth() / mc.options.chatScale().get());
        float baseBackgroundOpacity = mc.options.textBackgroundOpacity().get().floatValue();
        // lineIndex is relative to the bottom chat message as scrolled, so we need to account for scroll position
        int hoveredIndex = ChattingEnthusiast.INSTANCE.getHoveredMessage(mouseX,mouseY) - cca.getChatScrollbarPos();

        // draw message background
        if (baseBackgroundOpacity > 0) {
            int color = lineIndex == hoveredIndex && ChattingSettingsManager.INSTANCE.getSettingToggledById("messagehover") ? ChattingSettingsManager.INSTANCE.getColorById("hovercolor") : ChattingSettingsManager.INSTANCE.getColorById("backgroundcolor");
            graphics.fill(x - 4, mx, x + scaleOffset + 4 + 4, nx, ARGB.color(opacity * baseBackgroundOpacity, color));
        }

        if (lineIndex != hoveredIndex) return;
        lineContainer.setVisible(ChattingSettingsManager.INSTANCE.getSettingToggledById("linecontrols"));
        copy.setConfig(x + scaleOffset + 8 + 1, mx, cca.mixin$getLineHeight(), cca.mixin$getLineHeight(), true);
        copy.setMessage(lineIndex);
        delete.setConfig(x + scaleOffset + 8 + 2 + cca.mixin$getLineHeight(), mx, cca.mixin$getLineHeight(), cca.mixin$getLineHeight(), true);
        delete.setMessage(lineIndex);
        jump.setConfig(x + scaleOffset + 8 + 3 + (cca.mixin$getLineHeight() * 2), mx, cca.mixin$getLineHeight(), cca.mixin$getLineHeight(), !ChattingEnthusiast.filter().unfiltered());
        jump.setMessage(lineIndex);

        int hovered = ChattingEnthusiast.INSTANCE.getHoveredMessage(mouseX,mouseY);
        if (hovered == -1) {
            copy.setVisible(false);
            delete.setVisible(false);
        }
        // render lineContainer (per-line controls) with the scaling/offset of the chat line
        GuiManager.INSTANCE.mouseTransformations = true;
        GuiManager.INSTANCE.scale = ((IChatComponentAccessor) Minecraft.getInstance().gui.getChat()).mixin$getScale();
        lineContainer.render(graphics);
        GuiManager.INSTANCE.mouseTransformations = false;
    }
    public void updateMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
        GuiManager.INSTANCE.setMouseX(x);
        GuiManager.INSTANCE.setMouseY(y);
    }
    public void setupCustomElements() {
        if (search == null) {
            search = new WidgetElement(0,0, new EditBox(mc.font,-1,-1,172,14, Component.literal("Search...")), false);
            chatContainer.addElement(search);
        }
        // 15 meaning the chat text bar height of 12 + 2 padding from the bottom of the screen + 1 padding from the top of the search bar.
        // yes im using a magic number here because the original code does that too
        search.setRelativePosition(mc.getWindow().getGuiScaledWidth() - 230, mc.getWindow().getGuiScaledHeight() - 15 - search.getHeight());
        search.setVisible(search.isVisible() && ChattingSettingsManager.INSTANCE.getSettingToggledById("chatcontrols"));
        chatControls.setVisible(ChattingSettingsManager.INSTANCE.getSettingToggledById("chatcontrols"));
        chatControls.setRelativePosition(mc.getWindow().getGuiScaledWidth() - 3, mc.getWindow().getGuiScaledHeight() - 16);
        chatTabs.setRelativePosition(2,mc.getWindow().getGuiScaledHeight() - 15);
        chatTabs.setVisible(ChattingSettingsManager.INSTANCE.getSettingToggledById("chattabs") && ChattingEnthusiast.connectedToHypixel());
    }

    // these methods come from ChatScreen, and therefore only are called when chat is focused
    public void render(GuiGraphics g) {
        setupCustomElements();
        chatContainer.render(g);
        /*if (cq != null) {
            ChatUtil.copyImage(cq,g);
            cq = null;
        }*/
        //smooth scrolling
        boolean smoothScroll = ChattingSettingsManager.INSTANCE.getSettingToggledById("smoothscroll");
        if (!scrollTimer.hasTimeElapsed((int) (GuiManager.ANIMATION_INTERVAL/ChattingSettingsManager.INSTANCE.getFloatValueById("scrollspeed")), true) || !smoothScroll) return;
        IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
        int scrollDelta = Math.min(ChattingEnthusiast.SCROLLING_INTERVAL,Math.max(-ChattingEnthusiast.SCROLLING_INTERVAL, desiredScrollbarPos - cca.getChatScrollbarPos()));
        ignoreScroll = true;
        mc.gui.getChat().scrollChat(scrollDelta);
    }

    public void onClick() {
        GuiManager.INSTANCE.mouseTransformations = true;
        GuiManager.INSTANCE.scale = ((IChatComponentAccessor) Minecraft.getInstance().gui.getChat()).mixin$getScale();
        lineContainer.onClick();
        GuiManager.INSTANCE.mouseTransformations = false;
        chatContainer.onClick();
    }

    public void keyPressed(KeyEvent keyEvent) {
        lineContainer.keyPressed(keyEvent);
        chatContainer.keyPressed(keyEvent);
        CharacterEvent ce = new CharacterEvent(CharacterUtils.scancodeToCodepoint(keyEvent.scancode(), keyEvent.modifiers() > 0), keyEvent.modifiers());
        charTyped(ce);
    }
    public void charTyped(CharacterEvent characterEvent) {
        lineContainer.charTyped(characterEvent);
        chatContainer.charTyped(characterEvent);
    }

    public void tick() {
        if (search == null) return;
        if (!(mc.screen instanceof ChatScreen)) return;
        IChatScreenAccessor screen = (IChatScreenAccessor) mc.screen;
        if (search.getWidget().isFocused()) {
            screen.getInput().setCanLoseFocus(true);
            screen.getInput().setFocused(false);
        } else if (!screen.getInput().isFocused()) {
            screen.getInput().setCanLoseFocus(false);
            screen.getInput().setFocused(true);
        }
        String searchInput = ((EditBox) search.getWidget()).getValue();
        if (!searchInput.equals(ChattingEnthusiast.filter().getSearchCriteria())) {
            ChattingEnthusiast.filter().setSearch(searchInput);
        }
    }
    public double getChatOffset() {
        return chatOffset;
    }
    public void setChatOffset(double offset) {
        this.chatOffset = offset;
    }
}
