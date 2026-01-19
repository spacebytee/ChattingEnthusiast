package com.bytespacegames.chattingenthusiast.gui.containers;

import com.bytespacegames.chattingenthusiast.gui.elements.AbstractGuiElement;
import com.bytespacegames.chattingenthusiast.gui.elements.ScrollBarElement;
import net.minecraft.client.gui.GuiGraphics;

public class ScrollingContainer extends AbstractGuiContainer {
    private int scrollOffset = 0;
    private final int scrollBarBackgroundColor = 0xFF242424;
    public ScrollingContainer(int x, int y, int width, int height, int scrollBarColor, boolean visible) {
        super(x,y,width,height,visible);
        addElement(new ScrollBarElement(width, 0, height, scrollBarColor, true));
    }
    public void setScrollOffset(int pos) {
        this.scrollOffset = pos;
    }

    // basic container positions should just position the elements in their existing x,y positions (relative to container)
    @Override
    public int getEffectiveX(int elementIndex) {
        return elements.get(elementIndex).getX();
    }

    @Override
    public int getEffectiveY(int elementIndex) {
        if (elements.get(elementIndex) instanceof ScrollBarElement) {
            return elements.get(elementIndex).getY();
        }
        return elements.get(elementIndex).getY() - scrollOffset;
    }
    @Override
    public int getBottomBound() {
        return height;
    }
    public int getContentsBound() {
        int highest = 0;
        for (int i = 0; i < getElements().size(); i++) {
            AbstractGuiElement element = elements.get(i);
            int y = element.getY() + element.getBottomBound();
            if (y > highest) highest = y;
        }
        return Math.max(highest,height);
    }
    public void render(GuiGraphics g) {
        if (!visible) return;
        g.fill(x + width,y,x + width + 3,y + height,scrollBarBackgroundColor);
        g.enableScissor(x,y,x + width + 3,y + height);
        for (int i = 0; i < elements.size(); i++) {
            AbstractGuiElement e = elements.get(i);
            if (!e.isVisible()) continue;
            if (e.getY() >= getBottomBound()) continue;
            int effectiveX = this.x + getEffectiveX(i);
            int effectiveY = this.y + getEffectiveY(i);
            e.setAbsolutePosition(effectiveX,effectiveY);
            e.render(g);
        }
        g.disableScissor();
    }
}
