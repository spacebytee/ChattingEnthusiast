package com.bytespacegames.gui.elements;

import com.bytespacegames.config.gui.ConfigGui;
import com.bytespacegames.gui.Animator;
import com.bytespacegames.gui.Easings;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.Interpolator;
import net.minecraft.client.Minecraft;

public class DropdownElement extends AbstractExpandableElement {
    private String value;
    private final String[] options;
    private final Animator[] animators;
    Animator expandAnimator;
    public DropdownElement(int x, int y, int width, int height, boolean visible, String defaultValue, String... options) {
        super(x, y, width, height, visible);
        animators = new Animator[options.length];
        for (int i = 0; i < options.length; i++) {
            Animator hoverAnimator = new Animator(0);
            hoverAnimator.setAnimationTime(.5f);
            hoverAnimator.setEasingFunction(Easings.QUINT);
            animators[i] = hoverAnimator;
        }
        expandAnimator = new Animator(0);
        expandAnimator.setAnimationTime(.5f);
        expandAnimator.setEasingFunction(Easings.QUINT);
        this.value = defaultValue;
        this.options = options;
    }

    @Override
    public void render(GuiManager gui) {
        if (gui.getStandardRenderCycle() && expandAnimator.getValue() >= (1/1000f)) {
            gui.queueDelayedRender(this);
            return;
        }
        for (Animator animator : animators) {
            animator.setTarget(0);
        }
        int hovered = getHoveredIndex(GuiManager.getMouseX(), GuiManager.getMouseY());
        if (hovered != -1 && hovered < animators.length) {
            animators[hovered].setTarget(1);
        }
        expandAnimator.setTarget(expanded ? 1 : 0);
        // use vanilla scissor to not save state of the temporary scissor. scroll container scissor will be recovered
        if (expandAnimator.getValue() >= (1/1000f)) {
            gui.enableScissor(x,y,x+width, y + getEffectiveHeight());
        }

        gui.fill(x,y,x + width,y + getEffectiveHeight(), ConfigGui.BACKGROUND_COLOR);
        for (int i = 0; i < animators.length; i++) {
            if (animators[i].getValue() <= 1/256f) continue;
            gui.fill(x,y + height * i,x + width,y + height * (i + 1), Interpolator.interpolateColor(ConfigGui.BACKGROUND_COLOR,ConfigGui.SECONDARY_COLOR,animators[i].getValue()));
        }

        gui.drawString(Minecraft.getInstance().font, value,x + 4,y + (height/2) - 3, ConfigGui.HIGHLIGHT_COLOR);
        if (expandAnimator.getValue() >= (1/1000f)) {
            int i = 0;
            for (String string : options) {
                if (string.equals(value)) continue;
                i++;
                gui.drawString(Minecraft.getInstance().font, string,x + 4,y + (height/2) - 3 + height * (i), Interpolator.interpolateColor(ConfigGui.SECONDARY_COLOR,0xFFFFFFFF, animators[i].getValue()));
            }
        }
        if (expandAnimator.getValue() >= (1/1000f)) {
            gui.disableScissor();
        }
        //g.renderOutline(x,y,width,getEffectiveHeight(), Interpolator.interpolateColor(ConfigGui.SECONDARY_COLOR,0xFFFFFFFF,generalHoverAnimator.getValue()));
    }
    public int getEffectiveHeight() {
        return (int) (height + (height * (options.length - 1)) * expandAnimator.getValue() + .5);
    }
    public boolean isHovering(int mouseX, int mouseY) {
        int effectiveHeight = getEffectiveHeight();
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + effectiveHeight;
    }

    private int getHoveredIndex(int mouseX, int mouseY) {
        if (mouseY < y) return -1;
        if (!isHovering(mouseX,mouseY)) return -1;
        if (mouseY > y + getEffectiveHeight()) return -1;
        return (mouseY-(y+1))/height;
    }

    @Override
    public void onClick() {
        if (expanded) {
            int hovered = getHoveredIndex(GuiManager.getMouseX(),GuiManager.getMouseY());
            if (hovered <= 0) {
                expanded = false;
                return;
            }
            if (hovered > options.length) {
                expanded = false;
                return;
            }
            hovered--;
            int effectiveIndex = 0;
            for (String string : options) {
                if (string.equals(value)) continue;
                if (effectiveIndex == hovered) {
                    value = string;
                    break;
                }
                effectiveIndex++;
            }
        }
        expanded = !expanded;
    }
    public void clickOff() {
        expanded = false;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
