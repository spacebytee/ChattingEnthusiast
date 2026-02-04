package com.bytespacegames.gui;

import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix3x2f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiManager {
    public static GuiManager INSTANCE;
    private int mouseX, mouseY;
    // when true, apply the inverse of the gui transformations (scale by chat scale, transform 4 pixels) for chat, to offset the desync between hitboxes and the actual render positions
    public boolean mouseTransformations = false;
    private boolean isScissoring = false;
    public double scale = 1;
    private boolean standardRenderCycle = true;
    private final List<AbstractGuiElement> delayedRender = new ArrayList<>();
    public GuiManager() {
        INSTANCE = this;
        mouseX = 0;
        mouseY = 0;
    }
    public void setMouseX(int mX) {
        this.mouseX = mX;
    }
    public void setMouseY(int mY) {
        this.mouseY = mY;
    }
    public static int getMouseX() {
        if (!INSTANCE.mouseTransformations) return INSTANCE.mouseX;
        return (int) (INSTANCE.mouseX / INSTANCE.scale) - 4;
    }
    public static int getMouseY() {
        if (!INSTANCE.mouseTransformations) return INSTANCE.mouseY;
        return (int) (INSTANCE.mouseY / INSTANCE.scale);
    }
    public void disableScissor(GuiGraphics g) {
        if (isScissoring)
            g.disableScissor();
        isScissoring = false;
    }
    public void enableScissor(GuiGraphics g, int x1, int y1, int x2, int y2) {
        isScissoring = true;
        g.enableScissor(x1,y1,x2,y2);
    }
    public void flushDelayedRenders(GuiGraphics g) {
        standardRenderCycle = false;
        for (AbstractGuiElement e : delayedRender) {
            e.render(g);
        }
        delayedRender.clear();
        standardRenderCycle = true;
    }

    public boolean getStandardRenderCycle() {
        return standardRenderCycle;
    }

    public void queueDelayedRender(AbstractGuiElement element) {
        delayedRender.add(element);
    }
    public void drawGradientRectangle(
            GuiGraphics g,
            int x1, int y1,
            int x2, int y2,
            int leftColor,
            int rightColor
    ) {
        int width  = x2 - x1;
        int height = y2 - y1;

        g.pose().pushMatrix();

        updatePose(g, matrix -> {
            matrix.translate(x1, y1);
            matrix.rotate((float) -Math.PI / 2f);
            matrix.translate(-height, 0);
        });

        g.fillGradient(
                0, 0,
                height, width,
                leftColor,
                rightColor
        );

        g.pose().popMatrix();
    }
    public void updatePose(GuiGraphics g, Consumer<Matrix3x2f> consumer) {
        consumer.accept(g.pose());
    }

    public void renderOutline(GuiGraphics g, int x, int y, int width, int height, int color) {
        g.renderOutline(x,y,width,height,color);
    }
}
