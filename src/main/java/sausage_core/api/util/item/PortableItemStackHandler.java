package sausage_core.api.util.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PortableItemStackHandler extends ItemStackHandler implements Iterable<IItemStackSlotView>, IItemStackHandlerView {
	public PortableItemStackHandler() {
		super();
	}

	public PortableItemStackHandler(int size) {
		super(size);
	}

	public PortableItemStackHandler(NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	public PortableItemStackHandler(IItemHandler handler) {
		super(handler.getSlots());
		for (int i = 0; i < handler.getSlots(); ++i) stacks.set(i, handler.getStackInSlot(i));
	}

	@Override
	public Iterator<IItemStackSlotView> iterator() {
		return view().iterator();
	}

	private List<IItemStackSlotView> view = null;

	public List<IItemStackSlotView> view() {
		if (view == null) {
			List<IItemStackSlotView> list = new ArrayList<>(stacks.size());
			for (int i = 0; i < stacks.size(); ++i) list.add(new ItemStackSlotView(this, i));
			view = Collections.unmodifiableList(list);
		}
		return view;
	}

	public NonNullList<ItemStack> copyStacks() {
		return stacks.stream().map(ItemStack::copy).collect(Collectors.toCollection(NonNullList::create));
	}

	public PortableItemStackHandler copy() {
		return new PortableItemStackHandler(copyStacks());
	}

	@Override
	public void onContentsChanged(int slot) {
		super.onContentsChanged(slot);
	}

	@Override
	public void setSize(int size) {
		super.setSize(size);
		view = null;
	}

	public static class ItemStackSlotView implements IItemStackSlotView {
		private final PortableItemStackHandler handler;
		private final int slot;

		ItemStackSlotView(PortableItemStackHandler handler, int slot) {
			this.handler = handler;
			this.slot = slot;
		}

		@Override
		public PortableItemStackHandler handler() {
			return handler;
		}

		@Override
		public int slot() {
			return slot;
		}
	}
}
