package com.bytespacegames.gui.elements;

import com.bytespacegames.config.gui.ConfigGui;
import com.bytespacegames.gui.Animator;
import com.bytespacegames.gui.Easings;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.Interpolator;
import com.bytespacegames.gui.containers.AbstractGuiContainer;
import net.minecraft.client.gui.GuiGraphics;

public class ColorPickerElement extends AbstractGuiContainer {
    private final Animator animator;
    private final Animator hoverAnimator;
    private boolean toggled;
    private boolean expanded;
    private int r,g,b;
    public ColorPickerElement(int x, int y, int width, int height, boolean toggled, boolean visible) {
        super(x, y, width, height, visible);
        this.toggled = toggled;
        animator = new Animator(toggled ? 1 : 0);
        animator.setAnimationTime(.5f);
        animator.setEasingFunction(Easings.QUINT);
        hoverAnimator = new Animator(0);
        hoverAnimator.setAnimationTime(.5f);
        hoverAnimator.setEasingFunction(Easings.QUINT);
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
    public void render(GuiGraphics g) {
        hoverAnimator.setTarget(0);
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            hoverAnimator.setTarget(1);
        }
        g.fill(x,y,x+width,y+height, getColor());
        g.renderOutline(x,y,width,height, Interpolator.interpolateColor(ConfigGui.SECONDARY_COLOR,0xFFFFFFFF,hoverAnimator.getValue()));
        super.render(g);
    }
    public int getColor() {
        return 0xFF << 24 | r << 16 | g << 8 | b;
    }
    public boolean hoveringOrHoveringChildren() {
        if (isHovering(GuiManager.getMouseX(),GuiManager.getMouseY())) return true;
        for (AbstractGuiElement e : elements) {
            if (e.isHovering(GuiManager.getMouseX(),GuiManager.getMouseY())) return true;
        }
        return false;
    }
    @Override
    public void onClick() {
        super.onClick();
        if (!hoveringOrHoveringChildren()) clickOff();
        if (!isHovering(GuiManager.getMouseX(),GuiManager.getMouseY())) return;
        toggled = !toggled;
        if (!expanded) expanded = true;
    }

    public void clickOff() {
        super.clickOff();
        expanded = false;
    }

    public boolean getValue() {
        return toggled;
    }
    public void setValue(boolean value) {
        toggled = value;
    }
}
