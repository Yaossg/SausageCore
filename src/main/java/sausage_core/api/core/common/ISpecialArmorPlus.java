package sausage_core.api.core.common;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

import javax.annotation.Nonnull;

public interface ISpecialArmorPlus extends ISpecialArmor {
	/**
	 * From Forge's TO-DO item to avoid magic numbers
	 */
	enum EnumArmorType {
		HEAD(EntityEquipmentSlot.HEAD),
		CHEST(EntityEquipmentSlot.CHEST),
		LEGS(EntityEquipmentSlot.LEGS),
		FEET(EntityEquipmentSlot.FEET);
		private final EntityEquipmentSlot equipmentSlot;

		EnumArmorType(EntityEquipmentSlot equipmentSlot) {

			this.equipmentSlot = equipmentSlot;
		}

		public EntityEquipmentSlot toEquipmentSlot() {
			return equipmentSlot;
		}

		public static EnumArmorType fromEquipmentSlot(EntityEquipmentSlot equipmentSlot) {
			return valueOf(equipmentSlot.getName().toUpperCase());
		}

		public int toSlot() {
			return ordinal();
		}

		public static EnumArmorType fromSlot(int slot) {
			return values()[slot];
		}
	}

	@Override
	default ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
		return getProperties(player, armor, source, damage, EnumArmorType.fromSlot(slot));
	}

	@Override
	default int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
		return getArmorDisplay(player, armor, EnumArmorType.fromSlot(slot));
	}

	@Override
	default void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
		damageArmor(entity, stack, source, damage, EnumArmorType.fromSlot(slot));
	}

	/**
	 * {@inheritDoc}
	 * all damage will apply on {@link ISpecialArmorPlus#damageArmor(EntityLivingBase, ItemStack, DamageSource, int, int)} instead of vanilla
	 */
	default ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, EnumArmorType armorType) {
		return new ArmorProperties(0, 1, Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 * no extra armor value display provided
	 */
	default int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, EnumArmorType armorType) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * this implementation call the vanilla method
	 */
	default void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, EnumArmorType armorType) {
		if (stack.attemptDamageItem(damage, entity.getRNG(), entity instanceof EntityPlayerMP ? ((EntityPlayerMP) entity) : null))
			onArmorBroken(entity, stack, source, damage, armorType);
	}

	default void onArmorBroken(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, EnumArmorType armorType) {
		entity.renderBrokenItemStack(stack);
		stack.shrink(1);
	}
}