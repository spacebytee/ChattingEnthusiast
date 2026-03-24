package com.bytespacegames.config.gui;

import com.bytespacegames.config.SettingsCategory;
import com.bytespacegames.gui.Animator;
import com.bytespacegames.gui.Easings;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.Interpolator;
import com.bytespacegames.gui.elements.TextElement;
import net.minecraft.client.Minecraft;

public class CategoryButton extends TextElement {
    private final SettingsCategory category;
    private final ConfigGui gui;
    private final Animator animator;
    public CategoryButton(ConfigGui config, SettingsCategory category, int x, int y, boolean visible) {
        super(category.getName(), x, y, 0xFFFFFFFF, visible);
        this.gui = config;
        this.category = category;
        this.width = 100;
        animator = new Animator(0);
        animator.setAnimationTime(.5f);
        animator.setEasingFunction(Easings.QUINT);
    }

    public void render() {
        if (category == gui.selectedCategory) {
            //g.drawString(Minecraft.getInstance().font, getText(), x,y,0xFFB643DA);
            animator.setTarget(1f);
        }
        else if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            animator.setTarget(.5f);
        } else {
            animator.setTarget(0);
        }

        int color = Interpolator.interpolateColor(getColor(),0xFF00CCFF,animator.getValue() * 2);
        if (animator.getValue() > .5) {
            color = Interpolator.interpolateColor(0xFF00CCFF,ConfigGui.HIGHLIGHT_COLOR,(animator.getValue() - .5f) * 2);
        }
        GuiManager.INSTANCE.drawString(Minecraft.getInstance().font, getText(), x,y, color);
    }
    public void onClick() {
        gui.setSelectedCategory(category);
    }
}
