package com.bytespacegames.chattingenthusiast.compactchat;

import net.minecraft.client.GuiMessage;
import net.minecraft.network.chat.Component;

public class MessageTracker {
    private GuiMessage message;
    private int occurrences;
    private Component contents;
    public MessageTracker(GuiMessage message) {
        this.message = message;
        this.occurrences = 1;
    }

    public boolean isRepeat(GuiMessage message) {
        return message.content().equals(getContents());
    }
    public Component getContents() {
        return occurrences > 1 ? contents : message.content();
    }
    public GuiMessage getMessage() {
        return message;
    }
    public void setMessage(GuiMessage m) {
        this.message = m;
    }
    public void incrementOccurances(Component contents) {
        this.contents = contents;
        occurrences++;
    }
    public int getOccurrences() {
        return occurrences;
    }
}
