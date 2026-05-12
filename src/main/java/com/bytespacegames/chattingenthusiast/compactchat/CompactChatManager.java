package com.bytespacegames.chattingenthusiast.compactchat;

import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.chattingenthusiast.utils.ChatUtil;
import net.minecraft.client.multiplayer.chat.GuiMessage;
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
        int now = Minecraft.getInstance().gui.hud.getGuiTicks();
        int maxAge = (int)(ChattingSettingsManager.INSTANCE
                .getFloatValueById("compactchattime") * 20);

        messages.removeIf(tracker ->
                now - tracker.getMessage().addedTime() > maxAge
        );
    }
    public boolean excludeFromCompactChat(Component c) {
        if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("exclusions")) return false;
        String msg = c.getString().replaceAll("§.","");
        return msg.matches("\\s*") || msg.trim().matches("-+") || msg.trim().matches("▬+");
    }
    private String getOpeningBracket() {
        int index = ChattingSettingsManager.INSTANCE.getSelectedIndexById("compactformat");
        if (index < 2) return "(";
        return "[";
    }
    private String getClosingBracket() {
        int index = ChattingSettingsManager.INSTANCE.getSelectedIndexById("compactformat");
        if (index < 2) return ")";
        return "]";
    }
    public GuiMessage compactMessage(GuiMessage m) {
        clearOld();
        if (excludeFromCompactChat(m.content())) {
            return m;
        }
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
        int index = ChattingSettingsManager.INSTANCE.getSelectedIndexById("compactformat");
        Component compactTag = Component.literal(" " + getOpeningBracket() + (index % 2 == 1 ? "x" : "") + tracker.getOccurrences() + getClosingBracket())
                .withStyle(style -> style
                        .withColor(ChattingSettingsManager.INSTANCE.getColorById("tagcolor") & 0x00FFFFFF)
                        .withBold(false)
                        .withItalic(false)
                        .withObfuscated(false)
                        .withStrikethrough(false)
                        .withUnderlined(false));
        Component modifiedComponent = m.content().copy().append(compactTag);
        GuiMessage newMessage = new GuiMessage(m.addedTime(),modifiedComponent,m.signature(),m.source(),m.tag());
        tracker.setMessage(newMessage);
        // remove the old message that shares the contents of the new one
        ((IChatComponentAccessor) Minecraft.getInstance().gui.hud.getChat()).getAllMessages().remove(oldMessage);
        ((IChatComponentAccessor) Minecraft.getInstance().gui.hud.getChat()).getTrimmedMessages().removeAll(ChatUtil.getLinesFromMessage(oldMessage));
        return newMessage;
    }
}
