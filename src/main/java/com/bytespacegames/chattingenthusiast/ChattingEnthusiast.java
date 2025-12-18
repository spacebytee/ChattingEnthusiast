package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChattingEnthusiast implements ClientModInitializer {
	public static final String MOD_ID = "chattingenthusiast";
	public static final int MAX_MESSAGES = 16384;
	public static final int SCROLLING_INTERVAL = 1;
	public static ChattingEnthusiast INSTANCE;
	public static ChattingComponent chatting;
	public static int offsetChatHeight = 0;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	Minecraft mc;

	@Override
	public void onInitializeClient() {
		INSTANCE = this;
		mc = Minecraft.getInstance();
		chatting = new ChattingComponent();
	}

	public int getHoveredMessage(int mouseX, int mouseY) {
		if (!(mc.screen instanceof ChatScreen)) {
			return -1;
		}
		ChatComponent cc = mc.gui.getChat();
		IChatComponentAccessor cca = (IChatComponentAccessor) (cc);
		int scaleOffset = Mth.ceil(cca.mixin$getWidth() / mc.options.chatScale().get());
		int effectiveWidth = scaleOffset + 8 + 4;
		// we add the line height * 2, and 2 additional pixels to account for the copy/delete buttons as part of the line
		if (mouseX > effectiveWidth + 2 + cca.mixin$getLineHeight() * 2) return -1;
		int scrollBarOffset = cca.getChatScrollbarPos();
		int chatHeight = cca.mixin$getLineHeight();
		// mouse offset is the pixels away from the bottom of the chat your mouse is at
		int mouseOffset = (mc.getWindow().getGuiScaledHeight() - mouseY) - 40 - offsetChatHeight;
		if (mouseOffset < 0) return -1;
		int index = (mouseOffset/chatHeight) + scrollBarOffset;
		if (index >= cca.getTrimmedMessages().size()) return -1;
		if (index-scrollBarOffset >= cca.mixin$getLinesPerPage()) return -1;
		return index;
	}
}