package com.bytespacegames.chattingenthusiast.gui.elements;

import com.bytespacegames.chattingenthusiast.ChatFilter;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.gui.elements.AbstractGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ARGB;

public class TabElement extends AbstractGuiElement {
    private final String text;
    private final ChatFilter.TabFilter filter;
    public TabElement(String text, ChatFilter.TabFilter filter, int x, int y, boolean visible) {
        super(x, y, 0, 11, visible);
        this.text = text;
        this.filter = filter;
    }

    @Override
    public void render(GuiManager g) {
        if (width == 0) setWidth(Minecraft.getInstance().font.width(text) + 4);
        float opacity = Minecraft.getInstance().options.textBackgroundOpacity().get().floatValue();
        int color = isHovering(GuiManager.getMouseX(),GuiManager.getMouseY()) ? ARGB.colorFromFloat(opacity, 1f, 1f, 1f) : ARGB.colorFromFloat(opacity, 0f,0f,0f);
        g.fill(x,y,x+width,y+height,color);
        int textColor = filter.equals(ChattingEnthusiast.filter().getFilter()) ? 0xFFFFFFFF : 0xFFA0A0A0;
        g.drawString(Minecraft.getInstance().font, text, x+2,y+2,textColor);
    }

    @Override
    public void onClick() {
        ChattingEnthusiast.filter().setFilter(filter);
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("switchchannels")) return;
        switch (filter) {
            case ChatFilter.TabFilter.NONE:
                if (ChattingSettingsManager.INSTANCE.getSettingToggledById("alltabchannel"))
                    mc.player.connection.sendCommand("chat a");
                break;
            case ChatFilter.TabFilter.PARTY:
                mc.player.connection.sendCommand("chat p");
                break;
            case ChatFilter.TabFilter.GUILD:
                mc.player.connection.sendCommand("chat g");
                break;
        }
    }
}
