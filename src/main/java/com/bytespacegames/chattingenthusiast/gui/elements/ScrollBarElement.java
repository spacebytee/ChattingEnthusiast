package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.gui.GuiManager;
import com.bytespacegames.chattingenthusiast.gui.containers.ScrollingContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;

public class ScrollBarElement extends AbstractGuiElement {
    private final int scrollBarColor;
    private boolean dragging = false;
    private int scrollBarPosition = 0;
    private int dragAnchor = 0;
    public ScrollBarElement(int x, int y, int height, int scrollBarColor, boolean visible) {
        super(x, y, 3, height, visible);
        this.scrollBarColor = scrollBarColor;
    }
    private int highlightHeight() {
        return (int) (height * ((float) parent.getHeight() / ((ScrollingContainer)parent).getContentsBound()));
    }
    private void updateParent() {
        ScrollingContainer container = (ScrollingContainer) parent;
        float scrollAmount = (scrollBarPosition/(float) (getHeight() - highlightHeight()));
        int totalScrollablePixels = (container.getContentsBound() - parent.getHeight());
        int offset = (int) (totalScrollablePixels * scrollAmount);
        System.out.println(totalScrollablePixels + " | " + offset + " | " + scrollAmount);
        System.out.println(container.getBottomBound() + " | " + parent.getHeight());
        container.setScrollOffset(offset);
    }
    @Override
    public void render(GuiGraphics g) {
        if (isHovering(GuiManager.getMouseX(), GuiManager.getMouseY())) {
            g.fill(x,y+scrollBarPosition,x + 3, y + scrollBarPosition + highlightHeight(), 0xFFFFFFFF);
            return;
        }
        g.fill(x,y+scrollBarPosition,x + 3, y + scrollBarPosition + highlightHeight(), scrollBarColor);
    }
    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y + scrollBarPosition  && mouseY < y + scrollBarPosition + highlightHeight();
    }

    @Override
    public void onClick() {
        ChattingComponent c = ChattingEnthusiast.chatting();
        dragAnchor = c.getMouseY() - (y + scrollBarPosition);
        dragging = true;
    }

    @Override
    public void mouseDragged(MouseButtonEvent mouseButtonEvent, double i, double j) {
        int y = ChattingEnthusiast.chatting().getMouseY();
        if(!dragging) return;
        scrollBarPosition = Math.max(0,Math.min(height-highlightHeight(), y - this.y - dragAnchor));
        updateParent();
    }
    @Override
    public void mouseReleased(MouseButtonEvent mouseButtonEvent) {
        dragging = false;
    }
}
