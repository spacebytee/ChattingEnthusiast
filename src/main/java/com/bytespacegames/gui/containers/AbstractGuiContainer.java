package com.bytespacegames.gui.containers;

import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.AbstractExpandableElement;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGuiContainer extends AbstractExpandableElement {
    protected final List<AbstractGuiElement> elements;
    public AbstractGuiContainer(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, visible);
        elements = new ArrayList<>();
    }
    // define how an element should be positioned (relative to the containers x,y)
    public abstract int getEffectiveX(int elementIndex);
    public abstract int getEffectiveY(int elementIndex);

    public List<AbstractGuiElement> getElements() {
        return elements;
    }
    public void addElement(AbstractGuiElement e) {
        if (e.parent != null) {
            throw new IllegalStateException("Element already has a parent.");
        }
        elements.add(e);
        e.parent = this;
    }
    public void removeElement(AbstractGuiElement e) {
        if (!elements.contains(e)) {
            return;
        }
        elements.remove(e);
        e.parent = null;
    }

    @Override
    public void render(GuiManager gui) {
        if (!visible) return;
        for (int i = 0; i < elements.size(); i++) {
            AbstractGuiElement e = elements.get(i);
            if (!e.isVisible()) continue;
            int effectiveX = this.x + getEffectiveX(i);
            int effectiveY = this.y + getEffectiveY(i);
            e.setAbsolutePosition(effectiveX,effectiveY);
            e.render(gui);
        }
    }

    @Override
    public void onClick() {
        if (!visible) return;
        for (int i = 0; i < elements.size(); i++) {
            AbstractGuiElement e = elements.get(i);
            if (!e.isVisible() && !(e instanceof AbstractGuiContainer)) continue;
            int effectiveX = this.x + getEffectiveX(i);
            int effectiveY = this.y + getEffectiveY(i);
            e.setAbsolutePosition(effectiveX,effectiveY);
            if (!e.isHovering(GuiManager.getMouseX(), GuiManager.getMouseY()) && !(e instanceof AbstractGuiContainer)) {
                e.clickOff();
                continue;
            }
            e.onClick();
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        for (AbstractGuiElement e : elements) {
            if (!e.isVisible()) continue;
            e.keyPressed(keyEvent);
        }
    }

    @Override
    public void charTyped(CharacterEvent characterEvent) {
        for (AbstractGuiElement e : elements) {
            if (!e.isVisible()) continue;
            e.charTyped(characterEvent);
        }
    }

    @Override
    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double f) {
        for (AbstractGuiElement e : elements) {
            if (!e.isVisible()) continue;
            e.mouseDragged(mouseButtonEvent, d, f);
        }
    }

    public void mouseReleased(MouseButtonEvent mouseButtonEvent) {
        for (AbstractGuiElement e : elements) {
            if (!e.isVisible()) continue;
            e.mouseReleased(mouseButtonEvent);
        }
    }
    public void mouseScrolled(double d, double e, double f, double g) {
        for (AbstractGuiElement e1 : elements) {
            if (!e1.isVisible()) continue;
            e1.mouseScrolled(d,e,f,g);
        }
    }
}
