package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingGui;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.chattingenthusiast.ext.IChatComponentExt;
import com.bytespacegames.config.settings.BooleanSetting;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin implements IChatComponentExt {
	@Unique
	private GuiGraphics lastGraphics;
	@Unique
	private boolean isRefreshing = false;
	@Unique
	private List<GuiMessage.Line> iteratingLines;
	public boolean getRefreshing() {
		return isRefreshing;
	}
	//region Shadow
	@Shadow
	private final List<GuiMessage.Line> trimmedMessages = new ArrayList<>();
	@Shadow
	private int forEachLine(ChatComponent.AlphaCalculator alphaCalculator, ChatComponent.LineConsumer lineConsumer) {return 0;}
	@Shadow
	public int getLinesPerPage() {
		return 0;
	}
	@Shadow
	private void addMessageToDisplayQueue(GuiMessage guiMessage) {}
	@Shadow
	private void addMessageToQueue(GuiMessage guiMessage) {}
	@Shadow
	private void logChatMessage(GuiMessage guiMessage) {}
	@Shadow
	public boolean isChatFocused() { return false; }
	@Shadow protected abstract int getLineHeight();
	@Shadow private int chatScrollbarPos;
	@Shadow private double getScale() { return 0; }

	//endregion
	@ModifyVariable(
			method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V",
			at = @At("HEAD"),
			ordinal = 0, argsOnly = true)
	private boolean chatPeek(boolean original) {
		return original || ChattingEnthusiast.INSTANCE.getChatPeekBind().isDown();
	}
	//region Filter Mixins
	@Redirect(
			method = "forEachLine",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"
			)
	)
	public List<GuiMessage.Line> replaceTrimmedMessages(ChatComponent instance) {
		return iteratingLines;
	}
	@Inject(
			method="render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V",
			at=@At("HEAD")
	)
	public void mixin$renderHead(ChatComponent.ChatGraphicsAccess chatGraphicsAccess, int i, int j, boolean bl, CallbackInfo ci) {
		iteratingLines = new ArrayList<>(ChattingEnthusiast.filter().getEffectiveLines());
	}
	@ModifyExpressionValue(
			method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;size()I"
			)
	)
	private int replaceTrimmedMessagesSize(int original) {
		return iteratingLines.size();
	}
	@Inject(
			method="clearMessages(Z)V",
			at=@At("RETURN")
	)
	public void mixin$clearMessages(boolean bl, CallbackInfo ci) {
		ChattingEnthusiast.filter().queueRefilter(true);
	}
	@Inject(
			method="refreshTrimmedMessages",
			at=@At("HEAD")
	)
	public void mixin$startRefreshTrimmedMessages(CallbackInfo ci) {
		isRefreshing = true;
	}
	@Inject(
			method="refreshTrimmedMessages",
			at=@At("RETURN")
	)
	public void mixin$refreshTrimmedMessages(CallbackInfo ci) {
		isRefreshing = false;
		ChattingEnthusiast.filter().queueRefilter(false);
	}
	@Redirect(
			method = "addMessageToDisplayQueue",
			at = @At(value = "INVOKE",target = "Ljava/util/List;addFirst(Ljava/lang/Object;)V"))
	private void onTrimmedMessageAdd(List<GuiMessage.Line> list, Object element) {
		GuiMessage.Line line = (GuiMessage.Line) element;
		list.addFirst(line);
		ChattingEnthusiast.filter().onAddLine(line);
	}
	//endregion

	// extends chat history
	@ModifyExpressionValue(
			method = {"addMessageToQueue(Lnet/minecraft/client/GuiMessage;)V", "addMessageToDisplayQueue", "addRecentChat"},
			at = @At(value = "CONSTANT", args = "intValue=100")
	)
	private int chatHistoryLength(int i) {
		if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("chathistory")) {
			return 100;
		}
		return ChattingEnthusiast.MAX_MESSAGES;
	}

	@ModifyVariable(method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V", at = @At("STORE"), ordinal = 4)
	private int moveChat(int m) {
		int constantOffset = ChattingSettingsManager.INSTANCE.getSettingToggledById("raisedchat") ? ChattingEnthusiast.OFFSET_CHAT_HEIGHT : 0;
		return (int) (m + constantOffset + ChattingEnthusiast.chatting().getChatOffset());
	}
	//region Scrollbar
	@Redirect(
			method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;fill(IIIII)V",
					ordinal=1
			)
	)
	private void skipScrollbarFill(ChatComponent.ChatGraphicsAccess graphics, int x1, int y1, int x2, int y2, int color) {
		if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("noscroll")) {
			graphics.fill(x1,y1,x2,y2,color);
		}
	}
	@Redirect(
			method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;fill(IIIII)V",
					ordinal=2
			)
	)
	private void skipScrollbarFill2(ChatComponent.ChatGraphicsAccess graphics, int x1, int y1, int x2, int y2, int color) {
		if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("noscroll")) {
			graphics.fill(x1,y1,x2,y2,color);
		}
	}
	//endregion
	@Inject(
			method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V",
			at = @At("HEAD")
	)
	private void captureGraphics(final GuiGraphics graphics, final Font font, final int ticks, final int mouseX, final int mouseY, final boolean isChatting, final boolean changeCursorOnInsertions, CallbackInfo ci) {
		lastGraphics = graphics;
		ChattingEnthusiast.chatting().updateMouse(mouseX,mouseY);
	}
	@Redirect(
			method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/components/ChatComponent;forEachLine(Lnet/minecraft/client/gui/components/ChatComponent$AlphaCalculator;Lnet/minecraft/client/gui/components/ChatComponent$LineConsumer;)I",
					ordinal=0
			)
	)
	private int renderLine(ChatComponent instance, final ChatComponent.AlphaCalculator alphaCalculator, final ChatComponent.LineConsumer lineConsumer) {
		Minecraft minecraft = Minecraft.getInstance();
		int chatBottom = Mth.floor((float)(minecraft.getWindow().getGuiScaledHeight() - 40) / getScale());
		int constantOffset = ChattingSettingsManager.INSTANCE.getSettingToggledById("raisedchat") ? ChattingEnthusiast.OFFSET_CHAT_HEIGHT : 0;
		final int chatLX = chatBottom + (int) (constantOffset + ChattingEnthusiast.chatting().getChatOffset());
		int messageHeight = 9;
		double chatLineSpacing = minecraft.options.chatLineSpacing().get();
		int entryHeight = (int)((double)messageHeight * (chatLineSpacing + 1.0D));

		return forEachLine(alphaCalculator, (line, lineIndex, alphax) -> {
			int entryBottom = chatLX - lineIndex * entryHeight;
			int entryTop = entryBottom - entryHeight;
			ChattingEnthusiast.chatting().renderCustomLine(lastGraphics, -4, entryTop, entryBottom, lineIndex, alphax);
		});
	}
	@Inject(method = "resetChatScroll",
			at = @At("HEAD"))
	public void mixin$resetChatScroll(CallbackInfo ci) {
		ChattingEnthusiast.chatting().desiredScrollbarPos = 0;
	}
	@Inject(method = "scrollChat",
			at = @At("HEAD"),
			cancellable = true)
	public void mixin$scrollChat(int i, CallbackInfo ci) {
		boolean smoothscroll = ((BooleanSetting)ChattingSettingsManager.INSTANCE.getSettingById("smoothscroll")).getValue();
		if (!smoothscroll) {
			return;
		}
		ChattingGui ch = ChattingEnthusiast.chatting();
		boolean cancel = true;
		// if this was called from the animation, scroll normally
		if (Math.abs(i) <= ChattingEnthusiast.SCROLLING_INTERVAL) {
			if (ChattingEnthusiast.chatting().ignoreScroll) {
				ChattingEnthusiast.chatting().ignoreScroll = false;
				return;
			}
			cancel = false;
		}
		// if scroll animation was ignored, scroll normally, + change the desiredScrollbarPos so it doesn't animate away
		if (ChattingEnthusiast.chatting().ignoreScroll && !(Math.abs(i) <= ChattingEnthusiast.SCROLLING_INTERVAL)) {
			ch.desiredScrollbarPos += i;
			ChattingEnthusiast.chatting().ignoreScroll = false;
			return;
		}
		if (cancel) ci.cancel();

		if ((ch.desiredScrollbarPos > chatScrollbarPos && i < 0) || ch.desiredScrollbarPos < chatScrollbarPos && i > 0) {
			ch.desiredScrollbarPos = chatScrollbarPos + i;
		} else {
			ch.desiredScrollbarPos += i;
		}

		int j = trimmedMessages.size();
		if (ch.desiredScrollbarPos > j - this.getLinesPerPage()) {
			ch.desiredScrollbarPos = j - this.getLinesPerPage();
		}
		if (ch.desiredScrollbarPos <= 0) {
			ch.desiredScrollbarPos = 0;
		}
	}

	@Inject(method = "addMessageToDisplayQueue",
			at = @At("HEAD"))
	private void mixin$addMessageToDisplayQueue(GuiMessage guiMessage, CallbackInfo ci) {
		if (isRefreshing) return;
		if (isChatFocused() && chatScrollbarPos != 0) return;
		if (!((BooleanSetting)ChattingSettingsManager.INSTANCE.getSettingById("animation")).getValue()) return;
		if (!ChattingEnthusiast.filter().unfiltered()) return;
		ChattingEnthusiast.chatting().setChatOffset(ChattingEnthusiast.chatting().getChatOffset() + getLineHeight());
	}

	// change order to add the GuiMessage before the Line, to solve an issue regarding filters
	@Inject(method="addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V",
		at=@At("HEAD"),
		cancellable = true)
	public void mixin$addMessage(Component component, MessageSignature messageSignature, GuiMessageTag guiMessageTag, CallbackInfo ci) {
		ci.cancel();
		GuiMessage guiMessage = new GuiMessage(Minecraft.getInstance().gui.getGuiTicks(), component, messageSignature, guiMessageTag);
		logChatMessage(guiMessage);
		GuiMessage compacted = ChattingSettingsManager.INSTANCE.getSettingToggledById("compactchat") ? ChattingEnthusiast.compactChat().compactMessage(guiMessage) : guiMessage;
		addMessageToQueue(compacted);
		addMessageToDisplayQueue(compacted);
	}
}