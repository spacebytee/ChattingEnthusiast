package com.bytespacegames.chattingenthusiast.config.gui;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.config.SettingsCategory;
import com.bytespacegames.chattingenthusiast.gui.Animator;
import com.bytespacegames.chattingenthusiast.gui.Easings;
import com.bytespacegames.chattingenthusiast.gui.Interpolator;
import com.bytespacegames.chattingenthusiast.gui.elements.TextElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class CategoryButton extends TextElement {
    private SettingsCategory category;
    private ConfigGui gui;
    private Animator animator;
    public CategoryButton(ConfigGui config, SettingsCategory category, int x, int y, boolean visible) {
        super(category.getName(), x, y, 0xFFFFFFFF, visible);
        this.gui = config;
        this.category = category;
        this.width = 100;
        animator = new Animator(0);
        animator.setAnimationTime(.5f);
        animator.setEasingFunction(Easings.QUINT);
    }

    public void render(GuiGraphics g) {
        if (category == gui.selectedCategory) {
            //g.drawString(Minecraft.getInstance().font, getText(), x,y,0xFFB643DA);
            animator.setTarget(1f);
        }
        else if (isHovering(ChattingEnthusiast.chatting().getMouseX(),ChattingEnthusiast.chatting().getMouseY())) {
            animator.setTarget(.5f);
        } else {
            animator.setTarget(0);
        }

        int color = Interpolator.interpolateColor(getColor(),0xFF00CCFF,animator.getValue() * 2);
        if (animator.getValue() > .5) {
            color = Interpolator.interpolateColor(0xFF00CCFF,0xFFB643DA,(animator.getValue() - .5f) * 2);;
        }
        g.drawString(Minecraft.getInstance().font, getText(), x,y, color);
    }
    public void onClick() {
        gui.selectedCategory = category;
    }
}
