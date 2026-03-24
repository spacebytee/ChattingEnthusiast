package com.bytespacegames.chattingenthusiast.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenCompatMixin {
    @Inject(
            method = "drawLineButton",
            at = @At("HEAD"),
            remap=false,
            cancellable = true
    )
    public void mixin$drawLineButton(GuiGraphicsExtractor context, int mouseX, int mouseY, CallbackInfo ci) {
        ci.cancel();
    }
    @Inject(
            method = "drawScreenshotButton",
            at = @At("HEAD"),
            remap=false,
            cancellable = true
    )
    public void mixin$drawScreenshotButton(GuiGraphicsExtractor context, int mouseX, int mouseY, CallbackInfo ci) {
        ci.cancel();
    }
}