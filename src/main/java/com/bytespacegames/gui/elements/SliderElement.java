package com.bytespacegames.gui.elements;

import com.bytespacegames.config.gui.ConfigGui;
import com.bytespacegames.gui.Animator;
import com.bytespacegames.gui.Easings;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.Interpolator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;

public class SliderElement extends AbstractGuiElement {
    private float value;
    private final float min;
    private final float max;
    private final Animator hoverAnimator;
    private int dragAnchor = 0;
    private boolean dragging = false;
    public SliderElement(int x, int y, int width, int height, float value, float min, float max, boolean visible) {
        super(x, y, width, height, visible);
        this.value = value;
        hoverAnimator = new Animator(0);
        hoverAnimator.setAnimationTime(.5f);
        hoverAnimator.setEasingFunction(Easings.QUINT);
        this.min = min;
        this.max = max;
    }

    @Override
    public void render(GuiGraphics g) {
        hoverAnimator.setTarget(0);
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            hoverAnimator.setTarget(1);
        }
        float displayValue = (float) Math.round(getScaledValue() * 10) / 10;
        String val = String.valueOf(displayValue);
        val = val.substring(0,Math.min(4,val.length()));
        g.drawString(Minecraft.getInstance().font,val,x - Minecraft.getInstance().font.width(val) - 2,(int) ((y + (float)height / 2) - 3.5f), 0xFFFFFFFF);
        g.fill(x,y + (height / 2),x+width,y + (height / 2) + 1, ConfigGui.SECONDARY_COLOR);
        int toggleHeight = height;
        int toggleWidth = (int) (toggleHeight * .75f);
        int togglePosition = (int) ((width-toggleWidth) * value);
        g.fill(x + togglePosition,y,x + togglePosition + toggleWidth,y + toggleHeight, Interpolator.interpolateColor(ConfigGui.HIGHLIGHT_COLOR,0xFFFFFFFF,hoverAnimator.getValue()));
    }

    public boolean isHovering(int mouseX, int mouseY) {
        int toggleHeight = height;
        int toggleWidth = (int) (toggleHeight * .75f);
        int togglePosition = (int) ((width-toggleWidth) * value);
        return mouseX >= x + togglePosition && mouseX < x + togglePosition + toggleWidth && mouseY >= y  && mouseY < y + toggleHeight;
    }

    @Override
    public void onClick() {
        int toggleHeight = height;
        int toggleWidth = (int) (toggleHeight * .75f);
        int togglePosition = (int) ((width-toggleWidth) * value);
        dragAnchor = GuiManager.getMouseX() - (x + togglePosition);
        dragging = true;
    }
    @Override
    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double i, double j) {
        int mouseX = GuiManager.getMouseX();
        if(!dragging) return;
        int toggleHeight = height;
        int toggleWidth = (int) (toggleHeight * .75f);
        int newPosition = mouseX - dragAnchor;
        value = Math.max(0f,Math.min((newPosition-x)/(float)(width-toggleWidth),1f));
    }
    @Override
    public void mouseReleased(MouseButtonEvent mouseButtonEvent) {
        dragging = false;
    }
    public float getValue() {
        return value;
    }
    public float getScaledValue() {
        return min + value * (max-min);
    }
    public void setValue(float value) {
        this.value = value;
    }
}
