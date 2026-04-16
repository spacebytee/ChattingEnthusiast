package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.config.settings.BooleanSetting;
import com.bytespacegames.config.ConfigManager;
import com.bytespacegames.config.SettingsCategory;
import com.bytespacegames.config.settings.ColorSetting;
import com.bytespacegames.config.settings.FloatSetting;
import com.bytespacegames.config.settings.StringSetting;
import net.minecraft.client.Minecraft;

public class ChattingSettingsManager extends ConfigManager {
    public static ChattingSettingsManager INSTANCE;
    public final SettingsCategory function,visual,chatting,chattabs,compactchat;
    public ChattingSettingsManager() {
        super("/config/chattingenthusiast.properites", "ChattingEnthusiast");
        INSTANCE = this;
        function = new SettingsCategory("Chat Functionality", "function");
        function.addSetting(new BooleanSetting("Don't Clear Chat on Disconnect",
                "clearchat",
                "When enabled, when disconnecting from a server, or swapping worlds, your chat will persist, rather than being cleared.",
                true));
        function.addSetting(new BooleanSetting("Extended Chat History",
                "chathistory",
                "Increases chat history from 100 to 16,384.",
                true));

        visual = new SettingsCategory("Visuals", "visual");
        visual.addSetting(new ColorSetting("Chat Background Color",
                "backgroundcolor",
                "Color of the chat background.",
                0xFF000000));
        visual.addSetting(new ColorSetting("Message Hover Color",
                "hovercolor",
                "Color of a hovered message highlight.",
                0xFFFFFFFF));
        visual.addSetting(new BooleanSetting("Button Backgrounds",
                "buttonbackgrounds",
                "Shows backgrounds behind chat action buttons and line controls.",
                true));
        visual.addSetting(new FloatSetting("Message Hover Opacity",
                "hoveropacity",
                "Opacity of the hovered message highlight.",
                0.3f,0f,1f));
        visual.addSetting(new BooleanSetting("Button Backgrounds",
                "buttonbackgrounds",
                "Shows backgrounds behind chat action buttons and line controls.",
                true));
        visual.addSetting(new BooleanSetting("Smooth Scrolling",
                "smoothscroll",
                "Makes scrolling using the scroll wheel gradual.",
                true));
        visual.addSetting(new BooleanSetting("Smooth Scroll Timeout",
                "scrolltimeout",
                "If you are not actively moving the scroll wheel, stop the smooth scroll animation early.",
                true));
        visual.addSetting(new FloatSetting("Scroll Animation Speed",
                "scrollspeed",
                "Mutliplier for the speed of the smooth scrolling animation.",
                1f,.1f,2.0f));
        visual.addSetting(new BooleanSetting("New Message Animation",
                "animation",
                "Shifts the chat up to create a smooth animation for new messages.",
                true));
        visual.addSetting(new BooleanSetting("Hide Scrollbar",
                "noscroll",
                "Hides the vanilla scrollbar.",
                true));
        visual.addSetting(new BooleanSetting("Raised Chat",
                "raisedchat",
                "Raises chat by 10 pixels to not overlap with armor bars.",
                false));
        visual.addSetting(new BooleanSetting("Hide Gui Message Tags",
                "notags",
                "Skips rendering the message tag to the left of certain messages, such as server messages.",
                true));

        chatting = new SettingsCategory("Chatting Features", "chatting");
        chatting.addSetting(new BooleanSetting("Line Controls",
                "linecontrols",
                "Allows you to copy, delete, and jump to lines (if filtering) when hovering over a message.",
                true));
        chatting.addSetting(new BooleanSetting("Show Copy Button",
                "showcopybutton",
                "Shows the copy button in the line controls when hovering over a message.",
                true));
        chatting.addSetting(new BooleanSetting("Show Delete Button",
                "showdeletebutton",
                "Shows the delete button in the line controls when hovering over a message.",
                true));
        chatting.addSetting(new BooleanSetting("Show Copy Button",
                "showcopybutton",
                "Shows the copy button in the line controls when hovering over a message.",
                true));
        chatting.addSetting(new BooleanSetting("Show Delete Button",
                "showdeletebutton",
                "Shows the delete button in the line controls when hovering over a message.",
                true));
        chatting.addSetting(new BooleanSetting("Copy Tooltip",
                "tooltip",
                "Shows the tooltip explaining the different options when hovering over the copy button.",
                true));
        chatting.addSetting(new BooleanSetting("Chat Controls",
                "chatcontrols",
                "Controls in the bottom right that allows you to clear chat or search through chat.",
                true));
        chatting.addSetting(new BooleanSetting("Search Clears on Close",
                "clearsearch",
                "If enabled, the search filter will automatically clear when you close the chat screen.",
                true));
        chatting.addSetting(new BooleanSetting("Message Hover",
                "messagehover",
                "Highlights the background of a hovered message.",
                true));
        chatting.addSetting(new BooleanSetting("Command Tooltips",
                "tooltipcommands",
                "Shows the command ran by clickable command components/messages in the tooltip.",
                false));
        compactchat = new SettingsCategory("Compact Chat", "compactchat");
        compactchat.addSetting(new BooleanSetting("Compact Chat",
                "compactchat",
                "Condenses identical repeated messages into one.",
                true));
        compactchat.addSetting(new FloatSetting("Compact Chat Time",
                "compactchattime",
                "The maximum amount of time between identical messages to be compacted.",
                5f,1f,20f));
        compactchat.addSetting(new BooleanSetting("Don't Compact Dividers",
                "exclusions",
                "Messages seemingly intended as dividers, (eg, all white space, all hyphens), are excluded from being compacted.",
                true));
        compactchat.addSetting(new StringSetting("Compact Tag Format",
                "compactformat",
                "Determines the formatting of the compact chat tag following a compacted message.",
                "(6)", "(6)", "(x6)", "[6]", "[x6]"));
        compactchat.addSetting(new ColorSetting("Compact Tag Color",
                "tagcolor",
                "Color of the compact chat tag.",
                0xFFAAAAAA));
        chattabs = new SettingsCategory("Chat Tabs", "chattabs");
        chattabs.addSetting(new BooleanSetting("Chat Tabs",
                "chattabs",
                "Shows tabs for PM, all, party, and guild chat when connected to Hypixel.",
                true));
        chattabs.addSetting(new BooleanSetting("Tab Filters",
                "tabfilters",
                "Chat tabs will filter the chat to just the messages in that channel.",
                true));
        chattabs.addSetting(new BooleanSetting("Switch Chat Channels",
                "switchchannels",
                "Switching chat tabs will also switch you to the accompanying Hypixel chat channel.",
                true));
        chattabs.addSetting(new BooleanSetting("All Tab Switches Channel",
                "alltabchannel",
                "Switching to the all channel will switch you to all chat. Useful to be disabled, since the all tab never filters chat on it's own.",
                false));
        addCategory(visual);
        addCategory(function);
        addCategory(chatting);
        addCategory(compactchat);
        addCategory(chattabs);
        setupUpdateSubstitutes();
        load();
        save();
    }
    public void setupUpdateSubstitutes() {
        oldToNew.put("chatting.compactchat","compactchat.compactchat");
        oldToNew.put("chatting.compactchattime","compactchat.compactchattime");
    }
    @Override
    public void save() {
        super.save();
        try {
            ((IChatComponentAccessor)Minecraft.getInstance().gui.getChat()).mixin$refreshTrimmedMessages();
        } catch (Exception ignored) {}
    }
}
