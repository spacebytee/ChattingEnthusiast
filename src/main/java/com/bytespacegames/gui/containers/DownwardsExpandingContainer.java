package com.bytespacegames.gui.containers;

import net.minecraft.client.gui.GuiGraphics;

public class DownwardsExpandingContainer extends AbstractGuiContainer {
    protected int spacing;
    public DownwardsExpandingContainer(int x, int y, int spacing, boolean visible) {
        super(x, y, 0, 0, visible);
        this.spacing = spacing;
    }

    @Override
    public int getEffectiveX(int elementIndex) {
        return elements.get(elementIndex).getX();
    }

    @Override
    public int getEffectiveY(int elementIndex) {
        int elementY = 0;
        for (int i = 0; i < elementIndex; i++) {
            if (!elements.get(i).isVisible()) continue;
            elementY += elements.get(i).getBottomBound() + spacing;
        }
        return elementY;
    }
    @Override
    public int getBottomBound() {
        int elementY = 0;
        int elementIndex = elements.size() - 1;
        for (int i = 0; i < elementIndex; i++) {
            if (!elements.get(i).isVisible()) continue;
            elementY += elements.get(i).getBottomBound() + spacing;
        }
        return elementY + elements.get(elementIndex).getBottomBound();
    }
    public void render(GuiGraphics g) {
        super.render(g);
    }
}
