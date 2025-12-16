package com.bytespacegames.chattingenthusiast.gui.containers;

public class BasicContainer extends AbstractGuiContainer {
    public BasicContainer(int width, int height, boolean visible) {
        super(0, 0, width, height, visible);
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
}
