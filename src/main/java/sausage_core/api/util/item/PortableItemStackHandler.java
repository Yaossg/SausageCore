package sausage_core.api.util.item;

import com.google.common.collect.Iterators;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PortableItemStackHandler extends ItemStackHandler implements Iterable<ItemStack> {
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
    public Iterator<ItemStack> iterator() {
        return Iterators.unmodifiableIterator(stacks.iterator());
    }

    public List<ItemStack> view() {
        return Collections.unmodifiableList(stacks);
    }

    public NonNullList<ItemStack> underlyingCopy() {
        return stacks.stream().map(ItemStack::copy).collect(Collectors.toCollection(NonNullList::create));
    }

    public PortableItemStackHandler copy() {
        return new PortableItemStackHandler(underlyingCopy());
    }

    @Override
    @Deprecated
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
    }
}
