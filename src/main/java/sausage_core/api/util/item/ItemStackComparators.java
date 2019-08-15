package sausage_core.api.util.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import sausage_core.api.util.function.IEqualityComparator;
import sausage_core.api.util.function.IIntEqualityComparator;

import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public final class ItemStackComparators {
	private ItemStackComparators() {}
	public static final Builder ITEM_EQUAL_BUILDER = builder().anyCount().anyNBT();
	public static final IEqualityComparator<ItemStack>
			IS_ITEM_EQUAL = ItemStack::isItemEqual,
			ARE_ITEMS_EQUAL = ItemStack::areItemsEqual,
			ITEM_EQUAL = ITEM_EQUAL_BUILDER.build();
	public static final Builder ITEM_EQUAL_IGNORE_DURABILITY_BUILDER = builder().ignoreDurability().anyCount().anyNBT();
	public static final IEqualityComparator<ItemStack>
			IS_ITEM_EQUAL_IGNORE_DURABILITY = ItemStack::isItemEqualIgnoreDurability,
			ARE_ITEMS_EQUAL_IGNORE_DURABILITY = ItemStack::areItemsEqualIgnoreDurability,
			ITEM_EQUAL_IGNORE_DURABILITY = ITEM_EQUAL_IGNORE_DURABILITY_BUILDER.build();
	public static final Builder CAPS_COMPATIBLE_BUILDER = builder().noAddressShortCut().anyItem().anyCount().anyData().anyNBT().capsCompatible();
	public static final IEqualityComparator<ItemStack>
			ARE_CAPS_COMPATIBLE = ItemStack::areCapsCompatible,
			CAPS_COMPATIBLE = CAPS_COMPATIBLE_BUILDER.build();
	public static final Builder ITEM_STACKS_EQUAL_BUILDER = builder().noAddressShortCut().capsCompatible();
	public static final IEqualityComparator<ItemStack>
			ARE_ITEM_STACKS_EQUAL = ItemStack::areItemStacksEqual,
			ITEM_STACKS_EQUAL = ITEM_STACKS_EQUAL_BUILDER.build();
	public static final Builder ITEM_STACK_TAGS_EQUAL_BUILDER = builder().noAddressShortCut().anyItem().anyCount().anyData().capsCompatible();
	public static final IEqualityComparator<ItemStack>
			ARE_ITEM_STACK_TAGS_EQUAL = ItemStack::areItemStackTagsEqual,
			ITEM_STACK_TAGS_EQUAL = ITEM_STACK_TAGS_EQUAL_BUILDER.build();
	public static final IEqualityComparator<ItemStack> ARE_ITEM_STACK_SHARE_TAGS_EQUAL = ItemStack::areItemStackShareTagsEqual;
	public static final IEqualityComparator<ItemStack> ARE_ITEM_STACKS_EQUAL_USING_NBT_SHARE_TAG = ItemStack::areItemStacksEqualUsingNBTShareTag;
	public static final IEqualityComparator<ItemStack> STACKABLE = builder().anyCount().build();

	public static class Builder {
		private IEqualityComparator<Item> item = (a, b) -> a == b;
		private IIntEqualityComparator data = (a, b) -> a == b;
		private IIntEqualityComparator count = (a, b) -> a == b;
		private IEqualityComparator<NBTTagCompound> nbt = Objects::equals;
		private IEqualityComparator<ItemStack> shortcut = (a, b) -> a == b;
		private IEqualityComparator<ItemStack> empty = (a, b) -> a.isEmpty() == b.isEmpty() && a.isEmpty();
		private IEqualityComparator<ItemStack> ignoreDurability = (a, b) -> true;
		private IEqualityComparator<ItemStack> capsCompatible = (a, b) -> true;

		public Builder filterItem(IEqualityComparator<Item> item) {
			this.item = item;
			return this;
		}

		public Builder filterData(IIntEqualityComparator data) {
			this.data = data;
			return this;
		}

		public Builder filterCount(IIntEqualityComparator count) {
			this.count = count;
			return this;
		}

		public Builder filterNBT(IEqualityComparator<NBTTagCompound> nbt) {
			this.nbt = nbt;
			return this;
		}

		public Builder anyItem() {
			item = (a, b) -> true;
			return this;
		}

		public Builder anyData() {
			data = (a, b) -> true;
			return this;
		}

		public Builder anyCount() {
			count = (a, b) -> true;
			return this;
		}

		public Builder anyNBT() {
			nbt = (a, b) -> true;
			return this;
		}

		public Builder any() {
			empty = (a, b) -> true;
			return this;
		}

		public Builder biStateItem(Predicate<Item> predicate) {
			item = (a, b) -> predicate.test(a) == predicate.test(b);
			return this;
		}

		public Builder biStateData(IntPredicate predicate) {
			data = (a, b) -> predicate.test(a) == predicate.test(b);
			return this;
		}

		public Builder biStateData() {
			return biStateData(value -> value != 0);
		}

		public Builder biStateCount(IntPredicate predicate) {
			count = (a, b) -> predicate.test(a) == predicate.test(b);
			return this;
		}

		public Builder biStateNBT(Predicate<NBTTagCompound> predicate) {
			nbt = (a, b) -> predicate.test(a) == predicate.test(b);
			return this;
		}

		public Builder ignoreDurability() {
			ignoreDurability = (a, b) -> a.isItemStackDamageable() && b.isItemStackDamageable() || a.getMetadata() == b.getMetadata();
			return anyData();
		}

		public Builder capsCompatible() {
			capsCompatible = ItemStack::areCapsCompatible;
			return this;
		}

		public Builder noAddressShortCut() {
			shortcut = (a, b) -> false;
			return this;
		}

		public IEqualityComparator<ItemStack> build() {
			return (a, b) ->
					shortcut.areEqual(a, b)
							|| empty.areEqual(a, b)
							|| item.areEqual(a.getItem(), b.getItem())
							&& data.areEqual(a.getMetadata(), a.getMetadata())
							&& count.areEqual(a.getCount(), b.getCount())
							&& nbt.areEqual(a.getTagCompound(), a.getTagCompound())
							&& ignoreDurability.areEqual(a, b)
							&& capsCompatible.areEqual(a, b);
		}
	}

	public static Builder builder() {
		return new Builder();
	}
}
