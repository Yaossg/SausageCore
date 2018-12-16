package sausage_core.api.util.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import sausage_core.api.util.common.IEqualityComparator;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public interface IItemStackSlotView {
    PortableItemStackHandler handler();
    int slot();
    Predicate<ItemStack> compare(IEqualityComparator<ItemStack> comparator);
    ItemStack copy();
    Item getItem();
    int getCount();
    void setCount(int size);
    default void grow(int quantity) {
        setCount(getCount() + quantity);
    }
    default void shrink(int quantity) {
        grow(-quantity);
    }
    int getItemDamage();
    void setItemDamage(int data);
    int getMaxDamage();
    boolean hasTagCompound();
    @Nullable
    NBTTagCompound copyTagCompound();
    void setTagCompound(NBTTagCompound nbt);
}
