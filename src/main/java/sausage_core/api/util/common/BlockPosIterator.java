package sausage_core.api.util.common;

import com.google.common.collect.AbstractIterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

/**
 * same as {@link BlockPos#getAllInBox(BlockPos, BlockPos)} but NBTSerializable
 */
public class BlockPosIterator extends AbstractIterator<BlockPos> {
	public BlockPosIterator(BlockPos from, BlockPos to) {
		x1 = from.getX();
		y1 = from.getY();
		z1 = from.getZ();
		x2 = to.getX();
		y2 = to.getY();
		z2 = to.getZ();
		x = x1;
		y = y1;
		z = z1;
	}

	public BlockPosIterator(BlockPos from, BlockPos to, BlockPos current) {
		this(from, to);
		x = current.getX();
		y = current.getY();
		z = current.getZ();
	}

	public BlockPosIterator(NBTTagCompound compound) {
		this(NBTUtil.getPosFromTag(compound.getCompoundTag("from")),
				NBTUtil.getPosFromTag(compound.getCompoundTag("to")),
				NBTUtil.getPosFromTag(compound.getCompoundTag("current")));
	}

	public BlockPos getCurrent() {
		return new BlockPos(x, y, z);
	}

	private boolean first = true;
	private int x, y, z;
	private final int x1, y1, z1;
	private final int x2, y2, z2;

	protected BlockPos computeNext() {
		if(first) {
			first = false;
			return new BlockPos(x, y, z);
		} else if(x == x2 && y == y2 && z == z2) {
			return endOfData();
		} else {
			if(x < x2) {
				++x;
			} else if(y < y2) {
				x = x1;
				++y;
			} else if(z < z2) {
				x = x1;
				y = y1;
				++z;
			}
			return new BlockPos(x, y, z);
		}
	}

	private static NBTTagCompound toNBT(int x, int y, int z) {
		return NBTUtil.createPosTag(new BlockPos(x, y, z));
	}

	public NBTTagCompound toNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("from", toNBT(x1, y1, z1));
		compound.setTag("to", toNBT(x2, y2, z2));
		compound.setTag("current", toNBT(x, y, z));
		return compound;
	}
}
