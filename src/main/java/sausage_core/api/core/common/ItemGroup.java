package sausage_core.api.core.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Supplier;

public class ItemGroup extends CreativeTabs {
	private final Supplier<ItemStack> supplier;
	private boolean searchable = false;

	public ItemGroup(String label, Supplier<ItemStack> supplier) {
		super(label);
		this.supplier = supplier;
	}

	@Override
	public ItemStack createIcon() {
		return supplier.get();
	}

	public ItemGroup setSearchable() {
		searchable = true;
		return this;
	}

	@Override
	public boolean hasSearchBar() {
		return searchable;
	}

	/**
	 * You don't need this to override anything else
	 * @see #addRelevantEnchantmentTypes(EnumEnchantmentType...)
	 * */
	@Deprecated
	@Override
	public final ItemGroup setRelevantEnchantmentTypes(EnumEnchantmentType... types) {
		super.setRelevantEnchantmentTypes(types);
		return this;
	}

	public ItemGroup addRelevantEnchantmentTypes(EnumEnchantmentType... types) {
		super.setRelevantEnchantmentTypes(ArrayUtils.addAll(getRelevantEnchantmentTypes(), types));
		return this;
	}
}
