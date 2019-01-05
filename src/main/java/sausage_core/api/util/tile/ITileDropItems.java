package sausage_core.api.util.tile;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.minecraft.inventory.InventoryHelper.spawnItemStack;

public interface ITileDropItems {
    default IItemHandler[] getItemStackHandlers() {
        return new ItemStackHandler[0];
    }

    default ItemStack[] getItemStacks() {
        return new ItemStack[0];
    }

    default List<ItemStack> getItemStackList() {
        return Collections.emptyList();
    }

    default NonNullList<ItemStack> getDrops() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (IItemHandler handler : getItemStackHandlers())
            for (int i = 0; i < handler.getSlots(); i++)
                drops.add(handler.getStackInSlot(i));
        Collections.addAll(drops, getItemStacks());
        drops.addAll(getItemStackList());
        return drops;
    }

    static void dropOne(World world, BlockPos pos, ItemStack stack) {
        spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }

    static void dropAll(@Nullable TileEntity tileEntity) {
        if(tileEntity instanceof ITileDropItems) {
            ((ITileDropItems) tileEntity).getDrops()
                    .stream()
                    .filter(drop -> !drop.isEmpty())
                    .forEach(drop -> dropOne(tileEntity.getWorld(), tileEntity.getPos(), drop));
        }
    }

    // ============================================================  //

    static ItemStack dropWithNBT(Block block, TileEntity tileEntity) {
        ItemStack drop = new ItemStack(block);
        if(tileEntity != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            tileEntity.writeToNBT(nbt);
            drop.setTagCompound(nbt);
        }
        return drop;
    }
}
