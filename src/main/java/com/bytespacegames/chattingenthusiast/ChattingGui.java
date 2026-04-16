package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.containers.BasicContainer;
import com.bytespacegames.gui.containers.BottomLeftOriginatingContainer;
import com.bytespacegames.gui.containers.BottomRightOriginatingContainer;
import com.bytespacegames.chattingenthusiast.gui.elements.*;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.chattingenthusiast.mixin.IChatScreenAccessor;
import com.bytespacegames.chattingenthusiast.utils.TimerUtils;
import com.bytespacegames.chattingenthusiast.gui.elements.SearchElement;
import com.bytespacegames.gui.elements.WidgetElement;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.List;

public class ChattingGui {
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
        chatControls.addElement(new ScreenshotChatElement(0,0,13,13, FabricLoader.getInstance().isModLoaded("chatshot")));

        chatTabs = new BottomLeftOriginatingContainer(0,0,2,false);
        chatTabs.addElement(new TabElement("ALL", ChatFilter.TabFilter.NONE, 0,0,true));
        chatTabs.addElement(new TabElement("PARTY", ChatFilter.TabFilter.PARTY, 0,0,true));
        chatTabs.addElement(new TabElement("GUILD", ChatFilter.TabFilter.GUILD, 0,0,true));
        chatTabs.addElement(new TabElement("PM", ChatFilter.TabFilter.PM, 0,0,true));

        chatContainer.addElement(chatControls);
        chatContainer.addElement(chatTabs);
    }
    public void renderCustomLine(GuiManager gui, int x, int mx, int nx, int lineIndex, float opacity) {
        if (animationTimer.hasTimeElapsed(GuiManager.ANIMATION_INTERVAL, true)) chatOffset /= 1.3;
        IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
        int scaleOffset = Mth.ceil(cca.mixin$getWidth() / mc.options.chatScale().get());
        float baseBackgroundOpacity = mc.options.textBackgroundOpacity().get().floatValue();
        boolean messageHoverEnabled = ChattingSettingsManager.INSTANCE.getSettingToggledById("messagehover");
        float hoverOpacity = ChattingSettingsManager.INSTANCE.getFloatValueById("hoveropacity");
        // lineIndex is relative to the bottom chat message as scrolled, so we need to account for scroll position
        int hoveredIndex = ChattingEnthusiast.INSTANCE.getHoveredMessage(mouseX,mouseY) - cca.getChatScrollbarPos();
        boolean hoveredMessage = lineIndex == hoveredIndex && messageHoverEnabled;

        // draw message background
        if (baseBackgroundOpacity > 0) {
            gui.fill(x - 4, mx, x + scaleOffset + 4 + 4, nx,
                    ARGB.color(opacity * baseBackgroundOpacity, ChattingSettingsManager.INSTANCE.getColorById("backgroundcolor")));
        }
        if (hoveredMessage && hoverOpacity > 0) {
            gui.fill(x - 4, mx, x + scaleOffset + 4 + 4, nx,
                    ARGB.color(opacity * hoverOpacity, ChattingSettingsManager.INSTANCE.getColorById("hovercolor")));
        }

        if (lineIndex != hoveredIndex) return;
        boolean lineControlsEnabled = ChattingSettingsManager.INSTANCE.getSettingToggledById("linecontrols");
        boolean showCopyButton = lineControlsEnabled && ChattingSettingsManager.INSTANCE.getSettingToggledById("showcopybutton");
        boolean showDeleteButton = lineControlsEnabled && ChattingSettingsManager.INSTANCE.getSettingToggledById("showdeletebutton");
        boolean showJumpButton = lineControlsEnabled && !ChattingEnthusiast.filter().unfiltered();
        lineContainer.setVisible(showCopyButton || showDeleteButton || showJumpButton);

        int buttonX = x + scaleOffset + 8 + 1;
        int buttonSize = cca.mixin$getLineHeight();
        if (showCopyButton) {
            copy.setConfig(buttonX, mx, buttonSize, buttonSize, true);
            copy.setMessage(lineIndex);
            buttonX += buttonSize + 1;
        } else {
            copy.setVisible(false);
        }
        if (showDeleteButton) {
            delete.setConfig(buttonX, mx, buttonSize, buttonSize, true);
            delete.setMessage(lineIndex);
            buttonX += buttonSize + 1;
        } else {
            delete.setVisible(false);
        }
        if (showJumpButton) {
            jump.setConfig(buttonX, mx, buttonSize, buttonSize, true);
            jump.setMessage(lineIndex);
        } else {
            jump.setVisible(false);
        }

        int hovered = ChattingEnthusiast.INSTANCE.getHoveredMessage(mouseX,mouseY);
        if (hovered == -1) {
            copy.setVisible(false);
            delete.setVisible(false);
            jump.setVisible(false);
        }
        // render lineContainer (per-line controls) with the scaling/offset of the chat line
        GuiManager.INSTANCE.mouseTransformations = true;
        GuiManager.INSTANCE.scale = ((IChatComponentAccessor) Minecraft.getInstance().gui.getChat()).mixin$getScale();
        lineContainer.render(gui);
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
    private long lastScroll;
    // these methods come from ChatScreen, and therefore only are called when chat is focused
    public void render(GuiManager gui) {
        setupCustomElements();
        chatContainer.render(gui);
        //smooth scrolling
        boolean smoothScroll = ChattingSettingsManager.INSTANCE.getSettingToggledById("smoothscroll");
        if (!scrollTimer.hasTimeElapsed((int) (GuiManager.ANIMATION_INTERVAL/ChattingSettingsManager.INSTANCE.getFloatValueById("scrollspeed")), true) || !smoothScroll) return;
        IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
        if (System.currentTimeMillis() - lastScroll > 500 && ChattingSettingsManager.INSTANCE.getSettingToggledById("scrolltimeout")) {
            desiredScrollbarPos = cca.getChatScrollbarPos();
            return;
        }
        int scrollDelta = Math.min(ChattingEnthusiast.SCROLLING_INTERVAL,Math.max(-ChattingEnthusiast.SCROLLING_INTERVAL, desiredScrollbarPos - cca.getChatScrollbarPos()));
        ignoreScroll = true;
        mc.gui.getChat().scrollChat(scrollDelta);
    }
    public void onScroll() {
        lastScroll = System.currentTimeMillis();
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
