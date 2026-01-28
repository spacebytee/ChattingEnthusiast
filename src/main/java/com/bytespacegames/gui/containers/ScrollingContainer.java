package com.bytespacegames.gui.containers;

import com.bytespacegames.gui.elements.AbstractGuiElement;
import com.bytespacegames.gui.elements.ScrollBarElement;
import net.minecraft.client.gui.GuiGraphics;

public class ScrollingContainer extends AbstractGuiContainer {
    private int scrollOffset = 0;
    private static final int scrollBarBackgroundColor = 0xFF242424;
    private final ScrollBarElement scrollBar;
    public ScrollingContainer(int x, int y, int width, int height, int scrollBarColor, boolean visible) {
        super(x,y,width,height,visible);
        addElement(scrollBar = new ScrollBarElement(width, 0, height, scrollBarColor, true));
    }
    public void setScrollOffset(int pos, boolean fromScrollBar) {
        int distance = getContentsBound() - height;
        if (distance == 0) return;
        pos = Math.min(pos,distance);
        this.scrollOffset = pos;
        if (fromScrollBar) return;
        scrollBar.setScrollBarPosition(pos / (float) distance);
    }
    public void setScrollOffset(int pos) {
        setScrollOffset(pos, false);
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
