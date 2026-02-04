package com.bytespacegames.gui.elements.colorpicker;

import com.bytespacegames.config.gui.ConfigGui;
import com.bytespacegames.gui.Animator;
import com.bytespacegames.gui.Easings;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.Interpolator;
import com.bytespacegames.gui.containers.AbstractGuiContainer;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import com.bytespacegames.gui.elements.InputFieldElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;

public class ColorPickerElement extends AbstractGuiContainer {
    private final Animator expandAnimator;
    private final Animator hoverAnimator;
    public int r,g,b;
    private final int expandedWidth = 140;
    private final int expandedHeight = 4 + 20 * 4;
    private final ColorChannelSlider rS;
    private final ColorChannelSlider gS;
    private final ColorChannelSlider bS;
    private final InputFieldElement inputField;
    public ColorPickerElement(int x, int y, int width, int height, int defaultColor, boolean visible) {
        super(x, y, width, height, visible);
        setColor(defaultColor);
        expanded = false;
        expandAnimator = new Animator(0);
        expandAnimator.setAnimationTime(.5f);
        expandAnimator.setEasingFunction(Easings.QUINT);
        hoverAnimator = new Animator(0);
        hoverAnimator.setAnimationTime(.5f);
        hoverAnimator.setEasingFunction(Easings.QUINT);
        elements.add(rS = new ColorChannelSlider(this, width - expandedWidth + 4, 4, expandedWidth - 8, 16, 0, false));
        elements.add(gS = new ColorChannelSlider(this, width - expandedWidth + 4, 4 * 2 + 16, expandedWidth - 8, 16, 1, false));
        elements.add(bS = new ColorChannelSlider(this, width - expandedWidth + 4, 4 * 3 + 16 * 2, expandedWidth - 8, 16, 2, false));
        elements.add(inputField = new InputFieldElement("#FFFFFF","#", "", 6, width - expandedWidth + 4, 4 * 4 + 16 * 3, 81,16,true));
        inputField.applyHexRestrictions = true;
        updateColor(getColor());
    }
    public void setColor(int color) {
        r = (color >> 16) & 0xFF;
        g = (color >> 8) & 0xFF;
        b = color & 0xFF;
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
        if (GuiManager.INSTANCE.getStandardRenderCycle() && expandAnimator.getValue() >= (1/1000f)) {
            GuiManager.INSTANCE.queueDelayedRender(this);
            return;
        }
        hoverAnimator.setTarget(0);
        rS.setVisible(false);
        gS.setVisible(false);
        bS.setVisible(false);
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            hoverAnimator.setTarget(1);
        }

        g.fill(x,y,x+width,y+height, getColor());
        GuiManager.INSTANCE.renderOutline(g,x,y,width,height, Interpolator.interpolateColor(constructColor(r + (127-r) / 2,this.g + (127-this.g) / 2,b + (127-b) / 2),0xFFFFFFFF,hoverAnimator.getValue()));
        expandAnimator.setTarget(expanded ? 1 : 0);
        if (!expanded && expandAnimator.getValue() < (1/1000f)) {
            super.render(g);
            return;
        }
        rS.setVisible(true);
        gS.setVisible(true);
        bS.setVisible(true);
        g.enableScissor((int) (x + width - (expandedWidth * expandAnimator.getValue()) + .5),y,x + width, (int) (y + expandedHeight * expandAnimator.getValue() + .5));
        g.fill(x + width - expandedWidth,y,x + width, y + expandedHeight,ConfigGui.BACKGROUND_COLOR);
        super.render(g);
        g.disableScissor();
    }
    public void updateColor(int color) {
        inputField.setValue(Integer.toHexString(getColor()).substring(2).toLowerCase());
        r = color >> 16 & 0xFF;
        g = color >> 8 & 0xFF;
        b = color & 0xFF;
        rS.update();
        gS.update();
        bS.update();
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
        if (!expanded) expanded = true;
    }
    @Override
    public void charTyped(CharacterEvent characterEvent) {
        super.charTyped(characterEvent);
        updateColor(0xFF000000 | Integer.parseInt(inputField.getValue(), 16));
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        super.keyPressed(keyEvent);
        updateColor(0xFF000000 | Integer.parseInt(inputField.getValue(), 16));
    }

    public void clickOff() {
        super.clickOff();
        expanded = false;
    }
    public int constructColor(int r, int g, int b) {
        return 0xFF << 24 | r << 16 | g << 8 | b;
    }
    public int getValue() {
        return getColor();
    }
}
