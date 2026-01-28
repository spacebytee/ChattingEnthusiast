package com.bytespacegames.gui.containers;

import net.minecraft.client.gui.GuiGraphics;

public class BottomRightOriginatingContainer extends AbstractGuiContainer {
    protected int spacing;
    public BottomRightOriginatingContainer(int x, int y, int spacing, boolean visible) {
        super(x, y, 0, 0, visible);
        this.spacing = spacing;
    }

    @Override
    public int getEffectiveX(int elementIndex) {
        int elementX = 0;
        for (int i = 0; i <= elementIndex; i++) {
            if (!elements.get(i).isVisible()) continue;
            elementX -= elements.get(i).getWidth();
            if (i > 0) elementX -= spacing;
        }
        return elementX + 1;
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
