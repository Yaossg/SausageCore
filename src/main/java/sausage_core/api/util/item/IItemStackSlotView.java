package sausage_core.api.util.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import sausage_core.api.util.common.IEqualityComparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public interface IItemStackSlotView {
    PortableItemStackHandler handler();
    int slot();
    
    default void set(ItemStack stack) {
        handler().setStackInSlot(slot(), stack);
    }
    
    default Predicate<ItemStack> comparing(IEqualityComparator<ItemStack> comparator) {
        return stack -> comparator.areEqual(raw(), stack);
    }
    
    default ItemStack copy() {
        return raw().copy();
    }
    
    default Item getItem() {
        return raw().getItem();
    }
    
    default int getCount() {
        return raw().getCount();
    }

    default int getItemDamage() {
        return raw().getItemDamage();
    }
    
    default void setItemDamage(int data) {
        raw().setItemDamage(data);
        onContentChanged();
    }

    default int getMaxDamage() {
        return raw().getMaxDamage();
    }

    default boolean hasTagCompound() {
        return raw().hasTagCompound();
    }

    @Nullable
    default NBTTagCompound copyTagCompound() {
        NBTTagCompound compound = raw().getTagCompound();
        return compound == null ? null : compound.copy();
    }
    
    default void setTagCompound(@Nullable NBTTagCompound nbt) {
        raw().setTagCompound(nbt);
        onContentChanged();
    }
    
    default void setCount(int size) {
        raw().setCount(size);
        onContentChanged();
    }

    default boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return raw().hasCapability(capability, facing);
    }

    default void grow(int quantity) {
        setCount(getCount() + quantity);
    }
    default void shrink(int quantity) {
        grow(-quantity);
    }
    
    default ItemStack raw() {
        return handler().getStackInSlot(slot());
    }

    default void onContentChanged() {
        handler().onContentsChanged(slot());
    }
}
