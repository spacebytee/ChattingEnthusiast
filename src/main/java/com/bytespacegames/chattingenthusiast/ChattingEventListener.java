package com.bytespacegames.chattingenthusiast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.MouseButtonEvent;

public class ChattingEventListener implements GuiEventListener {
    public void setFocused(boolean bl) {

    }

    public boolean isFocused() {
        return false;
    }
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {
        if (Minecraft.getInstance().screen == null || !(Minecraft.getInstance().screen instanceof ChatScreen)) return false;
        ChattingEnthusiast.chatting.mouseDragged(mouseButtonEvent,d,e);
        return false;
    }
    public boolean charTyped(CharacterEvent characterEvent) {
        System.out.println("type");
        if (Minecraft.getInstance().screen == null || !(Minecraft.getInstance().screen instanceof ChatScreen)) return false;
        ChattingEnthusiast.chatting.charTyped(characterEvent);
        return false;
    }
}
