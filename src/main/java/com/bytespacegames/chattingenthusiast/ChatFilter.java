package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.chattingenthusiast.utils.ChatUtil;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class ChatFilter {

    private Minecraft mc;
    // stuff regarding chat filters
    private volatile List<GuiMessage.Line> effectiveLines;
    private volatile List<GuiMessage.Line> lineQueue;
    private String searchCriteria = "";
    private TabFilter filter = TabFilter.NONE;
    private boolean requiresRefilter = false;
    private volatile boolean refilterInProgress = false;
    private final Object chatTrimLock = new Object();
    private final Object queueLock = new Object();

    public ChatFilter() {
        effectiveLines = new ArrayList<>();
        lineQueue = new ArrayList<>();
        mc = Minecraft.getInstance();
    }

    public boolean unfiltered() {
        return filter == TabFilter.NONE && searchCriteria.isEmpty();
    }
    public List<GuiMessage.Line> getEffectiveLines() {
        if (unfiltered()) {
            return ((IChatComponentAccessor)mc.gui.getChat()).getTrimmedMessages();
        }
        return effectiveLines;
    }

    public void setSearch(String search) {
        if (!search.equals(searchCriteria)) requiresRefilter = true;
        this.searchCriteria = search;
        if (requiresRefilter) queueRefilter();
    }

    public void setFilter(TabFilter tab) {
        if (!tab.equals(filter)) requiresRefilter = true;
        this.filter = tab;
        if (requiresRefilter) queueRefilter();
    }
    public void onAddLine(GuiMessage.Line line) {
        if (unfiltered()) {
            return;
        }

        synchronized (queueLock) {
            if (refilterInProgress) {
                lineQueue.add(line);
                return;
            }
        }

        if (matchesFilter(line)) {
            effectiveLines.addFirst(line);
        }
    }
    private boolean matchesFilter(GuiMessage.Line line) {
        IChatComponentAccessor cca = (IChatComponentAccessor) mc.gui.getChat();
        GuiMessage msg = ChatUtil.getMessageFromLine(line, cca.getAllMessages());
        if (msg == null) return false;

        String contents = msg.content().getString().replaceAll("§.","");

        if (!searchCriteria.isEmpty() && !contents.toLowerCase().contains(searchCriteria.toLowerCase())) return false;
        if (filter == TabFilter.GUILD && !contents.startsWith("Guild > ")) return false;
        if (filter == TabFilter.PARTY && !contents.startsWith("Party > ")) return false;
        if (filter == TabFilter.PM &&
                !contents.startsWith("From ") &&
                !contents.startsWith("To ")) return false;

        return true;
    }
    public void queueRefilter() {
        if (unfiltered()) {
            clear();
            return;
        }
        requiresRefilter = false;
        refilterInProgress = true;
        new Thread(() -> {
            List<GuiMessage.Line> trimmedSnapshot;
            List<GuiMessage> allSnapshot;
            List<GuiMessage.Line> filtered = new ArrayList<>();
            IChatComponentAccessor cca = ((IChatComponentAccessor) mc.gui.getChat());
            synchronized (chatTrimLock) {
                trimmedSnapshot = new ArrayList<>(cca.getTrimmedMessages());
                allSnapshot =new ArrayList<>(cca.getAllMessages());
            }

            for (GuiMessage.Line line : trimmedSnapshot) {
                GuiMessage originatingMessage = ChatUtil.getMessageFromLine(line,allSnapshot);
                if (originatingMessage == null) continue;
                if (!matchesFilter(line)) continue;
                filtered.add(line);
            }
            updateEffectiveLines(filtered);
        }).start();
    }
    public void updateEffectiveLines(List<GuiMessage.Line> lines) {
        List<GuiMessage.Line> result = new ArrayList<>(lines);

        synchronized (queueLock) {
            for (GuiMessage.Line queued : lineQueue) {
                if (matchesFilter(queued)) {
                    result.addFirst(queued);
                }
            }
            lineQueue.clear();
            refilterInProgress = false;
            this.effectiveLines = result;
        }
        mc.gui.getChat().resetChatScroll();
    }

    public TabFilter getFilter() {
        return filter;
    }
    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void clear() {
        if (unfiltered()) {
            return;
        }
        effectiveLines.clear();
        mc.gui.getChat().resetChatScroll();
    }

    public enum TabFilter {
        NONE,PARTY,GUILD,PM
    }
}
