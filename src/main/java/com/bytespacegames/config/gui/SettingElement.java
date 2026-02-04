package com.bytespacegames.config.gui;

import com.bytespacegames.config.settings.*;
import com.bytespacegames.gui.containers.AbstractGuiContainer;
import com.bytespacegames.gui.elements.*;
import com.bytespacegames.gui.elements.colorpicker.ColorPickerElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;

public class SettingElement extends AbstractGuiContainer {
    private final Setting setting;
    private AbstractGuiElement settingToggle;
    public SettingElement(Setting setting, int x, int y, int width, boolean visible) {
        super(x, y, width,
                30 + 7 + 5 + getLines(setting.getDescription(),
                        (int) (width * getSpacingBySetting(setting)) - 15) * 7 + (getLines(setting.getDescription(),
                        (int) (width * getSpacingBySetting(setting)) - 15) - 1) * 4,
                visible);
        this.setting = setting;
        if (setting instanceof BooleanSetting) {
            settingToggle = new ToggleSwitchElement((int) (width * .8 + 2) + 8,height / 2 - 8,30,16,((BooleanSetting) setting).getValue(), true);
        }
        if (setting instanceof FloatSetting floatSetting) {
            settingToggle = new SliderElement(width/2 + 30, height / 2 - 8, width/2 - 5 - 30, 16, (floatSetting.getValue() - floatSetting.getMin())/(floatSetting.getMax()-floatSetting.getMin()), floatSetting.getMin(), floatSetting.getMax(), true);
        }
        if (setting instanceof StringSetting stringSetting) {
            settingToggle = new DropdownElement((int) (width * .6 + 2) + 15, height / 2 - 8, (int) (width * .4 - 6) - 15, 16, true, stringSetting.getValue(), stringSetting.getOptions());
        }
        if (setting instanceof ColorSetting colorSetting) {
            settingToggle = new ColorPickerElement((int) (width * .8 + 2) + 15, height / 2 - 8, 16, 16, colorSetting.getValue(), true);
        }
        if (settingToggle != null) {
            addElement(settingToggle);
        }
    }
    private static double getSpacingBySetting(Setting setting) {
        return setting instanceof StringSetting ? .6 : (setting instanceof FloatSetting ? .5 : .8);
    }
    private static int getLines(String text, int width) {
        Font f = Minecraft.getInstance().font;
        if (f.width(text) <= width) {
            return 1;
        }
        int i = 1;
        String[] words = text.split(" ");
        StringBuilder built = new StringBuilder();
        for (String word : words) {
            if (f.width(built + word) > width) {
                i++;
                built = new StringBuilder();
            }
            built.append(word).append(" ");
        }
        return i;
    }
    private void drawLines(GuiGraphics g, String text, int x, int y) {
        Font f = Minecraft.getInstance().font;
        if (f.width(text) <= getWidth() * getSpacingBySetting(setting) - 15) {
            g.drawString(f, text,x,y,ConfigGui.SECONDARY_COLOR);
            return;
        }
        int i = 0;
        String[] words = text.split(" ");
        StringBuilder built = new StringBuilder();
        for (String word : words) {
            if (f.width(built + word) > getWidth() * getSpacingBySetting(setting) - 15) {
                g.drawString(f, built.toString().trim(),x,y + i * 11,ConfigGui.SECONDARY_COLOR);
                i++;
                built = new StringBuilder();
            }
            built.append(word).append(" ");
        }
        g.drawString(f, built.toString(),x,y + i * 11,ConfigGui.SECONDARY_COLOR);
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
        if (setting instanceof BooleanSetting booleanSetting) {
            ToggleSwitchElement toggle = (ToggleSwitchElement) settingToggle;
            booleanSetting.setValue(toggle.getValue());
        }
        if (setting instanceof StringSetting stringSetting) {
            DropdownElement dropdown = (DropdownElement) settingToggle;
            stringSetting.setValue(dropdown.getValue());
        }
    }
    @Override
    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double i, double j) {
        super.mouseDragged(mouseButtonEvent,i,j);
        if (setting instanceof FloatSetting floatSetting) {
            SliderElement toggle = (SliderElement) settingToggle;
            floatSetting.setValue(toggle.getScaledValue());
        }
        if (setting instanceof ColorSetting colorSetting) {
            ColorPickerElement picker = (ColorPickerElement) settingToggle;
            colorSetting.setValue(picker.getColor());
        }
    }
}
