package com.bytespacegames.chattingenthusiast.config.gui;

import com.bytespacegames.chattingenthusiast.config.ConfigManager;
import com.bytespacegames.chattingenthusiast.config.SettingsCategory;
import com.bytespacegames.chattingenthusiast.gui.containers.BasicContainer;
import com.bytespacegames.chattingenthusiast.gui.containers.TopLeftOriginatingContainer;
import com.bytespacegames.chattingenthusiast.gui.elements.RectangleElement;
import com.bytespacegames.chattingenthusiast.gui.elements.TextElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ConfigGui extends Screen {
    BasicContainer container;
    public SettingsCategory selectedCategory;
    public ConfigGui(ConfigManager c) {
        super(Component.literal(c.getName()));
        container = new BasicContainer(0,0,400,250,0xFF121212, true);
        BasicContainer bar = new BasicContainer(0,0,400,30,0xFF242424, true);
        bar.addElement(new TextElement(c.getName(), 7, (bar.getHeight() / 2) - TextElement.TEXT_HEIGHT /2, 0xFFFFFFFF, true));
        bar.addElement(new CloseButton(bar.getWidth()-7-16,7,true));
        container.addElement(bar);
        container.addElement(new RectangleElement(117,0,3,container.getHeight(),0xFF242424, true));
        TopLeftOriginatingContainer sidebar = new TopLeftOriginatingContainer(7,37,7,true);
        for (SettingsCategory category : c.getCategories()) {
            if (selectedCategory == null) selectedCategory = category;
            sidebar.addElement(new CategoryButton(this,category,0,0,true));
        }
        container.addElement(sidebar);
    }
    public void render(GuiGraphics g, int i, int j, float f) {
        int centerY = this.height / 2;
        int centerX = this.width / 2;
        container.setX(centerX - container.getWidth()/2);
        container.setY(centerY - container.getHeight()/2);
        super.render(g, i, j, f);
        container.render(g);
        //guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal("AutoGG Settings"), centerX, centerY-55, 0xFFFFFFFF);
        //guiGraphics.drawString(this.font, Component.literal("GG Message"), centerX - 75, centerY - 38, 0xFFFFFFFF, true);
        //guiGraphics.drawString(this.font, Component.literal("GG Delay (seconds)"), centerX - 75, centerY + 4, 0xFFFFFFFF,true);
    }
    public boolean isPauseScreen() {
        return false;
    }
    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
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
}
