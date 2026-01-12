package com.bytespacegames.chattingenthusiast.config;

import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigManager {
    private final String path, name;
    private final List<SettingsCategory> categories;
    public ConfigManager(String saveLocation, String name) {
        path = saveLocation;
        this.name = name;
        this.categories = new ArrayList<SettingsCategory>();
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
    public String getAbsolutePath() {
        return Minecraft.getInstance().gameDirectory.getAbsolutePath() + path;
    }
    public void load() {
        Path path = Paths.get(getAbsolutePath());
        Properties props = new Properties();
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
                for (SettingsCategory c : categories) {
                    for (Setting s : c.getSettings()) {
                        s.loadFromProperties(props, c.getIdentifier() + "." + s.getIdentifier());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
