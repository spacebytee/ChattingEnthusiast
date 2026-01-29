package com.bytespacegames.config.settings;

import java.util.Properties;

public abstract class Setting {
    private final String name, description, identifier;
    public Setting (String name, String identifier, String description) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getIdentifier() {
        return identifier;
    }
    public abstract void saveToProperties(Properties p, String location);
    public abstract void loadFromProperties(Properties p, String location);
}
