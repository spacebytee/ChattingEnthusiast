package com.bytespacegames.gui.elements;

import com.bytespacegames.config.gui.ConfigGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;

public class InputFieldElement extends AbstractGuiElement {
    private String value;
    private final String placeholder;
    private final String prefix;
    private final int maxLength;
    private boolean focused = false;
    private int cursorPos;
    public boolean applyHexRestrictions = false;
    public InputFieldElement(String placeholder, String prefix, String defaultValue, int maxLength, int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, visible);
        this.placeholder = placeholder;
        this.prefix = prefix;
        this.value = defaultValue;
        this.maxLength = maxLength;
        cursorPos = value.length();
    }

    @Override
    public void render(GuiGraphics g) {
        g.fill(x, y, x + width, y + height, ConfigGui.PRIMARY_COLOR);
        g.enableScissor(x, y, x + width, y + height);
        if (value.isEmpty()) {
            g.drawString(Minecraft.getInstance().font, placeholder, x + 4, (int) (y + (height/2f) - 3.5f), ConfigGui.SECONDARY_COLOR);
            g.disableScissor();
            return;
        }
        cursorPos = Math.max(0,Math.min(cursorPos,value.length()));

        String displayText = prefix + value;
        if (cursorPos == value.length() && System.currentTimeMillis() % 1000 < 500 && focused) {
            displayText += "_";
        } else if (System.currentTimeMillis() % 1000 < 500 && focused) {
            int cursorX = Minecraft.getInstance().font.width(prefix) + Minecraft.getInstance().font.width(value.substring(0,cursorPos));
            g.fill(x + 4 + cursorX, (int) (y + (height/2f) - 4.5f), x + 5 + cursorX, (int) (y + (height/2f) + 4.5f), 0xFFFFFFFF);
        }
        g.drawString(Minecraft.getInstance().font, displayText, x + 4, (int) (y + (height/2f) - 3.5f), 0xFFFFFFFF);
        g.disableScissor();
    }

    @Override
    public void onClick() {
        focused = true;
        cursorPos = value.length();
    }
    @Override
    public void clickOff() {
        focused = false;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (!focused) return;
        switch (keyEvent.key()) {
            case 259:
                value = value.substring(0,Math.max(0,cursorPos-1)) + value.substring(Math.min(cursorPos,value.length()));
                cursorPos--;
                break;
            case 262:
                cursorPos++;
                break;
            case 263:
                cursorPos--;
                break;
        }
        cursorPos = Math.max(0,Math.min(cursorPos,value.length()));

        if (!keyEvent.isPaste()) {
            return;
        }
        String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
        String trimmed = clipboard.trim();
        // if the copied text perfectly fits within the max input length, replace the value
        if (applyHexRestrictions && trimmed.length() == maxLength && trimmed.matches("[0-9A-Fa-f]+")) {
            setValue(trimmed);
            return;
        }
        // if the copied text includes the prefix that should not be inserted, eg: #00DDFF
        if (applyHexRestrictions && !prefix.isEmpty() &&
                trimmed.length() == prefix.length() + maxLength &&
                trimmed.startsWith(prefix)) {
            String hex = trimmed.substring(prefix.length());
            if (!hex.matches("[0-9A-Fa-f]+")) return;
            setValue(hex);
            return;
        }
        // insert at the cursor position into current value
        insertText(clipboard);
    }

    @Override
    public void charTyped(CharacterEvent characterEvent) {
        if (!focused) return;
        insertText(characterEvent.codepointAsString());
    }

    public void insertText(String s) {
        if (applyHexRestrictions && !s.matches("[0-9A-Fa-f]+")) {
            return;
        }
        value = value.substring(0,Math.max(0,cursorPos)) + s + value.substring(Math.min(cursorPos,value.length()));
        value = value.substring(0,Math.min(maxLength, value.length()));
        cursorPos += s.length();
        cursorPos = Math.max(0,Math.min(cursorPos,value.length()));
    }

    public void setValue(String value) {
        this.value = value;
        cursorPos = value.length();
    }
    public String getValue() {
        return value;
    }
}
