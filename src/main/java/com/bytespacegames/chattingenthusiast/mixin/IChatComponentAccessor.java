package com.bytespacegames.chattingenthusiast.mixin;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ChatComponent.class)
public interface IChatComponentAccessor {
    @Accessor("trimmedMessages")
    List<GuiMessage.Line> getTrimmedMessages();
    @Accessor("allMessages")
    List<GuiMessage> getAllMessages();
    @Accessor("chatScrollbarPos")
    int getChatScrollbarPos();
    @Invoker("getLineHeight")
    int mixin$getLineHeight();
    @Invoker("getWidth")
    int mixin$getWidth();
    @Invoker("getLinesPerPage")
    int mixin$getLinesPerPage();
    @Invoker("getScale")
    double mixin$getScale();
    @Invoker("refreshTrimmedMessages")
    void mixin$refreshTrimmedMessages();
}