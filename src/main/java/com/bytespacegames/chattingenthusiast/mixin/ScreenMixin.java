package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "tick",
            at = @At("HEAD"))
    public void mixin$tick(CallbackInfo ci) {
        if (!((Object) this instanceof ChatScreen)) return;
        ChattingEnthusiast.chatting.tick();
    }
    @Inject(method = "children", at = @At("RETURN"), cancellable = true)
    private void addCustomChild(CallbackInfoReturnable<List<? extends GuiEventListener>> cir) {
        List<? extends GuiEventListener> original = cir.getReturnValue();
        List<GuiEventListener> modified = new ArrayList<>(original);
        modified.add(ChattingEnthusiast.INSTANCE.getListener());
        cir.setReturnValue(modified);
    }
}
