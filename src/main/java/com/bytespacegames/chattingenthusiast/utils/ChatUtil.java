package com.bytespacegames.chattingenthusiast.utils;

import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class ChatUtil {
    public static GuiMessage getMessageFromLine(GuiMessage.Line line) {
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) cc;
        List<GuiMessage> msgs = cca.getAllMessages();
        return getMessageFromLine(line, msgs);
    }
    public static GuiMessage getMessageFromLine(GuiMessage.Line line, List<GuiMessage> msgs) {
        int found = -1;
        int low = 0;
        int high = msgs.size() - 1;
        int targetAddedTime = line.addedTime();
        while (high >= low) {
            int mid = (low + high) >>> 1;
            int time = msgs.get(mid).addedTime();

            if (time < targetAddedTime) {
                high = mid - 1;
            }
            else if (time > targetAddedTime) {
                low = mid + 1;
            }
            else {
                found = mid;
                break;
            }
        }
        if (found == -1)
            return null;

        // move the index to the earliest message matching the time
        while (found > 0 && msgs.get(found-1).addedTime() == targetAddedTime) {
            found--;
        }
        // move older until you find a matching message
        while (found < msgs.size() && msgs.get(found).addedTime() == targetAddedTime) {
            if (msgs.get(found).tag() != line.tag()) {
                found++;
                continue;
            }
            if (msgs.get(found).content().getString().replaceAll("§.","").contains(getPlainText(line.content()).trim().replaceAll("§.",""))) {
                return msgs.get(found);
            }
            found++;
        }
        return null;
    }
    public static String getPlainText(FormattedCharSequence seq) {
        StringBuilder sb = new StringBuilder();

        seq.accept((index, style, codePoint) -> {
            sb.appendCodePoint(codePoint);
            return true;
        });

        return sb.toString();
    }
}
