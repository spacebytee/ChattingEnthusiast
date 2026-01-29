package com.bytespacegames.chattingenthusiast.compactchat;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class CompactChatManager {
    private final List<MessageTracker> messages;
    public CompactChatManager() {
        messages = new ArrayList<>();
    }
    private void clearOld() {
        int latestValid = messages.size();
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (Minecraft.getInstance().gui.getGuiTicks() - messages.get(i).getMessage().addedTime() > ChattingEnthusiast.COMPACT_CHAT_MEMORY_TICKS) {
                break;
            }
            latestValid = i;
        }
        messages.subList(0,latestValid).clear();
    }
    public GuiMessage compactMessage(GuiMessage m) {
        clearOld();
        MessageTracker tracker = null;
        for (int i = messages.size() - 1; i >= 0; i--) {
            tracker = messages.get(i);
            if (tracker.isRepeat(m)) {
                break;
            } else {
                tracker = null;
            }
        }
        if (tracker == null) {
            MessageTracker newTracker = new MessageTracker(m);
            messages.add(newTracker);
            return m;
        }
        GuiMessage oldMessage = tracker.getMessage();
        tracker.incrementOccurances(m.content());
        Component compactTag = Component.literal(" (" + tracker.getOccurrences() + ")")
                .withStyle(ChatFormatting.GRAY);
        Component modifiedComponent = m.content().copy().append(compactTag);
        GuiMessage newMessage = new GuiMessage(m.addedTime(),modifiedComponent,m.signature(),m.tag());
        tracker.setMessage(newMessage);
        // remove the old message that shares the contents of the new one
        ((IChatComponentAccessor) Minecraft.getInstance().gui.getChat()).getAllMessages().remove(oldMessage);
        ((IChatComponentAccessor) Minecraft.getInstance().gui.getChat()).mixin$refreshTrimmedMessages();
        return newMessage;
    }
}
