package com.bytespacegames.chattingenthusiast.gui.containers;

import net.minecraft.client.gui.GuiGraphics;

public class TopLeftOriginatingContainer extends AbstractGuiContainer {
    protected int spacing;
    public TopLeftOriginatingContainer(int x, int y, int spacing, boolean visible) {
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
        for (int i = 1; i <= elementIndex; i++) {
            if (!elements.get(i).isVisible()) continue;
            elementY += elements.get(i).getHeight() + spacing;
        }
        return elementY;
    }
    public void render(GuiGraphics g) {
        super.render(g);
    }
}
