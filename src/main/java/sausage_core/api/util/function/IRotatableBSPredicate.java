package sausage_core.api.util.function;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;

import java.util.function.Predicate;

public interface IRotatableBSPredicate extends Predicate<IBlockState> {
	default IRotatableBSPredicate rotate(Rotation rotation) {
		return this;
	}

	static IRotatableBSPredicate of(Block block) {
		return $ -> $.getBlock() == block;
	}

	static IRotatableBSPredicate of(IBlockState state, IEqualityComparator<IBlockState> underlying) {
		return new IRotatableBSPredicate() {
			@Override
			public boolean test(IBlockState state0) {
				return underlying.areEqual(state0, state);
			}

			@Override
			public IRotatableBSPredicate rotate(Rotation rotation) {
				return of(state.withRotation(rotation), underlying);
			}
		};
	}
}
