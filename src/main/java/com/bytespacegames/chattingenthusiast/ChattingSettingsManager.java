package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.chattingenthusiast.config.BooleanSetting;
import com.bytespacegames.chattingenthusiast.config.ConfigManager;
import com.bytespacegames.chattingenthusiast.config.SettingsCategory;

public class ChattingSettingsManager extends ConfigManager {
    public static ChattingSettingsManager INSTANCE;
    public final SettingsCategory function;
    public final SettingsCategory visual;
    public ChattingSettingsManager() {
        super("/config/chattingenthusiast.properites", "ChattingEnthusiast");
        INSTANCE = this;
        function = new SettingsCategory("Chat Functionality", "function");
        function.addSetting(new BooleanSetting("Don't Clear Chat on Disconnect",
                "clearchat",
                "When enabled, when disconnecting from a server, or swapping worlds, your chat will persist, rather than being cleared.",
                true));
        visual = new SettingsCategory("Visuals", "visual");
        visual.addSetting(new BooleanSetting("Smooth Scrolling",
                "smoothscroll",
                "Makes scrolling using the scroll wheel gradual.",
                true));
        visual.addSetting(new BooleanSetting("New Message Animation",
                "animation",
                "Shifts the chat up to create a smooth animation for new messages.",
                true));
        addCategory(visual);
        addCategory(function);
        load();
        save();
    }
}
