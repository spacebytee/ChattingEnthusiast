package com.bytespacegames.gui.containers;

import com.bytespacegames.gui.GuiManager;

public class BasicContainer extends AbstractGuiContainer {
    public final int color;
    public BasicContainer(int x, int y, int width, int height, int color, boolean visible) {
        super(x, y, width, height, visible);
        this.color = color;
    }
    public BasicContainer(int x, int y, int width, int height, boolean visible) {
        this(x, y, width, height, 0x00000000, visible);
    }
    public BasicContainer(int width, int height, boolean visible) {
        this(0,0,width,height,visible);
    }

    // basic container positions should just position the elements in their existing x,y positions (relative to container)
    @Override
    public int getEffectiveX(int elementIndex) {
        return elements.get(elementIndex).getX();
    }

    @Override
    public int getEffectiveY(int elementIndex) {
        return elements.get(elementIndex).getY();
    }

    @Override
    public int getBottomBound() {
        return height;
    }

    public void render(GuiManager gui) {
        GuiManager.INSTANCE.fill(x,y,x + width,y + height,color);
        super.render(gui);
    }
}
