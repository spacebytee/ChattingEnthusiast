package com.bytespacegames.chattingenthusiast.mixin;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatScreen.class)
public interface IChatScreenAccessor {
    @Accessor("input")
    EditBox getInput();
}
