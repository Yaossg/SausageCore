package sausage_core.api.core.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sausage_core.api.util.common.LazyOptional;

public interface IMultiBlockDetector {
	/**
	 * @param master The pos of the master block if present, any block inside the structure otherwise
	 * */
	LazyOptional<MultiblockStructure> detect(IBlockAccess world, BlockPos master);
}
