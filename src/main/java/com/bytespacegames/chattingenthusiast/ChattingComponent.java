package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.chattingenthusiast.gui.AbstractGuiElement;
import com.bytespacegames.chattingenthusiast.gui.CopyElement;
import com.bytespacegames.chattingenthusiast.gui.DeleteElement;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class ChattingComponent {
    private int mouseX,mouseY;
    private Minecraft mc;
    public int renderOffsetX = 0;
    public int renderOffsetY = 0;
    public List<AbstractGuiElement> elements;
    CopyElement copy;
    DeleteElement delete;
    public ChattingComponent() {
        this.mc = Minecraft.getInstance();
        elements = new ArrayList<AbstractGuiElement>();
        copy = new CopyElement(0,0,0,0, false);
        delete = new DeleteElement(0,0,0,0, false);
        elements.add(copy);
        elements.add(delete);
    }
    public void renderCustomLine(GuiGraphics graphics, int x, int mx, int nx, int lineIndex, float opacity) {
        IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
        int scaleOffset = Mth.ceil(cca.mixin$getWidth() / mc.options.chatScale().get());
        float baseBackgroundOpacity = mc.options.textBackgroundOpacity().get().floatValue();
        // lineIndex is relative to the bottom chat message as scrolled, so we need to account for scroll position
        int hoveredIndex = ChattingEnthusiast.INSTANCE.getHoveredMessage(mouseX,mouseY) - cca.getChatScrollbarPos();
        int color = lineIndex == hoveredIndex ? 0xFFFFFFFF : 0xFF000000;
        graphics.fill(x - 4, mx, x + scaleOffset + 4 + 4, nx, ARGB.color(opacity * baseBackgroundOpacity, color));
        if (lineIndex != hoveredIndex) return;
        copy.setConfig(x + scaleOffset + 8 + 5, mx, cca.mixin$getLineHeight(), cca.mixin$getLineHeight(), true);
        copy.setMessage(lineIndex);
        delete.setConfig(x + scaleOffset + 8 + 6 + cca.mixin$getLineHeight(), mx, cca.mixin$getLineHeight(), cca.mixin$getLineHeight(), true);
        delete.setMessage(lineIndex);
        //graphics.fill(x + scaleOffset + 4 + 5, mx, x + scaleOffset + 4 + 5 + cca.mixin$getLineHeight(), mx + cca.mixin$getLineHeight(), ARGB.color(opacity * baseBackgroundOpacity, 0xFF000000));
        //graphics.fill(x + scaleOffset + 4 + 6 + cca.mixin$getLineHeight(), mx, x + scaleOffset + 4 + 6 + cca.mixin$getLineHeight() * 2, mx + cca.mixin$getLineHeight(), ARGB.color(opacity * baseBackgroundOpacity, 0xFF000000));

    }
    public void updateMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }
    public int getMouseX() {
        return mouseX;
    }
    public int getMouseY() {
        return mouseY;
    }

    public void render(GuiGraphics g) {
        int hovered = ChattingEnthusiast.INSTANCE.getHoveredMessage(mouseX,mouseY);
        if (hovered == -1) {
            copy.setVisible(false);
            delete.setVisible(false);
        }
        for (AbstractGuiElement e : elements) {
            if (!e.isVisible()) continue;
            e.render(g);
        }
    }

    public void onClick() {
        for (AbstractGuiElement e : elements) {
            if (!e.isVisible()) continue;
            if (!e.isHovering(mouseX,mouseY)) continue;
            e.onClick();
        }
    }
}
