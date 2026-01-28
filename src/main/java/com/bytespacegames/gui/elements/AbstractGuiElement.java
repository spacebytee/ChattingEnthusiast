package com.bytespacegames.gui.elements;

import com.bytespacegames.gui.containers.AbstractGuiContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class AbstractGuiElement {
    protected int x,y,relativeX,relativeY;
    protected boolean visible;
    public AbstractGuiContainer parent;
    protected int width;
    protected int height;
    public static final int baseButtonColor = 0xFF000000;
    public static final int baseIconColor = 0xFF7F7F7F;
    public static final int hoveredButtonColor = 0xFFFFFFFF;
    public static final int hoveredIconColor = 0xFFFFFFFF;
    // note: x & y internally refer to the absolute positioning on the screen, so in render functions it is more concise to use those variables,
    // relative positions are the stored positions used to manipulate elements, and are what x & y refer to externally
    public AbstractGuiElement(int x, int y, int width, int height, boolean visible) {
        this.relativeX = x;
        this.relativeY = y;
        this.width = width;
        this.height = height;
        this.visible = visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean isVisible() {
        return visible;
    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getY() {
        return relativeY;
    }

    public void setY(int y) {
        setRelativePosition(this.relativeX,y);
    }

    public int getX() {
        return relativeX;
    }
    public void setX(int x) {
        setRelativePosition(x,this.relativeY);
    }

    public int getBottomBound() {
        return getHeight();
    }

    public void setRelativePosition(int x, int y) {
        this.relativeX = x;
        this.relativeY = y;
        if (parent == null) {
            setAbsolutePosition(relativeX,relativeY);
        }
    }
    public void setAbsolutePosition(int effectiveX, int effectiveY) {
        this.x = effectiveX;
        this.y = effectiveY;
    }

    public void setConfig(int x, int y, int width, int height, boolean visible) {
        setRelativePosition(x,y);
        this.width = width;
        this.height = height;
        this.visible = visible;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y  && mouseY < y + height;
    }
    public abstract void render(GuiGraphics g);
    public abstract void onClick();
    public void keyPressed(KeyEvent keyEvent) {}
    public void charTyped(CharacterEvent characterEvent) {}
    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {}
    public void clickOff() {}

    public void mouseReleased(MouseButtonEvent mouseButtonEvent) {}
}
