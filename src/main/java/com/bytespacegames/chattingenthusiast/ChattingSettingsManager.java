package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.config.settings.BooleanSetting;
import com.bytespacegames.config.ConfigManager;
import com.bytespacegames.config.SettingsCategory;
import com.bytespacegames.config.settings.FloatSetting;

public class ChattingSettingsManager extends ConfigManager {
    public static ChattingSettingsManager INSTANCE;
    public final SettingsCategory function,visual,chatting;
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
        visual.addSetting(new BooleanSetting("Smooth Scrolling",
                "smoothscroll",
                "Makes scrolling using the scroll wheel gradual.",
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
        chatting.addSetting(new BooleanSetting("Chat Tabs",
                "chattabs",
                "Shows tabs for PM, all, party, and guild chat when connected to Hypixel.",
                true));
        chatting.addSetting(new BooleanSetting("Line Controls",
                "linecontrols",
                "Allows you to copy, delete, and jump to lines (if filtering) when hovering over a message.",
                true));
        chatting.addSetting(new BooleanSetting("Chat Controls",
                "chatcontrols",
                "Controls in the bottom right that allows you to clear chat or search through chat.",
                true));
        chatting.addSetting(new BooleanSetting("Message Hover",
                "messagehover",
                "Highlights the background of a hovered message.",
                true));
        chatting.addSetting(new BooleanSetting("Compact Chat",
                "compactchat",
                "Condenses identical repeated messages into one.",
                true));
        chatting.addSetting(new FloatSetting("Compact Chat Time",
                "compactchattime",
                "The maximum amount of time between identical messages to be compacted.",
                5f,1f,20f));

        addCategory(visual);
        addCategory(function);
        addCategory(chatting);
        load();
        save();
    }
}
