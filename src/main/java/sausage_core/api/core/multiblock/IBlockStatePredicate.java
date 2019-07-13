package sausage_core.api.core.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import sausage_core.api.util.common.IEqualityComparator;

import java.util.function.Predicate;

public interface IBlockStatePredicate extends Predicate<IBlockState> {
	default IBlockStatePredicate rotate(Rotation rotation) {
		return this;
	}

	static IBlockStatePredicate of(Block block) {
		return $ -> $.getBlock() == block;
	}

	static IBlockStatePredicate of(IBlockState state, IEqualityComparator<IBlockState> underlying) {
		return new IBlockStatePredicate() {
			@Override
			public boolean test(IBlockState state0) {
				return underlying.areEqual(state0, state);
			}

			@Override
			public IBlockStatePredicate rotate(Rotation rotation) {
				return of(state.withRotation(rotation), underlying);
			}
		};
	}
}
