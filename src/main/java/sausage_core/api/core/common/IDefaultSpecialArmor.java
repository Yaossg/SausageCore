package sausage_core.api.core.common;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

import javax.annotation.Nonnull;

/**
 * Default implementation of {@link ISpecialArmor}
 * */
public interface IDefaultSpecialArmor extends ISpecialArmor {
    /**
     * all damage will apply on {@link IDefaultSpecialArmor#damageArmor(EntityLivingBase, ItemStack, DamageSource, int, int)} instead of vanilla
     * */
    @Override
    default ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(0, 1, Integer.MAX_VALUE);
    }
    /**
     * no extra armor value display provided
     * */
    @Override
    default int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return 0;
    }
    /**
     * this implementation invokes vanilla method
     * */
    @Override
    default void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        if(stack.attemptDamageItem(damage, entity.getRNG(), entity instanceof EntityPlayerMP ? ((EntityPlayerMP) entity) : null))
            onArmorBroken(entity, stack, source, damage, slot);
    }

    default void onArmorBroken(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        entity.renderBrokenItemStack(stack);
        stack.shrink(1);
    }
}