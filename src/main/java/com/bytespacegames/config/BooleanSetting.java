package com.bytespacegames.config;

import java.util.Properties;

public class BooleanSetting extends Setting {
    private boolean value;
    public BooleanSetting(String name, String identifier, String description, boolean defaultValue) {
        super(name,identifier,description);
        this.value = defaultValue;
    }
    public void setValue(boolean b) {
        this.value = b;
    }
    public boolean getValue() {
        return value;
    }

    @Override
    public void saveToProperties(Properties p, String location) {
        p.setProperty(location,String.valueOf(value));
    }

    @Override
    public void loadFromProperties(Properties p, String location) {
        value = Boolean.parseBoolean(p.getProperty(location, String.valueOf(value)));
    }
}
