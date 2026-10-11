package com.limbchaos.mixin;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InventoryScreen.class)
public interface InventoryScreenInvoker {
    @Invoker("addDrawableChild") <T extends ClickableWidget> T limbchaos$addButton(T widget);
}
