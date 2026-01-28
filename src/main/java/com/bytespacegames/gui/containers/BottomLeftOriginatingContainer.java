package com.bytespacegames.gui.containers;

import net.minecraft.client.gui.GuiGraphics;

public class BottomLeftOriginatingContainer extends AbstractGuiContainer {
    protected final int spacing;
    public BottomLeftOriginatingContainer(int x, int y, int spacing, boolean visible) {
        super(x, y, 0, 0, visible);
        this.spacing = spacing;
    }

    @Override
    public int getEffectiveX(int elementIndex) {
        int elementX = 0;
        int lWidth = 0;
        for (int i = 0; i <= elementIndex; i++) {
            if (!elements.get(i).isVisible()) continue;
            lWidth = elements.get(i).getWidth();
            elementX += lWidth;
            if (i > 0) elementX += spacing;
        }
        elementX -= lWidth;
        return elementX;
    }

    @Override
    public int getEffectiveY(int elementIndex) {
        return -elements.get(elementIndex).getHeight() + 1;
    }
    @Override
    public int getBottomBound() {
        return 0;
    }
    public void render(GuiGraphics g) {
        super.render(g);
    }
}
