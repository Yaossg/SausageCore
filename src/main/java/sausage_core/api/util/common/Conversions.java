package sausage_core.api.util.common;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeHooks;
import sausage_core.api.util.nbt.NBTs;

import static sausage_core.api.util.common.Conversions.To.block;

/**
 * Conversions among {@link Item}, {@link Block}, {@link ItemStack} and {@link IBlockState}
 */
public final class Conversions {
	private static Item block2item(Block block) {
		return Item.getItemFromBlock(block);
	}

	private static Block item2block(Item item) {
		return Block.getBlockFromItem(item);
	}

	public static ItemStack state2stack(IBlockState state) {
		return state2stack(state, 1);
	}

	public static ItemStack state2stack(IBlockState state, int count) {
		Item item = block2item(state.getBlock());
		return new ItemStack(item, count, item.getHasSubtypes() ? state.getBlock().getMetaFromState(state) : 0);
	}

	@SuppressWarnings("deprecation")
	public static IBlockState stack2state(ItemStack stack) {
		Block block = item2block(stack.getItem());
		return block.getStateFromMeta(stack.getMetadata());
	}

	public static String stack2string(ItemStack stack) {
		StringBuilder builder = new StringBuilder();
		if (stack.getCount() > 1) {
			builder.append(stack.getCount());
			builder.append('*');
		}
		builder.append(stack.getItem().getRegistryName());
		if (stack.getHasSubtypes() || stack.isItemStackDamageable()) {
			builder.append(':');
			builder.append(stack.getMetadata());
		}
		if (stack.hasTagCompound()) {
			builder.append(stack.getTagCompound());
		}
		return builder.toString();
	}

	public static String state2string(IBlockState state) {
		return state.getBlock().getRegistryName() + state.getProperties().toString();
	}

	/**
	 * Stores a TE with its NBT but without its states into a stack
	 */
	public static ItemStack TE2stack(TileEntity tileEntity) {
		ItemStack stack = new ItemStack(tileEntity.getBlockType());
		NBTTagCompound nbt = new NBTTagCompound();
		tileEntity.writeToNBT(nbt);
		nbt.removeTag("id");
		nbt.removeTag("x");
		nbt.removeTag("y");
		nbt.removeTag("z");
		stack.setTagCompound(NBTs.of("tile", nbt));
		return stack;
	}

	/**
	 * Restore TE's NBTs from stack
	 */
	public static void stack2TE(ItemStack stack, TileEntity tileEntity) {
		if (tileEntity == null || block(stack.getItem()) != tileEntity.getBlockType()) return;
		NBTTagCompound tile = stack.getSubCompound("tile");
		if (tile != null) {
			tile = tile.copy();
			tile.setString("id", ForgeHooks.getRegistryName(tileEntity.getClass()));
			BlockPos pos = tileEntity.getPos();
			tile.setInteger("x", pos.getX());
			tile.setInteger("y", pos.getY());
			tile.setInteger("z", pos.getZ());
			tileEntity.readFromNBT(tile);
		}
	}

	/**
	 * Convert to specified type (which is the same as its method name)
	 * DO NOT support state(), you should use {@link #stack2state(ItemStack)} or {@link Block#getDefaultState()} instead
	 */
	public static class To {
		public static Item item(Block block) {
			return block2item(block);
		}

		public static Item item(ItemStack stack) {
			return stack.getItem();
		}

		public static Item item(IBlockState state) {
			return block2item(block(state));
		}

		public static Block block(Item item) {
			return item2block(item);
		}

		public static Block block(ItemStack stack) {
			return item2block(item(stack));
		}

		public static Block block(IBlockState state) {
			return state.getBlock();
		}

		public static ItemStack stack(Item item) {
			return stack(item, 1);
		}

		public static ItemStack stack(Block block) {
			return stack(block, 1);
		}

		public static ItemStack stack(IBlockState state) {
			return stack(state, 1);
		}

		public static ItemStack stack(Item item, int count) {
			return new ItemStack(item, count);
		}

		public static ItemStack stack(Block block, int count) {
			return new ItemStack(block, count);
		}

		public static ItemStack stack(ItemStack stack, int count) {
			ItemStack copy = stack.copy();
			copy.setCount(count);
			return copy;
		}

		public static ItemStack stack(IBlockState state, int count) {
			return state2stack(state, count);
		}
	}
}
