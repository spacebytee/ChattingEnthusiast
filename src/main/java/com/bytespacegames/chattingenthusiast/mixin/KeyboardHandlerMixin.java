package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.CharacterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(method="charTyped", at=@At("HEAD"))
    public void mixin$charTyped(long l, CharacterEvent characterEvent, CallbackInfo ci) {
        if (Minecraft.getInstance().gui.screen() instanceof ChatScreen) {
            ChattingEnthusiast.chatting().charTyped(characterEvent);
        }
    }
}