package com.bytespacegames.gui.elements;

public abstract class AbstractExpandableElement extends AbstractGuiElement {
    protected boolean expanded;
    public AbstractExpandableElement(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, visible);
        expanded = false;
    }
    public boolean getExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
