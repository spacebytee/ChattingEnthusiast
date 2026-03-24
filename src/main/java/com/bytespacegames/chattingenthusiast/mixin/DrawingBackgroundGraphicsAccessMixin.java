package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.DrawingBackgroundGraphicsAccess.class)
public class DrawingBackgroundGraphicsAccessMixin {
    @Inject(method = "handleTag",
            at = @At("HEAD"),
            cancellable = true)
    public void mixin$handleTag(int par1, int par2, int par3, int par4, float par5, GuiMessageTag par6, CallbackInfo ci) {
        if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("notags")) return;
        ci.cancel();
    }
}

@Mixin(ChatComponent.DrawingFocusedGraphicsAccess.class)
class DrawingFocusedGraphicsAccessMixin {
    @Inject(method = "handleTag",
            at = @At("HEAD"),
            cancellable = true)
    public void mixin$handleTag(int par1, int par2, int par3, int par4, float par5, GuiMessageTag par6, CallbackInfo ci) {
        if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("notags")) return;
        ci.cancel();
    }
}