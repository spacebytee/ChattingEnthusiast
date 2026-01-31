package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.bytespacegames.chattingenthusiast.ext.IChatComponentExt;
import com.bytespacegames.chattingenthusiast.utils.ChatUtil;
import com.bytespacegames.config.settings.BooleanSetting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public class ChatFilter {

    private final Minecraft mc;
    // stuff regarding chat filters
    private volatile List<GuiMessage.Line> effectiveLines;
    private final List<GuiMessage.Line> lineQueue;
    private String searchCriteria = "";
    private String searchCriteriaLower = "";
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
    private static String stripFormatting(String s) {
        if (s.indexOf('§') == -1) {
            return s;
        }
        StringBuilder out = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '§' && i + 1 < s.length()) {
                i++;
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }
    public boolean unfiltered() {
        return (filter == TabFilter.NONE || !ChattingSettingsManager.INSTANCE.getSettingToggledById("tabfilters")) && searchCriteria.isEmpty();
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
        this.searchCriteriaLower = search.toLowerCase();
        if (requiresRefilter) queueRefilter();
    }

    public void setFilter(TabFilter tab) {
        if (!tab.equals(filter) && ChattingSettingsManager.INSTANCE.getSettingToggledById("tabfilters")) requiresRefilter = true;
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
            if (!((BooleanSetting)ChattingSettingsManager.INSTANCE.getSettingById("animation")).getValue()) return;
            if (((IChatComponentExt)mc.gui.getChat()).getRefreshing()) return;
            if (mc.gui.getChat().isChatFocused() && ((IChatComponentAccessor)mc.gui.getChat()).getChatScrollbarPos() != 0) return;
            ChattingEnthusiast.chatting().setChatOffset(ChattingEnthusiast.chatting().getChatOffset() + 9);
        }
    }
    private boolean matchesFilter(GuiMessage.Line line) {
        IChatComponentAccessor cca = (IChatComponentAccessor) mc.gui.getChat();
        GuiMessage msg = ChatUtil.getMessageFromLine(line, cca.getAllMessages());
        if (msg == null) return false;

        String contents = stripFormatting(msg.content().getString());

        if (!searchCriteria.isEmpty() && !contents.toLowerCase().contains(searchCriteriaLower)) return false;
        if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("tabfilters")) return true;
        if (filter == TabFilter.GUILD && !contents.startsWith("Guild > ")) return false;
        if (filter == TabFilter.PARTY && !contents.startsWith("Party > ")) return false;
        return filter != TabFilter.PM ||
                contents.startsWith("From ") ||
                contents.startsWith("To ");
    }
    public void queueRefilter() {
        queueRefilter(true);
    }
    public void queueRefilter(boolean resetScroll) {
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
                allSnapshot = new ArrayList<>(cca.getAllMessages());
            }

            for (GuiMessage.Line line : trimmedSnapshot) {
                GuiMessage originatingMessage = ChatUtil.getMessageFromLine(line,allSnapshot);
                if (originatingMessage == null) continue;
                if (!matchesFilter(line)) continue;
                filtered.add(line);
            }
            updateEffectiveLines(filtered, resetScroll);
            refilterInProgress = false;
        }).start();
    }
    public void updateEffectiveLines(List<GuiMessage.Line> lines, boolean resetScroll) {
        List<GuiMessage.Line> result = new ArrayList<>(lines);
        synchronized (queueLock) {
            for (GuiMessage.Line queued : lineQueue) {
                if (matchesFilter(queued)) {
                    result.addFirst(queued);
                }
            }
            lineQueue.clear();
            this.effectiveLines = result;
        }

        if (resetScroll)
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
        filter = TabFilter.NONE;
        searchCriteria = "";
        searchCriteriaLower = "";
        mc.gui.getChat().resetChatScroll();
    }

    public enum TabFilter {
        NONE,PARTY,GUILD,PM
    }
}
