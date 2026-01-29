package com.bytespacegames.config.settings;

import java.util.Properties;

public class FloatSetting extends Setting {
    private float value;
    private final float minValue;
    private final float maxValue;
    public FloatSetting(String name, String identifier, String description, float defaultValue, float min, float max) {
        super(name,identifier,description);
        this.value = defaultValue;
        this.minValue = min;
        this.maxValue = max;
    }
    public void setValue(float b) {
        this.value = b;
    }
    public float getValue() {
        return value;
    }

    @Override
    public void saveToProperties(Properties p, String location) {
        p.setProperty(location,String.valueOf(value));
    }

    @Override
    public void loadFromProperties(Properties p, String location) {
        value = Float.parseFloat(p.getProperty(location, String.valueOf(value)));
    }

    public float getMin() {
        return minValue;
    }
    public float getMax() {
        return maxValue;
    }
}
