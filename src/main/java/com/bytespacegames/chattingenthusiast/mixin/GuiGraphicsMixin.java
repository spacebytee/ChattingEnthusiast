package com.bytespacegames.chattingenthusiast.mixin;

import com.bytespacegames.chattingenthusiast.ChattingSettingsManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsMixin {
    @Unique
    ClickEvent.RunCommand hoveredCommand;
    @Final
    @Shadow
    int mouseX;
    @Final
    @Shadow
    int mouseY;
    @Shadow
    Style hoveredTextStyle;
    @Shadow
    Style clickableTextStyle;
    @Shadow
    public void setTooltipForNextFrame(Component component, int i, int j) {}
    @Inject(
            method="extractDeferredElements",
            at = @At(
                    value = "HEAD"
            )
    )
    public void mixin$renderDeferredElements(final int mX, final int mY, final float f, CallbackInfo ci) {
        if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("tooltipcommands")) return;

        Style style = hoveredTextStyle;
        if (style == null) style = clickableTextStyle;
        if (style != null && style.getClickEvent() != null && style.getHoverEvent() == null && style.getClickEvent() instanceof ClickEvent.RunCommand run) {
            String command = run.command();
            if (command.startsWith("/")) command = command.substring(1);
            setTooltipForNextFrame(Component.literal(I18n.get("chattingenthusiast.tooltip.command") + command),mouseX,mouseY);
        }
    }
    @Inject(
            method="componentHoverEffect",
            at = @At("HEAD")
    )
    public void mixin$renderComponentHoverEffect(Font font, Style style, int i, int j, CallbackInfo ci) {
        if (!ChattingSettingsManager.INSTANCE.getSettingToggledById("tooltipcommands")) return;

        ClickEvent click = style.getClickEvent();
        if (click instanceof ClickEvent.RunCommand run) {
            hoveredCommand = run;
        } else {
            hoveredCommand = null;
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
        String command = hoveredCommand.command();
        if (command.startsWith("/")) command = command.substring(1);
        newList.add(ClientTooltipComponent.create(Component.literal(I18n.get("chattingenthusiast.tooltip.command") + command).getVisualOrderText()));
        hoveredCommand = null;
        return newList;
    }
}