package com.bytespacegames.config.settings;

import java.util.Arrays;
import java.util.Properties;

public class StringSetting extends Setting {
    private final String[] options;
    private String value;
    private int index;
    public StringSetting(String name, String identifier, String description, String defaultValue, String... options) {
        super(name, identifier, description);
        this.options = options;
        this.value = defaultValue;
        index = Arrays.stream(options).toList().indexOf(value);
    }
    public String[] getOptions() {
        return options;
    }
    public String getValue() {
        return value;
    }
    public int getIndex() {
        return index;
    }
    public void setValue(String value) {
        this.value = value;
        index = Arrays.stream(options).toList().indexOf(value);
    }

    @Override
    public void saveToProperties(Properties p, String location) {
        p.setProperty(location,value);
    }

    @Override
    public void loadFromProperties(Properties p, String location) {
        setValue(p.getProperty(location, value));
    }
}
