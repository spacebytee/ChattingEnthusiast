package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "tick",
            at = @At("HEAD"))
    public void mixin$tick(CallbackInfo ci) {
        if (!((Object) this instanceof ChatScreen)) return;
        ChattingEnthusiast.chatting.tick();
    }
}
