package com.bytespacegames.chattingenthusiast.gui.containers;

import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

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
