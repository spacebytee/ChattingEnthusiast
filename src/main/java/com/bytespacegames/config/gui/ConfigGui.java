package com.bytespacegames.config.gui;

import com.bytespacegames.config.ConfigManager;
import com.bytespacegames.config.Setting;
import com.bytespacegames.config.SettingsCategory;
import com.bytespacegames.gui.containers.BasicContainer;
import com.bytespacegames.gui.containers.ScrollingContainer;
import com.bytespacegames.gui.containers.DownwardsExpandingContainer;
import com.bytespacegames.gui.elements.RectangleElement;
import com.bytespacegames.gui.elements.TextElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.HashMap;

public class ConfigGui extends Screen {
    BasicContainer container;
    public SettingsCategory selectedCategory;
    private final ConfigManager config;
    private final ScrollingContainer scrollBox;
    private final DownwardsExpandingContainer settings;
    private final HashMap<SettingsCategory, CategoryLabelElement> categoryLabels;
    public static final int PRIMARY_COLOR = 0xFF242424;
    public static final int SECONDARY_COLOR = 0xFF5C5C5C;
    public static final int BACKGROUND_COLOR = 0xFF121212;
    public ConfigGui(ConfigManager c) {
        super(Component.literal(c.getName()));
        this.config = c;
        categoryLabels = new HashMap<>();
        // gui
        container = new BasicContainer(0,0,Math.min(Minecraft.getInstance().getWindow().getGuiScaledWidth(), 400)
                ,Math.min(Minecraft.getInstance().getWindow().getGuiScaledHeight(), 250),
                BACKGROUND_COLOR, true);
        BasicContainer bar = new BasicContainer(0,0,Math.min(Minecraft.getInstance().getWindow().getGuiScaledWidth(), 400),30,PRIMARY_COLOR, true);
        bar.addElement(new TextElement(c.getName(), 7, (bar.getHeight() / 2) - TextElement.TEXT_HEIGHT /2, 0xFFFFFFFF, true));
        bar.addElement(new CloseButton(bar.getWidth()-7-16,7,true));
        container.addElement(bar);
        container.addElement(new RectangleElement(117,0,3,container.getHeight(),PRIMARY_COLOR, true));

        // sidebar
        DownwardsExpandingContainer sidebar = new DownwardsExpandingContainer(7,37,7,true);
        for (SettingsCategory category : c.getCategories()) {
            if (selectedCategory == null) selectedCategory = category;
            sidebar.addElement(new CategoryButton(this,category,0,0,true));
        }
        container.addElement(sidebar);

        // settings
        scrollBox = new ScrollingContainer(120,30,container.getWidth() - 120 - 3,container.getHeight()-30,SECONDARY_COLOR,true);
        settings = new DownwardsExpandingContainer(5,5,5,true);
        scrollBox.addElement(settings);
        for (SettingsCategory cat : c.getCategories()) {
            CategoryLabelElement categoryLabel = new CategoryLabelElement(cat.getName(), 0, 0, scrollBox.getWidth() - 10, true);
            categoryLabels.put(cat, categoryLabel);
            settings.addElement(categoryLabel);
            for (Setting setting : cat.getSettings()) {
                settings.addElement(new SettingElement(setting, 0, 0, scrollBox.getWidth() - 10, true));
            }
        }
        container.addElement(scrollBox);
    }
    public void setSelectedCategory(SettingsCategory c) {
        selectedCategory = c;
        scrollBox.setScrollOffset(Math.min(scrollBox.getContentsBound() - scrollBox.getHeight(), settings.getEffectiveY(settings.getElements().indexOf(categoryLabels.get(c)))));
    }
    public void render(GuiGraphics g, int i, int j, float f) {
        int centerY = this.height / 2;
        int centerX = this.width / 2;
        container.setX(centerX - container.getWidth()/2);
        container.setY(centerY - container.getHeight()/2);
        super.render(g, i, j, f);
        container.render(g);
    }
    public boolean isPauseScreen() {
        return false;
    }
    @Override
    public void onClose() {
        super.onClose();
        this.minecraft.setScreen(null);
        config.save();
    }
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (mouseButtonEvent.button() == 0) {
            container.onClick();
        }
        return super.mouseClicked(mouseButtonEvent, bl);
    }

    public boolean keyPressed(KeyEvent keyEvent) {
        container.keyPressed(keyEvent);
        return super.keyPressed(keyEvent);
    }
    public boolean charTyped(CharacterEvent characterEvent) {
        container.charTyped(characterEvent);
        return super.charTyped(characterEvent);
    }
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {
        container.mouseDragged(mouseButtonEvent, d, e);
        return super.mouseDragged(mouseButtonEvent, d, e);
    }
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        container.mouseReleased(mouseButtonEvent);
        return super.mouseReleased(mouseButtonEvent);
    }
    public boolean mouseScrolled(double d, double e, double f, double g) {
        return super.mouseScrolled(d,e,f,g);
    }
}
