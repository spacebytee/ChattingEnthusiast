package com.bytespacegames.config;

import com.bytespacegames.config.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public class SettingsCategory {
    private final String name, identifier;
    private final List<Setting> settings;
    public SettingsCategory(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
        this.settings = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public String getIdentifier() {
        return identifier;
    }
    public void addSetting(Setting s) {
        settings.add(s);
    }
    public List<Setting> getSettings() {
        return settings;
    }
}
