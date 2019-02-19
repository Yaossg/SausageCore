package sausage_core.api.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Default implementation of {@link Container} for a {@link TileEntity}
 * */
public abstract class ContainerBase<T extends TileEntity> extends Container {
    public final T tileEntity;

    @SuppressWarnings("unchecked")
    public ContainerBase(TileEntity tileEntity) {
        this.tileEntity = (T) tileEntity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getDistanceSq(tileEntity.getPos()) <= 64;
    }

    /**
     * copy from {@link Container#mergeItemStack(ItemStack, int, int, boolean)}
     * but check if the item is valid for the slot
     */
    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean changed = false;
        int i = reverseDirection ? endIndex - 1 : startIndex;
        if(stack.isStackable()) {
            while (!stack.isEmpty()) {
                if(reverseDirection && i < startIndex) break;
                else if(i >= endIndex) break;
                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
                if(slot.isItemValid(itemstack)
                        && !itemstack.isEmpty()
                        && itemstack.getItem() == stack.getItem()
                        && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata())
                        && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
                    if(j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        changed = true;
                    } else if(itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.onSlotChanged();
                        changed = true;
                    }
                }
                i += reverseDirection ? -1 : 1;
            }
        }
        if(!stack.isEmpty()) {
            i = reverseDirection ? endIndex - 1 : startIndex;
            while (true) {
                if(reverseDirection && i < startIndex) break;
                else if(i >= endIndex) break;
                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
                if(itemstack.isEmpty() && slot.isItemValid(stack)) {
                    if(stack.getCount() > slot.getSlotStackLimit())
                        slot.putStack(stack.splitStack(slot.getItemStackLimit(stack)));
                    else slot.putStack(stack.splitStack(stack.getCount()));
                    slot.onSlotChanged();
                    changed = true;
                    break;
                }
                i += reverseDirection ? -1 : 1;
            }
        }
        return changed;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = inventorySlots.get(index);
        if(slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack newStack = slot.getStack(), oldStack = newStack.copy();
        boolean isMerged;
        int length = inventorySlots.size() - 36;
        if(index < length)
            isMerged = mergeItemStack(newStack, length, 36 + length, true);
        else if(index < 27 + length)
            isMerged = mergeItemStack(newStack, 0, length, false)
                    || mergeItemStack(newStack, 27 + length, 36 + length, false);
        else
            isMerged = mergeItemStack(newStack, 0, length, false)
                    || mergeItemStack(newStack, length, 27 + length, false);
        if(!isMerged) return ItemStack.EMPTY;
        if(newStack.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();
        return oldStack;
    }
}

