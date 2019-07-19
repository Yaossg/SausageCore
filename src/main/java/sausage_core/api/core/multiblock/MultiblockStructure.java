package sausage_core.api.core.multiblock;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class MultiblockStructure {
	public final BlockPos master;
	public final List<BlockPos> blocks;
	public final BlockPos min, max;
	public final AxisAlignedBB bb;
	public final int dx, dy, dz;

	public MultiblockStructure(BlockPos master, List<BlockPos> blocks) {
		this.master = master;
		this.blocks = blocks;
		boolean has = false;
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for (BlockPos pos : blocks) {
			if (pos.getX() < minX) minX = pos.getX();
			if (pos.getX() > maxX) maxX = pos.getX();
			if (pos.getY() < minY) minY = pos.getY();
			if (pos.getY() > maxY) maxY = pos.getY();
			if (pos.getZ() < minZ) minZ = pos.getZ();
			if (pos.getZ() > maxZ) maxZ = pos.getZ();
			if (pos.equals(master)) has = true;
		}
		if (!has) throw new IllegalArgumentException();

		dx = maxX + 1 - minX;
		dy = maxY + 1 - minY;
		dz = maxZ + 1 - minZ;
		bb = new AxisAlignedBB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
		min = new BlockPos(minX, minY, minZ);
		max = new BlockPos(maxX, maxY, maxZ);
	}
}
