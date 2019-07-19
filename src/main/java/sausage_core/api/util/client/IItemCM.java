package sausage_core.api.util.client;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface IItemCM {
	int colorMultiplier(ItemStack stack, int tintIndex);
}