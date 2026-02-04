package com.bytespacegames.config.settings;

import java.util.Properties;

public class ColorSetting extends Setting {
    private int value;
    public ColorSetting(String name, String identifier, String description, int defaultValue) {
        super(name,identifier,description);
        this.value = defaultValue;
    }
    public void setValue(int b) {
        this.value = b;
    }
    public int getValue() {
        return value;
    }

    @Override
    public void saveToProperties(Properties p, String location) {
        p.setProperty(location,String.valueOf(value));
    }

    @Override
    public void loadFromProperties(Properties p, String location) {
        value = Integer.parseInt(p.getProperty(location, String.valueOf(value)));
    }
}
