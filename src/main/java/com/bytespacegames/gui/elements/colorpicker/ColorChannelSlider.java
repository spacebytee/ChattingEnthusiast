package com.bytespacegames.gui.elements.colorpicker;

import com.bytespacegames.gui.Animator;
import com.bytespacegames.gui.Easings;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.Interpolator;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;

public class ColorChannelSlider extends AbstractGuiElement {
    private float value;
    private final Animator hoverAnimator;
    private int dragAnchor = 0;
    private boolean dragging = false;
    private final int channel;
    private final ColorPickerElement picker;
    public ColorChannelSlider(ColorPickerElement picker, int x, int y, int width, int height, int channel, boolean visible) {
        super(x, y, width, height, visible);
        this.channel = channel;
        this.picker = picker;
        this.value = (channel == 0 ? picker.r : (channel == 1 ? picker.g : picker.b))/255f;
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
        //g.drawString(Minecraft.getInstance().font,val,x - Minecraft.getInstance().font.width(val) - 2,(int) ((y + (float)height / 2) - 3.5f), 0xFFFFFFFF);
        GuiManager.INSTANCE.drawGradientRectangle(g,x,y,x+width,y + height, getColor(0), getColor(255));
        int toggleHeight = height;
        int togglePosition = (int) ((width-toggleHeight) * value);
        g.fill(x + togglePosition,y,x + togglePosition + toggleHeight,y + toggleHeight, getColor(getScaledValue()));

        GuiManager.INSTANCE.renderOutline(g,x + togglePosition,y,toggleHeight,toggleHeight,
                Interpolator.interpolateColor(
                        picker.constructColor(picker.r + (127-picker.r) / 2,picker.g + (127-picker.g) / 2,picker.b + (127-picker.b) / 2),
                        0xFFFFFFFF,hoverAnimator.getValue()));
    }

    public boolean isHovering(int mouseX, int mouseY) {
        int toggleHeight = height;
        int togglePosition = (int) ((width- toggleHeight) * value);
        return mouseX >= x + togglePosition && mouseX < x + togglePosition + toggleHeight && mouseY >= y  && mouseY < y + toggleHeight;
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
        postValue();
    }
    @Override
    public void mouseReleased(MouseButtonEvent mouseButtonEvent) {
        dragging = false;
    }
    public float getValue() {
        return value;
    }
    public void postValue() {
        switch (channel) {
            case 0:
                picker.r = getScaledValue();
                break;
            case 1:
                picker.g = getScaledValue();
                break;
            case 2:
                picker.b = getScaledValue();
                break;
        }
        picker.updateColor(picker.getColor());
    }
    public void update() {
        value = (channel == 0 ? picker.r : (channel == 1 ? picker.g : picker.b))/255f;
    }
    public int getScaledValue() {
        return (int) (value * (255));
    }
    public int getColor(int channelValue) {
        return 0xFF << 24 | (channel == 0 ? channelValue : picker.r) << 16 | (channel == 1 ? channelValue : picker.g) << 8 | (channel == 2 ? channelValue : picker.b);
    }
}
