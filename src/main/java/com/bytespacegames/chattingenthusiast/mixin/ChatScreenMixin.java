package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;
import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.gui.GuiManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChatScreen.class, priority = 0)
public class ChatScreenMixin {
    @Inject(
            method = "extractRenderState",
            at = @At("HEAD")
    )
    public void render(GuiGraphicsExtractor graphicsExtractor, int i, int j, float f, CallbackInfo ci) {
        GuiManager.INSTANCE.updateGuiGraphics(graphicsExtractor);
        ChattingEnthusiast.chatting().render(GuiManager.INSTANCE);
    }
    @Inject(
            method = "mouseClicked",
            at = @At("HEAD")
    )
    public void mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (mouseButtonEvent.button() == 0) {
            ChattingEnthusiast.chatting().onClick();
        }
    }
    @Inject(
            method = "keyPressed",
            at = @At("HEAD")
    )
    public void keyPressed(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        ChattingEnthusiast.chatting().keyPressed(keyEvent);
    }
    @Inject(
            method="onClose",
            at=@At("HEAD")
    )
    public void onClose(CallbackInfo ci) {
        if (ChattingSettingsManager.INSTANCE.getSettingToggledById("clearsearch")) {
            ((EditBox)ChattingEnthusiast.chatting().search.getWidget()).setValue("");
            ChattingEnthusiast.chatting().search.getWidget().setFocused(false);
            ChattingEnthusiast.filter().setSearch("");
        }
    }
}
