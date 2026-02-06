package com.bytespacegames.gui.containers;

import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.TimerUtils;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import com.bytespacegames.gui.elements.ScrollBarElement;
import net.minecraft.client.gui.GuiGraphics;

public class ScrollingContainer extends AbstractGuiContainer {
    private int scrollOffset = 0;
    private int desiredScrollOffset = 0;
    private static final int scrollBarBackgroundColor = 0xFF242424;
    private final ScrollBarElement scrollBar;
    private final TimerUtils animationTimer;
    public ScrollingContainer(int x, int y, int width, int height, int scrollBarColor, boolean visible) {
        super(x,y,width,height,visible);
        this.animationTimer = new TimerUtils();
        addElement(scrollBar = new ScrollBarElement(width, 0, height, scrollBarColor, true));
    }
    public void setScrollOffset(int pos, boolean fromScrollBar) {
        int distance = getContentsBound() - height;
        if (distance == 0) return;
        pos = Math.max(0,Math.min(pos,distance));
        desiredScrollOffset = pos;
        if (fromScrollBar)
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
        if (animationTimer.hasTimeElapsed(GuiManager.ANIMATION_INTERVAL, true)) {
            scrollOffset += (int) ((desiredScrollOffset - scrollOffset) * (1 - 1/1.3));
        }
        GuiManager.INSTANCE.enableScissor(g, x,y,x + width + 3,y + height);
        for (int i = 0; i < elements.size(); i++) {
            AbstractGuiElement e = elements.get(i);
            if (!e.isVisible()) continue;
            if (getEffectiveY(i) + e.getBottomBound() < 0) continue;
            if (getEffectiveY(i) >= getBottomBound()) continue;
            int effectiveX = this.x + getEffectiveX(i);
            int effectiveY = this.y + getEffectiveY(i);
            e.setAbsolutePosition(effectiveX,effectiveY);
            e.render(g);
        }
        GuiManager.INSTANCE.disableScissor(g);
    }
    public void mouseScrolled(double d, double e, double f, double g) {
        setScrollOffset((int) (scrollOffset - g * 100));
    }
}
