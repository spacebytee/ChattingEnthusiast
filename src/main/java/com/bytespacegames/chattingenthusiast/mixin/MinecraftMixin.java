package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import com.bytespacegames.config.gui.ConfigGui;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.bytespacegames.chattingenthusiast.ChattingEnthusiast;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method="tick", at=@At("HEAD"))
    public void tick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (ChattingEnthusiast.INSTANCE.shouldOpenGui && mc.level != null && mc.screen == null) {
            ChattingEnthusiast.INSTANCE.shouldOpenGui = false;
            mc.execute(() -> mc.setScreen(new ConfigGui(ChattingSettingsManager.INSTANCE)));
        }
        if (FabricLoader.getInstance().isModLoaded("nochatreports")) {
            ChattingEnthusiast.chatting().tick();
        }
    }
}
