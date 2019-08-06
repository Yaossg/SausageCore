package sausage_core.api.util.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.Map;

import static sausage_core.api.util.common.Conversions.stack2state;

@FunctionalInterface
public interface IBlockCM extends IItemCM {
	int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex);

	default int colorMultiplier(ItemStack stack, int tintIndex) {
		return colorMultiplier(stack2state(stack), null, null, tintIndex);
	}

	interface ISpecializedFunction {
		int applyAsInt(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos);
	}

	static IBlockCM mappingBy(Int2ObjectMap<ISpecializedFunction> map) {
		return (state, worldIn, pos, tintIndex) -> {
			ISpecializedFunction function = map.get(tintIndex);
			return function == null ? TRANSPARENT : function.applyAsInt(state, worldIn, pos);
		};
	}

	static IBlockCM mappingBy(Map<Integer, ISpecializedFunction> map) {
		return (state, worldIn, pos, tintIndex) ->
				map.getOrDefault(tintIndex, (state0, worldIn0, pos0) -> TRANSPARENT)
						.applyAsInt(state, worldIn, pos);
	}


	static IBlockCM mappingBy(ISpecializedFunction... map) {
		return (state, worldIn, pos, tintIndex) ->
				0 <= tintIndex && tintIndex < map.length
						? map[tintIndex].applyAsInt(state, worldIn, pos) : TRANSPARENT;
	}

}