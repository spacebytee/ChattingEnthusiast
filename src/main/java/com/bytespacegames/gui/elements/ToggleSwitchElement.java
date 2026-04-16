package com.bytespacegames.gui.elements;

import com.bytespacegames.config.gui.ConfigGui;
import com.bytespacegames.gui.Animator;
import com.bytespacegames.gui.Easings;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.Interpolator;

public class ToggleSwitchElement extends AbstractGuiElement {
    private final Animator animator;
    private final Animator hoverAnimator;
    private boolean toggled;
    public ToggleSwitchElement(int x, int y, int width, int height, boolean toggled, boolean visible) {
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
    public void render(GuiManager gui) {
        hoverAnimator.setTarget(0);
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            hoverAnimator.setTarget(1);
        }
        gui.fill(x,y,x+width,y+height, Interpolator.interpolateColor(0x00B643DA,ConfigGui.HIGHLIGHT_COLOR, animator.getValue()));
        gui.renderOutline(x,y,width,height, Interpolator.interpolateColor(ConfigGui.SECONDARY_COLOR,0xFFFFFFFF,hoverAnimator.getValue()));
        int toggleHeight = height - 4;
        int toggleWidth = (int) (toggleHeight * .75f);
        int toggleOffset = (int) ((width - 4 - toggleWidth) * animator.getValue() + .5f);
        gui.fill(x + 2 + toggleOffset,y + 2,x + 2 + toggleWidth + toggleOffset,y + 2 + toggleHeight, Interpolator.interpolateColor(ConfigGui.SECONDARY_COLOR,0xFFFFFFFF,animator.getValue()));
    }

    @Override
    public void onClick() {
        toggled = !toggled;
        animator.setTarget(toggled ? 1 : 0);
    }

    public boolean getValue() {
        return toggled;
    }
    public void setValue(boolean value) {
        toggled = value;
    }
}
