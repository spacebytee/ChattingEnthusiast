package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        ChattingEnthusiast.chatting.render(guiGraphics);
    }
    @Inject(
            method = "mouseClicked",
            at = @At("HEAD")
    )
    public void mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (mouseButtonEvent.button() == 0) {
            ChattingEnthusiast.chatting.onClick();
        }
    }
}
