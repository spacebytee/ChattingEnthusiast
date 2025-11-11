package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
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
}