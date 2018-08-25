package com.github.yaossg.sausage_core.api.util.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

/**
 * Default Inventory implementation for ItemStackHandler-like inventory
 * */
public interface IDefaultInventory extends IInventory {

    /**
     * returns all {@link ItemStackHandler}s of this inventory
     */
    default ItemStackHandler[] getItemStackHandlers() {
        return new ItemStackHandler[0];
    }

    /**
     * returns its tile
     * */
    default Optional<TileEntity> getTileEntity() {
        return Optional.empty();
    }

    @Override
    default int getSizeInventory() {
        int size = 0;
        for (ItemStackHandler handler : getItemStackHandlers())
            size += handler.getSlots();
        return size;
    }

    @Override
    default boolean isEmpty() {
        return getSizeInventory() == 0;
    }

    @Override
    default ItemStack getStackInSlot(int index) {
        for (ItemStackHandler handler : getItemStackHandlers()) {
            if(handler.getSlots() > index)
                return handler.getStackInSlot(index);
            index -= handler.getSlots();
        }
        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack decrStackSize(int index, int count) {
        for (ItemStackHandler handler : getItemStackHandlers()) {
            if(handler.getSlots() > index)
                return handler.getStackInSlot(index).splitStack(count);
            index -= handler.getSlots();
        }
        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack removeStackFromSlot(int index) {
        for (ItemStackHandler handler : getItemStackHandlers()) {
            if(handler.getSlots() > index)
                return handler.getStackInSlot(index).splitStack(64);
            index -= handler.getSlots();
        }
        return ItemStack.EMPTY;
    }

    @Override
    default void setInventorySlotContents(int index, ItemStack stack){
        for (ItemStackHandler handler : getItemStackHandlers()) {
            if(handler.getSlots() > index)
                handler.setStackInSlot(index, stack);
            index -= handler.getSlots();
        }
    }

    @Override
    default int getInventoryStackLimit() {
        return 64;
    }

    @Override
    default void markDirty() {
        getTileEntity().ifPresent(TileEntity::markDirty);
    }

    @Override
    default boolean isUsableByPlayer(EntityPlayer player) {
        return getTileEntity().map(tileEntity ->  player.getDistanceSq(tileEntity.getPos()) <= 64).orElse(true);
    }

    @Override
    default void openInventory(EntityPlayer player) {

    }

    @Override
    default void closeInventory(EntityPlayer player) {

    }

    @Override
    default boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    default int getField(int id) {
        return 0;
    }

    @Override
    default void setField(int id, int value) {}

    @Override
    default int getFieldCount() {
        return 0;
    }

    @Override
    default void clear() {
        for (ItemStackHandler handler : getItemStackHandlers())
            for (int i = 0; i < handler.getSlots(); i++)
                handler.setStackInSlot(i, ItemStack.EMPTY);
    }

    @Override
    default boolean hasCustomName() {
        return false;
    }

    @Override
    default ITextComponent getDisplayName() {
        return getTileEntity().map(TileEntity::getDisplayName).orElse(new TextComponentString(""));
    }

    @Override
    default String getName() {
        return getDisplayName().getUnformattedText();
    }
}
