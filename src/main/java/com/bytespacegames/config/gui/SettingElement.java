package com.bytespacegames.config.gui;

import com.bytespacegames.config.BooleanSetting;
import com.bytespacegames.config.Setting;
import com.bytespacegames.gui.containers.AbstractGuiContainer;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import com.bytespacegames.gui.elements.ToggleSwitchElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class SettingElement extends AbstractGuiContainer {
    private final Setting setting;
    private AbstractGuiElement settingToggle;
    public SettingElement(Setting setting, int x, int y, int width, boolean visible) {
        super(x, y, width, 30 + 7 + 5 + getLines(setting.getDescription(), width) * 7 + (getLines(setting.getDescription(), width) - 1) * 4, visible);
        this.setting = setting;
        if (setting instanceof BooleanSetting) {
            settingToggle = new ToggleSwitchElement(width - 35,height / 2 - 8,30,16,((BooleanSetting) setting).getValue(), true);
        }
        if (settingToggle != null) {
            addElement(settingToggle);
        }
    }
    private static int getLines(String text, int width) {
        Font f = Minecraft.getInstance().font;
        if (f.width(text) <= width * .8) {
            return 1;
        }
        int i = 1;
        String[] words = text.split(" ");
        String built = "";
        for (String word : words) {
            if (f.width(built + word) > width * .8) {
                i++;
                built = "";
            }
            built += word + " ";
        }
        return i;
    }
    private void drawLines(GuiGraphics g, String text, int x, int y) {
        Font f = Minecraft.getInstance().font;
        if (f.width(text) <= getWidth() * .8) {
            g.drawString(f, text,x,y,ConfigGui.SECONDARY_COLOR);
            return;
        }
        int i = 0;
        String[] words = text.split(" ");
        String built = "";
        for (String word : words) {
            if (f.width(built + word) > getWidth() * .8) {
                g.drawString(f, built.trim(),x,y + i * 11,ConfigGui.SECONDARY_COLOR);
                i++;
                built = "";
            }
            built += word + " ";
        }
        g.drawString(f, built,x,y + i * 11,ConfigGui.SECONDARY_COLOR);
    }
    @Override
    public int getEffectiveX(int elementIndex) {
        return elements.get(elementIndex).getX();
    }

    @Override
    public int getEffectiveY(int elementIndex) {
        return elements.get(elementIndex).getY();
    }

    @Override
    public int getBottomBound() {
        return height;
    }

    @Override
    public void render(GuiGraphics g) {
        g.fill(x,y,x+width,y+height, ConfigGui.PRIMARY_COLOR);
        g.renderOutline(x,y,width,height, ConfigGui.SECONDARY_COLOR);
        g.drawString(Minecraft.getInstance().font, setting.getName(), x + 15,y + 15, 0xFFFFFFFF);
        drawLines(g, setting.getDescription(), x + 15,y + 15+12);
        super.render(g);
    }

    @Override
    public void onClick() {
        super.onClick();
        if (setting instanceof BooleanSetting) {
            ToggleSwitchElement toggle = (ToggleSwitchElement) settingToggle;
            ((BooleanSetting) setting).setValue(toggle.getValue());
        }
    }
}
