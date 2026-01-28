package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.config.BooleanSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Gui.class)
public class GuiMixin {
    @Redirect(
            method = "onDisconnected",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/ChatComponent;clearMessages(Z)V",
                    ordinal=0
            )
    )
    public void mixin$onDisconnected(ChatComponent instance, boolean bl) {
        if (!((BooleanSetting) ChattingSettingsManager.INSTANCE.getSettingById("clearchat")).getValue()) {
            instance.clearMessages(bl);
        }
    }
}
