package com.bytespacegames.chattingenthusiast.mixin;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ChatComponent.class)
public interface IChatComponentAccessor {
    @Accessor("trimmedMessages")
    public List<GuiMessage.Line> getTrimmedMessages();
    @Accessor("chatScrollbarPos")
    public int getChatScrollbarPos();
    @Invoker("getLineHeight")
    public int mixin$getLineHeight();
}