package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingComponent;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
	@Unique
	private GuiGraphics lastGraphics;
	@Unique
	private int lastMouseX;
	@Unique
	private int lastMouseY;
	@Shadow
	private final List<GuiMessage.Line> trimmedMessages = new ArrayList<>();
	@Shadow
	private static double getTimeFactor(int i) { return 0; }
	@Shadow
	private int forEachLine(int i, int j, boolean bl, int k, ChatComponent.LineConsumer lineConsumer) {return 0;}
	@Shadow
	private int getTagIconLeft(GuiMessage.Line line) {
		return 0;
	}
	@Shadow
	private void drawTagIcon(GuiGraphics guiGraphics, int i, int j, GuiMessageTag.Icon icon) {}
	@Shadow
    public double getScale() { return 0; }
	@Shadow
	private int getMessageEndIndexAt(double d, double e) { return 0;}
	@Shadow
	public int getLinesPerPage() {
		return 0;
	}
	@Shadow
	private double screenToChatX(double d) {
		return 0;
	}
	@Shadow
	private double screenToChatY(double d) {
		return 0;
	}
	@ModifyExpressionValue(
			method = {"addMessageToQueue(Lnet/minecraft/client/GuiMessage;)V", "addMessageToDisplayQueue", "addRecentChat"},
			at = @At(value = "CONSTANT", args = "intValue=100")
	)
	private int chatHistoryLength(int i) {
		return ChattingEnthusiast.MAX_MESSAGES;
	}

	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V",
					ordinal=1
			)
	)
	private void skipScrollbarFill(GuiGraphics graphics, int x1, int y1, int x2, int y2, int color) {

	}
	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V",
					ordinal=2
			)
	)
	private void skipScrollbarFill2(GuiGraphics graphics, int x1, int y1, int x2, int y2, int color) {

	}
	@Inject(
			method = "render",
			at = @At("HEAD")
	)
	private void captureGraphics(GuiGraphics graphics, int i, int j, int k, boolean bl, CallbackInfo ci) {
		lastGraphics = graphics;
		lastMouseX = j;
		lastMouseY = k;
		ChattingEnthusiast.chatting.updateMouse(j,k);
	}
	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/components/ChatComponent;forEachLine(IIZILnet/minecraft/client/gui/components/ChatComponent$LineConsumer;)I",
					ordinal=0
			)
	)
	private int renderLine(ChatComponent instance, int i, int j, boolean bl, int k, ChatComponent.LineConsumer consumer) {
		Minecraft minecraft = Minecraft.getInstance();
		float g = minecraft.options.chatOpacity().get().floatValue() * 0.9F + 0.1F;
		int q = getMessageEndIndexAt(screenToChatX(lastMouseX), screenToChatY(lastMouseY));
		double d = minecraft.options.chatLineSpacing().get();
		int r = (int)Math.round(-8.0 * (d + 1.0) + 4.0 * d);
		int p = Mth.floor((lastGraphics.guiHeight() - 40) / getScale());
		return forEachLine(getLinesPerPage(), i, bl, p, (lx, mx, nx, line, ox, hx) -> {
			float fakeHx = bl ? 1.0f : ((float) getTimeFactor(j-line.addedTime()));
			ChattingEnthusiast.chatting.renderCustomLine(lastGraphics, lx, mx, nx, ox,fakeHx);
			GuiMessageTag guiMessageTag = line.tag();
			if (guiMessageTag != null) {
				int px = ARGB.color(fakeHx * g, guiMessageTag.indicatorColor());
				lastGraphics.fill(lx - 4, mx, lx - 2, nx, px);
				if (ox == q && guiMessageTag.icon() != null) {
					int qx = getTagIconLeft(line);
					int rx = nx + r + 9;
					drawTagIcon(lastGraphics, qx, rx, guiMessageTag.icon());
				}
			}
		});
	}
	@Inject(method = "resetChatScroll",
			at = @At("HEAD"))
	public void mixin$resetChatScroll(CallbackInfo ci) {
		ChattingEnthusiast.chatting.desiredScrollbarPos = 0;
	}
	@Inject(method = "scrollChat",
			at = @At("HEAD"),
			cancellable = true)
	public void mixin$scrollChat(int i, CallbackInfo ci) {
		ChattingComponent ch = ChattingEnthusiast.chatting;
		boolean cancel = true;
		if (Math.abs(i) <= ChattingEnthusiast.SCROLLING_INTERVAL) {
			if (ChattingEnthusiast.chatting.ignoreScroll) {
				ChattingEnthusiast.chatting.ignoreScroll = false;
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
	@Inject(method = "tick",
			at = @At("HEAD"))
	public void mixin$tick(CallbackInfo ci) {
		ChattingEnthusiast.chatting.tick();
	}
}