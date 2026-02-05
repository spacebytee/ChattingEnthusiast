package com.bytespacegames.config;

import com.bytespacegames.config.settings.*;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ConfigManager {
    private final String path, name;
    private final List<SettingsCategory> categories;
    protected final HashMap<String, String> oldToNew;
    public ConfigManager(String saveLocation, String name) {
        path = saveLocation;
        oldToNew = new HashMap<>();
        this.name = name;
        this.categories = new ArrayList<>();
    }
    public void addCategory(SettingsCategory c) {
        categories.add(c);
    }
    public List<SettingsCategory> getCategories() {
        return categories;
    }
    public String getName() {
        return name;
    }
    public boolean getSettingToggledById(String id) {
        Setting setting = getSettingById(id);
        if (!(setting instanceof BooleanSetting)) return false;
        return ((BooleanSetting) setting).getValue();
    }
    public float getFloatValueById(String id) {
        Setting setting = getSettingById(id);
        if (setting instanceof BooleanSetting booleanSetting) {
            return booleanSetting.getValue() ? 1f : 0;
        }
        if (!(setting instanceof FloatSetting)) return 0;
        return ((FloatSetting) setting).getValue();
    }
    public int getSelectedIndexById(String id) {
        Setting setting = getSettingById(id);
        if (!(setting instanceof StringSetting stringSetting)) {
            return 0;
        }
        return stringSetting.getIndex();
    }
    public int getColorById(String id) {
        Setting setting = getSettingById(id);
        if (!(setting instanceof ColorSetting colorSetting)) {
            return 0;
        }
        return colorSetting.getValue();
    }
    public Setting getSettingById(String id) {
        for (SettingsCategory c : categories) {
            for (Setting s : c.getSettings()) {
                if (s.getIdentifier().equalsIgnoreCase(id)) return s;
            }
        }
        return null;
    }
    public String getAbsolutePath() {
        return Minecraft.getInstance().gameDirectory.getAbsolutePath() + path;
    }
    public void load() {
        Path path = Paths.get(getAbsolutePath());
        Properties props = new Properties();
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
                // load by the map for legacy ids
                for (String oldId : oldToNew.keySet()) {
                    String newId = oldToNew.get(oldId);
                    String category = newId.split("\\.",2)[0];
                    String setting = newId.split("\\.",2)[1];
                    boolean set = false;
                    for (SettingsCategory c : categories) {
                        if (!c.getIdentifier().equals(category)) continue;
                        for (Setting s : c.getSettings()) {
                            if (!s.getIdentifier().equals(setting)) continue;
                            s.loadFromProperties(props, oldId);
                            set = true;
                            break;
                        }
                        if (set) break;
                    }
                }
                // load settings
                for (SettingsCategory c : categories) {
                    for (Setting s : c.getSettings()) {
                        String property = c.getIdentifier() + "." + s.getIdentifier();
                        if (!props.containsKey(property)) {
                            continue;
                        }
                        s.loadFromProperties(props, property);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                throw new RuntimeException("Failed to load config: " + getAbsolutePath(), e);
            }
        }
    }
    public void save() {
        Path path = Paths.get(getAbsolutePath());
        Properties props = new Properties();
        for (SettingsCategory c : categories) {
            for (Setting s : c.getSettings()) {
                s.saveToProperties(props, c.getIdentifier() + "." + s.getIdentifier());
            }
        }

        try (OutputStream out = Files.newOutputStream(path)) {
            props.store(out, name + " Config");
        } catch (IOException ignored) {
        }
    }
}
