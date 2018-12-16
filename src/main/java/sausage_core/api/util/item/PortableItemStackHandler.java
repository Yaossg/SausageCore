package sausage_core.api.util.item;

import com.google.common.collect.Iterators;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sausage_core.api.util.common.IEqualityComparator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PortableItemStackHandler extends ItemStackHandler implements Iterable<IItemStackSlotView> {

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

    public List<IItemStackSlotView> view() {
        List<IItemStackSlotView> list = new ArrayList<>(stacks.size());
        for (int i = 0; i < stacks.size(); ++i) list.add(new ItemStackSlotView(this, i));
        return Collections.unmodifiableList(list);
    }

    public NonNullList<ItemStack> copyStacks() {
        return stacks.stream().map(ItemStack::copy).collect(Collectors.toCollection(NonNullList::create));
    }

    public PortableItemStackHandler copy() {
        return new PortableItemStackHandler(copyStacks());
    }

    public static class ItemStackSlotView implements IItemStackSlotView {
        private final PortableItemStackHandler handler;
        private final int slot;

        ItemStackSlotView(PortableItemStackHandler handler, int slot) {
            this.handler = handler;
            this.slot = slot;
        }

        private ItemStack raw() {
            return handler.getStackInSlot(slot);
        }

        private void changed() {
            handler.onContentsChanged(slot);
        }

        @Override
        public PortableItemStackHandler handler() {
            return handler;
        }

        @Override
        public int slot() {
            return slot;
        }

        @Override
        public Predicate<ItemStack> compare(IEqualityComparator<ItemStack> comparator) {
            return stack -> comparator.areEqual(raw(), stack);
        }

        @Override
        public ItemStack copy() {
            return raw().copy();
        }

        @Override
        public Item getItem() {
            return raw().getItem();
        }

        @Override
        public int getCount() {
            return raw().getCount();
        }

        @Override
        public int getItemDamage() {
            return raw().getItemDamage();
        }

        @Override
        public void setItemDamage(int data) {
            raw().setItemDamage(data);
            changed();
        }

        @Override
        public int getMaxDamage() {
            return raw().getMaxDamage();
        }

        @Override
        public boolean hasTagCompound() {
            return raw().hasTagCompound();
        }

        @Nullable
        @Override
        public NBTTagCompound copyTagCompound() {
            NBTTagCompound compound = raw().getTagCompound();
            return compound == null ? null : compound.copy();
        }

        @Override
        public void setTagCompound(NBTTagCompound nbt) {
            raw().setTagCompound(nbt);
            changed();
        }

        @Override
        public void setCount(int size) {
            raw().setCount(size);
            changed();
        }
    }
}
