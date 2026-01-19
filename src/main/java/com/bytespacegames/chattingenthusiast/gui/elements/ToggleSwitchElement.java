package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.config.gui.ConfigGui;
import com.bytespacegames.chattingenthusiast.gui.Animator;
import com.bytespacegames.chattingenthusiast.gui.Easings;
import com.bytespacegames.chattingenthusiast.gui.GuiManager;
import com.bytespacegames.chattingenthusiast.gui.Interpolator;
import net.minecraft.client.gui.GuiGraphics;

public class ToggleSwitchElement extends AbstractGuiElement {
    private Animator animator;
    private Animator hoverAnimator;
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
    public void render(GuiGraphics g) {
        hoverAnimator.setTarget(0);
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            hoverAnimator.setTarget(1);
        }
        g.fill(x,y,x+width,y+height, Interpolator.interpolateColor(0x00B643DA,0xFFB643DA, animator.getValue()));
        g.renderOutline(x,y,width,height, ConfigGui.SECONDARY_COLOR);
        int toggleHeight = height - 4;
        int toggleWidth = (int) (toggleHeight * .75f);
        int toggleOffset = (int) ((width - 4 - toggleWidth) * animator.getValue());
        g.fill(x + 2 + toggleOffset,y + 2,x + 2 + toggleWidth + toggleOffset,y + 2 + toggleHeight, Interpolator.interpolateColor(ConfigGui.SECONDARY_COLOR,0xFFFFFFFF,hoverAnimator.getValue()));
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
