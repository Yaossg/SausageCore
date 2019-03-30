package sausage_core.api.core.tile;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Collections;

public interface ITileDropItems {
	default IItemHandler[] getItemStackHandlers() {
		return new ItemStackHandler[0];
	}

	default ItemStack[] getItemStacks() {
		return new ItemStack[0];
	}

	default NonNullList<ItemStack> getDrops() {
		NonNullList<ItemStack> drops = NonNullList.create();
		for(IItemHandler handler : getItemStackHandlers())
			for(int i = 0; i < handler.getSlots(); i++)
				drops.add(handler.getStackInSlot(i));
		Collections.addAll(drops, getItemStacks());
		return drops;
	}

	static void drop(World world, BlockPos pos, ItemStack stack) {
		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	}

	static void dropAll(@Nullable TileEntity tileEntity) {
		if(tileEntity instanceof ITileDropItems) {
			((ITileDropItems) tileEntity).getDrops()
					.stream()
					.filter(drop -> !drop.isEmpty())
					.forEach(drop -> drop(tileEntity.getWorld(), tileEntity.getPos(), drop));
		}
	}
}
