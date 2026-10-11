package com.limbchaos.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class LimbMenuScreen extends Screen {
    private static final String[] LIMBS = {"left_arm", "right_arm", "left_leg", "right_leg"};
    private static final String[] NAMES = {"Левая рука", "Правая рука", "Левая нога", "Правая нога"};

    public LimbMenuScreen() { super(Text.literal("Limb Chaos — меню конечностей")); }

    @Override protected void init() {
        int w = 190, x = (this.width - w) / 2, y = this.height / 2 - 48;
        for (int i = 0; i < LIMBS.length; i++) {
            final String limb = LIMBS[i];
            final int index = i;
            addDrawableChild(ButtonWidget.builder(buttonText(index), button -> {
                boolean detach = !LimbClientState.isMissing(limb);
                LimbClientState.setMissing(limb, detach);
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("limbchaos " + (detach ? "detach " : "restore ") + limb);
                }
                button.setMessage(buttonText(index));
            }).dimensions(x, y + i * 24, w, 20).build());
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Готово"), b -> close())
                .dimensions(x, y + 4 * 24 + 4, w, 20).build());
    }

    private static Text buttonText(int index) {
        String limb = LIMBS[index];
        return Text.literal(NAMES[index] + (LimbClientState.isMissing(limb) ? " — вернуть" : " — отсоединить"));
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 75, 0xFFFF55FF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Мемная хирургия. Без лицензии."), width / 2, height / 2 - 61, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override public boolean shouldPause() { return false; }
}
