package com.limbchaos;

import com.limbchaos.item.LimbItem;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class LimbChaos implements ModInitializer {
    public static final String MOD_ID = "limbchaos";
    public static final Item LEFT_ARM = registerItem("left_arm", "Левая рука", new LimbItem(itemSettings("left_arm")));
    public static final Item RIGHT_ARM = registerItem("right_arm", "Правая рука", new LimbItem(itemSettings("right_arm")));
    public static final Item LEFT_LEG = registerItem("left_leg", "Левая нога", new LimbItem(itemSettings("left_leg")));
    public static final Item RIGHT_LEG = registerItem("right_leg", "Правая нога", new LimbItem(itemSettings("right_leg")));

    private static Item.Settings itemSettings(String path) {
        return new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, path)));
    }
    private static Item registerItem(String path, String name, Item item) {
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, path), item);
        return item;
    }

    @Override public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(CommandManager.literal("limbchaos")
                .then(CommandManager.literal("detach")
                    .then(CommandManager.argument("limb", StringArgumentType.word())
                        .executes(ctx -> detach(ctx.getSource().getPlayerOrThrow(), StringArgumentType.getString(ctx, "limb")))))
                .then(CommandManager.literal("restore")
                    .then(CommandManager.argument("limb", StringArgumentType.word())
                        .executes(ctx -> restore(ctx.getSource().getPlayerOrThrow(), StringArgumentType.getString(ctx, "limb")))))
            )
        );
    }

    private static int detach(ServerPlayerEntity player, String limb) {
        Item item = itemFor(limb);
        if (item == null) { player.sendMessage(Text.literal("§cНеизвестная конечность."), false); return 0; }
        String tag = "limbchaos_" + limb;
        if (player.getCommandTags().contains(tag)) {
            player.sendMessage(Text.literal("§eЭта конечность уже отсоединена!"), false); return 0;
        }
        player.addCommandTag(tag);
        var stack = new net.minecraft.item.ItemStack(item);
        if (!player.getInventory().insertStack(stack)) player.dropItem(stack, false);
        player.sendMessage(Text.literal("§dПЛОП! Ты отсоединил: " + limbName(limb) + " §7(предмет добавлен в инвентарь)"), false);
        return 1;
    }

    private static int restore(ServerPlayerEntity player, String limb) {
        Item item = itemFor(limb);
        if (item == null) { player.sendMessage(Text.literal("§cНеизвестная конечность."), false); return 0; }
        String tag = "limbchaos_" + limb;
        if (!player.getCommandTags().contains(tag)) {
            player.sendMessage(Text.literal("§eЭта конечность уже на месте."), false); return 0;
        }
        boolean hasLimb = false;
        for (int i = 0; i < player.getInventory().size(); i++) {
            var stack = player.getInventory().getStack(i);
            if (stack.isOf(item)) { stack.decrement(1); hasLimb = true; break; }
        }
        if (!hasLimb) { player.sendMessage(Text.literal("§cНужен предмет этой конечности в инвентаре!"), false); return 0; }
        player.removeCommandTag(tag);
        player.sendMessage(Text.literal("§aЧпок! Конечность вернулась на место."), false);
        return 1;
    }

    public static Item itemFor(String limb) {
        return switch (limb.toLowerCase()) {
            case "left_arm" -> LEFT_ARM;
            case "right_arm" -> RIGHT_ARM;
            case "left_leg" -> LEFT_LEG;
            case "right_leg" -> RIGHT_LEG;
            default -> null;
        };
    }
    public static String limbName(String limb) {
        return switch (limb) {
            case "left_arm" -> "левую руку"; case "right_arm" -> "правую руку";
            case "left_leg" -> "левую ногу"; case "right_leg" -> "правую ногу"; default -> limb;
        };
    }
}
