package sausage_core.api.util.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class SingleItemStackHandler extends ItemStackHandler {
    public SingleItemStackHandler() {}
    public SingleItemStackHandler(ItemStack stack) {
        stacks = NonNullList.create();
        stacks.add(stack);
    }

    @Override
    public void setSize(int size) {
        if(size != 1)
            throw new UnsupportedOperationException();
    }

    public void setStack(ItemStack stack) {
        super.setStackInSlot(0, stack);
    }

    @Nonnull
    public ItemStack getStack() {
        return super.getStackInSlot(0);
    }

    @Nonnull
    public ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(0, stack, simulate);
    }

    @Nonnull
    public ItemStack extractItem(int amount, boolean simulate) {
        return super.extractItem(0, amount, simulate);
    }

    public SingleItemStackHandler copy() {
        return new SingleItemStackHandler(getStack().copy());
    }

    @Override
    @Deprecated
    protected final void onContentsChanged(int slot) {
        onContentsChanged();
    }

    protected void onContentsChanged() {

    }
}
