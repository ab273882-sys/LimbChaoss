package com.limbchaos.mixin;

import com.limbchaos.client.LimbMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
    @Shadow protected int x;
    @Shadow protected int y;

    @Inject(method = "init", at = @At("TAIL"))
    private void limbchaos$addLimbButton(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        // Use the screen's public widget list through the inherited Screen API via the mixin target.
        ((InventoryScreenInvoker) this).limbchaos$addButton(ButtonWidget.builder(Text.literal("Конечности"), b -> {
            client.setScreen(new LimbMenuScreen());
        }).dimensions(this.x + 8, this.y - 24, 82, 20).build());
    }
}
