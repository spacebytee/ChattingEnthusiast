package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Unique
    ClickEvent.RunCommand hoveredCommand;
    @Shadow
    public void setTooltipForNextFrame(Component component, int i, int j) {}
    @Inject(
            method="renderComponentHoverEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;requestCursor(Lcom/mojang/blaze3d/platform/cursor/CursorType;)V"
            )
    )
    public void mixin$renderComponentHoverEffect(Font font, Style style, int i, int j, CallbackInfo ci) {
        if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("tooltipcommands")) return;

        ClickEvent click = style.getClickEvent();
        if (click instanceof ClickEvent.RunCommand run) {
            hoveredCommand = run;
        } else {
            hoveredCommand = null;
            return;
        }

        if (style.getClickEvent() != null && style.getHoverEvent() == null) {
            ClickEvent.RunCommand tempCommand = hoveredCommand;
            hoveredCommand = null;
            setTooltipForNextFrame(Component.literal(I18n.get("chattingenthusiast.tooltip.command") + tempCommand.command()),i,j);
        }
    }
    @ModifyVariable(
            method = "setTooltipForNextFrameInternal",
            at = @At(value = "HEAD"),
            index = 2,
            argsOnly = true)
    private List<ClientTooltipComponent> replaceTooltipList(List<ClientTooltipComponent> originalList) {
        if (hoveredCommand == null || !ChattingSettingsManager.INSTANCE.getSettingToggledById("tooltipcommands")) return originalList;
        List<ClientTooltipComponent> newList = new ArrayList<>(originalList);
        newList.add(ClientTooltipComponent.create(Component.literal(I18n.get("chattingenthusiast.tooltip.command") + hoveredCommand.command()).getVisualOrderText()));
        hoveredCommand = null;
        return newList;
    }
}
