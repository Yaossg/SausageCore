package sausage_core.api.util.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public interface IItemStackHandlerView extends IItemHandler {
	default boolean isInput() {
		return true;
	}

	default boolean isOutput() {
		return true;
	}

	default IItemHandler getAsInput() {
		return new IItemStackHandlerView() {
			private final IItemStackHandlerView view = IItemStackHandlerView.this;

			@Override
			public int getSlots() {
				return view.getSlots();
			}

			@Nonnull
			@Override
			public ItemStack getStackInSlot(int slot) {
				return view.getStackInSlot(slot);
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				return view.insertItem(slot, stack, simulate);
			}

			@Nonnull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				return ItemStack.EMPTY;
			}

			@Override
			public int getSlotLimit(int slot) {
				return view.getSlotLimit(slot);
			}

			@Override
			public boolean isOutput() {
				return false;
			}
		};
	}

	default IItemStackHandlerView getAsOutput() {
		return new IItemStackHandlerView() {
			private final IItemStackHandlerView view = IItemStackHandlerView.this;

			@Override
			public int getSlots() {
				return view.getSlots();
			}

			@Nonnull
			@Override
			public ItemStack getStackInSlot(int slot) {
				return view.getStackInSlot(slot);
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				return stack;
			}

			@Nonnull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				return view.extractItem(slot, amount, simulate);
			}

			@Override
			public int getSlotLimit(int slot) {
				return view.getSlotLimit(slot);
			}

			@Override
			public boolean isInput() {
				return false;
			}
		};
	}
}
