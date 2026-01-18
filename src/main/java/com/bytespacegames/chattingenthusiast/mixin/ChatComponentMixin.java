package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.chattingenthusiast.config.BooleanSetting;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
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
public abstract class ChatComponentMixin {
	@Unique
	private GuiGraphics lastGraphics;
	@Unique
	private int lastMouseX;
	@Unique
	private int lastMouseY;
	@Shadow
	private final List<GuiMessage.Line> trimmedMessages = new ArrayList<>();
	@Shadow
	private int forEachLine(ChatComponent.AlphaCalculator alphaCalculator, ChatComponent.LineConsumer lineConsumer) {return 0;}
	@Shadow
	public int getLinesPerPage() {
		return 0;
	}
	@Shadow
	public boolean isChatFocused() { return false; }

	@Shadow protected abstract int getLineHeight();

	@Shadow private int chatScrollbarPos;

	@ModifyExpressionValue(
			method = {"addMessageToQueue(Lnet/minecraft/client/GuiMessage;)V", "addMessageToDisplayQueue", "addRecentChat"},
			at = @At(value = "CONSTANT", args = "intValue=100")
	)
	private int chatHistoryLength(int i) {
		return ChattingEnthusiast.MAX_MESSAGES;
	}

	@ModifyVariable(method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V", at = @At("STORE"), ordinal = 4)
	private int moveChat(int m) {
		return (int) (m + ChattingEnthusiast.OFFSET_CHAT_HEIGHT + ChattingEnthusiast.chatting().getChatOffset());
	}

	@Redirect(
			method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;fill(IIIII)V",
					ordinal=1
			)
	)
	private void skipScrollbarFill(ChatComponent.ChatGraphicsAccess graphics, int x1, int y1, int x2, int y2, int color) {

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

	}
	@Inject(
			method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V",
			at = @At("HEAD")
	)
	private void captureGraphics(final GuiGraphics graphics, final Font font, final int ticks, final int mouseX, final int mouseY, final boolean isChatting, final boolean changeCursorOnInsertions, CallbackInfo ci) {
		lastGraphics = graphics;
		ChattingEnthusiast.chatting().updateMouse(mouseX,mouseY);
	}
	@Shadow
	private int getWidth() { return 0; }
	@Shadow
	private double getScale() { return 0; }
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
		final int chatLX = chatBottom + (int) (ChattingEnthusiast.OFFSET_CHAT_HEIGHT + ChattingEnthusiast.chatting().getChatOffset());
		int messageHeight = 9;
		double chatLineSpacing = (Double)minecraft.options.chatLineSpacing().get();
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
		ChattingComponent ch = ChattingEnthusiast.chatting();
		boolean cancel = true;
		if (Math.abs(i) <= ChattingEnthusiast.SCROLLING_INTERVAL) {
			if (ChattingEnthusiast.chatting().ignoreScroll) {
				ChattingEnthusiast.chatting().ignoreScroll = false;
				return;
			}
			cancel = false;
		}
		if (cancel) ci.cancel();
		ch.desiredScrollbarPos += i;
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
		if (isChatFocused() && chatScrollbarPos != 0) return;
		if (!((BooleanSetting)ChattingSettingsManager.INSTANCE.getSettingById("animation")).getValue()) return;
		ChattingEnthusiast.chatting().setChatOffset(ChattingEnthusiast.chatting().getChatOffset() + getLineHeight());
	}

	@Redirect(
			method = "addMessageToDisplayQueue",
			at = @At(value = "INVOKE",target = "Ljava/util/List;addFirst(Ljava/lang/Object;)V"))
	private void onTrimmedMessageAdd(List<GuiMessage.Line> list, Object element) {
		GuiMessage.Line line = (GuiMessage.Line) element;
		ChattingEnthusiast.filter().onAddLine(line);
		list.addFirst(line);
	}
}