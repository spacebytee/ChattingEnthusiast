package com.bytespacegames.gui;

import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiManager {
    public static GuiManager INSTANCE;
    public static final int ANIMATION_INTERVAL = 1000/60;

    private int mouseX, mouseY;
    // when true, apply the inverse of the gui transformations (scale by chat scale, transform 4 pixels) for chat, to offset the desync between hitboxes and the actual render positions
    public boolean mouseTransformations = false;
    public double scale = 1;
    private boolean standardRenderCycle = true;
    private GuiGraphicsExtractor graphics;
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
    public void disableScissor() {
        graphics.disableScissor();
    }
    public void enableScissor(int x1, int y1, int x2, int y2) {
        graphics.enableScissor(x1,y1,x2,y2);
    }
    public void updateGuiGraphics(GuiGraphicsExtractor g) {
        this.graphics = g;
    }
    public void flushDelayedRenders() {
        standardRenderCycle = false;
        for (AbstractGuiElement e : delayedRender) {
            e.render(this);
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
            int x1, int y1,
            int x2, int y2,
            int leftColor,
            int rightColor
    ) {
        int width  = x2 - x1;
        int height = y2 - y1;

        graphics.pose().pushMatrix();

        updatePose(matrix -> {
            matrix.translate(x1, y1);
            matrix.rotate((float) -Math.PI / 2f);
            matrix.translate(-height, 0);
        });

        graphics.fillGradient(
                0, 0,
                height, width,
                leftColor,
                rightColor
        );

        graphics.pose().popMatrix();
    }
    public void updatePose(Consumer<Matrix3x2f> consumer) {
        consumer.accept(graphics.pose());
    }

    public void renderOutline(int x, int y, int width, int height, int color) {
        graphics.outline(x,y,width,height,color);
    }

    public void drawString(Font f, String string, int x, int y, int color) {
        graphics.text(f,string,x,y,color);
    }

    public void fill(int x, int y, int x2, int y2, int color) {
        graphics.fill(x,y,x2,y2,color);
    }

    public void drawCenteredString(Font font, String text, int x, int y, int color) {
        drawString(font,text,x-font.width(text)/2,y,color);
    }

    public GuiGraphicsExtractor getGuiGraphics() {
        return graphics;
    }
}
