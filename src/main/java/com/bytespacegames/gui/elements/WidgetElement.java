package com.bytespacegames.gui.elements;

import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.TimerUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;

public class WidgetElement extends AbstractGuiElement {
    AbstractWidget widget;
    private final TimerUtils focusTimer;
    public WidgetElement(int x, int y, AbstractWidget widget, boolean visible) {
        super(x, y, widget.getWidth(),widget.getHeight(), visible);
        this.widget = widget;
        this.focusTimer = new TimerUtils();
    }

    @Override
    public void render(GuiGraphics g) {
        widget.setX(x);
        widget.setY(y);
        widget.render(g, GuiManager.getMouseX(),GuiManager.getMouseY(), 0);
    }

    @Override
    public void onClick() {
        widget.setFocused(true);
        widget.setX(x);
        widget.setY(y);
        widget.onClick(new MouseButtonEvent(GuiManager.getMouseX(),GuiManager.getMouseY(), new MouseButtonInfo(0,0)), false);
        widget.mouseClicked(new MouseButtonEvent(GuiManager.getMouseX(),GuiManager.getMouseY(), new MouseButtonInfo(0,0)), false);
    }
    public void clickOff() {
        if (!focusTimer.hasTimeElapsed(200, false)) return;
        widget.setFocused(false);
    }
    public void setFocused(boolean focused) {
        focusTimer.reset();
        widget.setFocused(true);
    }
    public void keyPressed(KeyEvent e) {
        widget.keyPressed(e);
    }
    public void charTyped(CharacterEvent characterEvent) {
        widget.charTyped(characterEvent);
    }
    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {
        widget.mouseDragged(mouseButtonEvent, d, e);
    }
    public AbstractWidget getWidget() {
        return widget;
    }
    public void setVisible(boolean visible) {
        if (!visible) {
            widget.setFocused(false);
        }
        super.setVisible(visible);
    }
}
