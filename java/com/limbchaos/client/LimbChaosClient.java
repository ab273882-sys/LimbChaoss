package com.limbchaos.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LimbChaosClient implements ClientModInitializer {
    private static KeyBinding openMenu;

    @Override public void onInitializeClient() {
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.limbchaos.open_menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_L,
                KeyBinding.Category.MISC));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                if (client.player != null && client.currentScreen == null) client.setScreen(new LimbMenuScreen());
            }
        });
    }
}
