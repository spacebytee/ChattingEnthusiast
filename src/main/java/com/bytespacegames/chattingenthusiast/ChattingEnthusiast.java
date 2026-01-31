package com.bytespacegames.chattingenthusiast;

import com.bytespacegames.chattingenthusiast.compactchat.CompactChatManager;
import com.bytespacegames.gui.GuiManager;
import com.bytespacegames.chattingenthusiast.mixin.IChatComponentAccessor;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChattingEnthusiast implements ClientModInitializer {
	public static final String MOD_ID = "chattingenthusiast";
	public static final int MAX_MESSAGES = 16384;
	public static final int SCROLLING_INTERVAL = 1;
	public static final int ANIMATION_INTERVAL = 1000/60;
	public static final int OFFSET_CHAT_HEIGHT = -10;
	public static ChattingEnthusiast INSTANCE;
	private ChattingGui chatting;
	private ChatFilter filter;
	private CompactChatManager compactChat;
	public boolean shouldOpenGui = false;

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	Minecraft mc;

	private static KeyMapping chatPeekBind;


    @Override
	public void onInitializeClient() {
		INSTANCE = this;
		mc = Minecraft.getInstance();
		new GuiManager();
		new ChattingSettingsManager();
		chatting = new ChattingGui();
		filter = new ChatFilter();
		compactChat = new CompactChatManager();

		tryRegisterCommand();

        KeyMapping.Category CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("chattingenthusiast", "chattingenthusiast"));
		chatPeekBind = new KeyMapping(
				"key.chattingenthusiast.chatpeek",
				GLFW.GLFW_KEY_Z,
                CATEGORY
		);
		if (FabricLoader.getInstance().isModLoaded("fabric-api"))
			KeyBindingHelper.registerKeyBinding(chatPeekBind);
	}

	public KeyMapping getChatPeekBind() {
		return chatPeekBind;
	}

	public void tryRegisterCommand() {
		if (!FabricLoader.getInstance().isModLoaded("fabric-api")) {
			return;
		}
		try {
			ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
				LiteralArgumentBuilder<FabricClientCommandSource> cmd = ClientCommandManager.literal("chatting")
						.executes(this::openGui);
				dispatcher.register(cmd);
			});
		} catch (Throwable ignored) {

		}
	}

	private static final String[] HYPIXEL_IPS = { "hypixel.io", "hypixel.net"};
	public static boolean connectedToHypixel() {
		if (Minecraft.getInstance().getCurrentServer() == null) return false;
		String ip = Minecraft.getInstance().getCurrentServer().ip;
		for (String s : HYPIXEL_IPS) {
			if (ip.toLowerCase().contains(s)) return true;
		}
		return false;
	}

	public int openGui(CommandContext commandContext) {
		shouldOpenGui = true;
		return 1;
	}

	public int getHoveredMessage(int mouseX, int mouseY) {
		if (!(mc.screen instanceof ChatScreen)) {
			return -1;
		}
		ChatComponent cc = mc.gui.getChat();
		IChatComponentAccessor cca = (IChatComponentAccessor) (cc);
		int scaleOffset = Mth.ceil(cca.mixin$getWidth() / mc.options.chatScale().get());
		int effectiveWidth = scaleOffset + 8 + 4;
		// we add the line height * 3, and 3 additional pixels to account for the copy/delete/jump buttons as part of the line
		int buttons = ChattingSettingsManager.INSTANCE.getSettingToggledById("linecontrols") ? (filter.unfiltered() ? 2 : 3) : 0;
		if (mouseX > effectiveWidth + (1 + cca.mixin$getLineHeight()) * buttons) return -1;
		int scrollBarOffset = cca.getChatScrollbarPos();
		double chatHeight = cca.mixin$getLineHeight() * ((IChatComponentAccessor)mc.gui.getChat()).mixin$getScale();
		// mouse offset is the pixels away from the bottom of the chat your mouse is at
		int constantOffset = ChattingSettingsManager.INSTANCE.getSettingToggledById("raisedchat") ? ChattingEnthusiast.OFFSET_CHAT_HEIGHT : 0;
		int mouseOffset = (mc.getWindow().getGuiScaledHeight() - mouseY) - 40 + constantOffset + (int) chatting.getChatOffset();
		if (mouseOffset < 0) return -1;
		int index = (int) ((mouseOffset/chatHeight) + scrollBarOffset);
		if (index >= cca.getTrimmedMessages().size()) return -1;
		if (index-scrollBarOffset >= cca.mixin$getLinesPerPage()) return -1;
		return index;
	}

	public static ChattingGui chatting() {
		return INSTANCE.chatting;
	}

	public static ChatFilter filter() {
		return INSTANCE.filter;
	}
	public static CompactChatManager compactChat() {
		return INSTANCE.compactChat;
	}

}