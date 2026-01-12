package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;

public class WidgetElement extends AbstractGuiElement {
    AbstractWidget widget;
    public WidgetElement(int x, int y, AbstractWidget widget, boolean visible) {
        super(x, y, widget.getWidth(),widget.getHeight(), visible);
        this.widget = widget;
    }

    @Override
    public void render(GuiGraphics g) {
        widget.setX(x);
        widget.setY(y);
        ChattingComponent c = ChattingEnthusiast.chatting();
        widget.render(g,c.getMouseX(),c.getMouseY(), 0);
    }

    @Override
    public void onClick() {
        ChattingComponent c = ChattingEnthusiast.chatting();
        widget.setFocused(true);
        widget.setX(x);
        widget.setY(y);
        widget.onClick(new MouseButtonEvent(c.getMouseX(),c.getMouseY(), new MouseButtonInfo(0,0)), false);
        widget.mouseClicked(new MouseButtonEvent(c.getMouseX(),c.getMouseY(), new MouseButtonInfo(0,0)), false);
    }
    public void clickOff() {
        widget.setFocused(false);
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
}
