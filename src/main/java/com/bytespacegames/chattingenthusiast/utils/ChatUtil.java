package com.bytespacegames.chattingenthusiast.utils;

import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.FormattedCharSequence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatUtil {
    public static GuiMessage getMessageFromLine(GuiMessage.Line line) {
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) cc;
        List<GuiMessage> msgs = cca.getAllMessages();
        return getMessageFromLine(line, msgs);
    }
    public static List<GuiMessage.Line> getLinesFromMessage(GuiMessage msg) {
        ChatComponent cc = Minecraft.getInstance().gui.getChat();
        IChatComponentAccessor cca = (IChatComponentAccessor) cc;
        List<GuiMessage.Line> msgs = cca.getTrimmedMessages();
        return getLinesFromMessage(msg, msgs);
    }
    public static List<GuiMessage.Line> getLinesFromMessage(GuiMessage msg, List<GuiMessage.Line> msgs) {
        int found = -1;
        int low = 0;
        int high = msgs.size() - 1;
        int targetAddedTime = msg.addedTime();
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
            return Collections.emptyList();

        // move the index to the earliest message matching the time
        while (found + 1 < msgs.size() && msgs.get(found+1).addedTime() == targetAddedTime) {
            found++;
        }
        String message = cleanUpMessage(msg.content().getString());
        List<GuiMessage.Line> matching = new ArrayList<>();
        // move newer to find all matching messages
        while (found >= 0 && msgs.get(found).addedTime() == targetAddedTime) {
            if (msgs.get(found).tag() != msg.tag()) {
                found--;
                continue;
            }
            String searchingFor = cleanUpMessage(getPlainText(msgs.get(found).content()).trim());
            if (message.contains(searchingFor)) {
                matching.add(msgs.get(found));
            }
            found--;
        }
        return matching;
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

        // move the index to the latest message matching the time
        while (found > 0 && msgs.get(found-1).addedTime() == targetAddedTime) {
            found--;
        }
        String searchingFor = cleanUpMessage(getPlainText(line.content()).trim());
        // move older until you find a matching message
        while (found < msgs.size() && msgs.get(found).addedTime() == targetAddedTime) {
            if (msgs.get(found).tag() != line.tag()) {
                found++;
                continue;
            }
            String message = cleanUpMessage(msgs.get(found).content().getString());
            if (message.contains(searchingFor)) {
                return msgs.get(found);
            }
            found++;
        }
        return null;
    }
    public static String cleanUpMessage(String message) {
        message = message.replaceAll("§.","").replaceAll("￼","");
        if (FabricLoader.getInstance().isModLoaded("chat_heads")) {
            message = message.replaceAll("\\[[^]]*\\bhead]", "");
        }
        return message;
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
