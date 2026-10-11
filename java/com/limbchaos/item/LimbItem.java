package com.limbchaos.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/** A goofy collectible limb. Right-clicking a living target bonks it for a tiny amount of damage. */
public class LimbItem extends Item {
    public LimbItem(Settings settings) { super(settings); }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.getWorld().isClient) return ActionResult.SUCCESS;
        if (entity == user) return ActionResult.PASS;
        entity.damage(user.getDamageSources().playerAttack(user), 2.0F);
        user.getWorld().sendEntityStatus(entity, (byte)  thumpStatus());
        if (!user.getAbilities().creativeMode) stack.decrement(1);
        return ActionResult.SUCCESS;
    }

    private byte thumpStatus() { return (byte) 0; }
}
