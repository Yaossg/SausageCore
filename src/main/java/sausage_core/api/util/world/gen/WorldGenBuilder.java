package sausage_core.api.util.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;
import java.util.function.*;

public class WorldGenBuilder {
	public WorldGenBuilder(IWorldGenWrapper wrapper) {
		this.wrapper = wrapper;
	}

	private final IWorldGenWrapper wrapper;
	private ToDoubleFunction<Random> times = random -> 1;
	private BiFunction<Random, BlockPos, BlockPos> offsetEach = (random, blockPos) -> blockPos;
	private BiFunction<Random, BlockPos, BlockPos> offsetAll = (random, blockPos) -> blockPos;
	private Predicate<Biome> atBiome = biome -> true;
	private IntPredicate atDimensionA = i -> true;
	private Predicate<DimensionType> atDimensionB = type -> true;

	public WorldGenBuilder times(ToDoubleFunction<Random> function) {
		times = function;
		return this;
	}

	public WorldGenBuilder offsetEach(BiFunction<Random, BlockPos, BlockPos> offset) {
		offsetEach = offset;
		return this;
	}

	public WorldGenBuilder offsetAll(BiFunction<Random, BlockPos, BlockPos> offset) {
		offsetAll = offset;
		return this;
	}

	public WorldGenBuilder atBiome(Predicate<Biome> predicate) {
		atBiome = predicate;
		return this;
	}

	public WorldGenBuilder atDimension(IntPredicate predicate) {
		atDimensionA = predicate;
		return this;
	}

	public WorldGenBuilder atDimension(Predicate<DimensionType> predicate) {
		atDimensionB = predicate;
		return this;
	}

	public WorldGenBuilder copy() {
		return new WorldGenBuilder(wrapper)
				.times(times)
				.offsetEach(offsetEach)
				.offsetAll(offsetAll)
				.atBiome(atBiome)
				.atDimension(atDimensionA)
				.atDimension(atDimensionB);
	}

	public IWorldGenWrapper build() {
		return (random, world, pos) -> {
			if(atBiome.test(world.getBiome(pos))
					&& atDimensionA.test(world.provider.getDimension())
					&& atDimensionB.test(world.provider.getDimensionType())) {
				pos = offsetAll.apply(random, pos);
				double chance = times.applyAsDouble(random);
				do if(random.nextFloat() < chance)
					wrapper.generate(random, world, offsetEach.apply(random, pos));
				while(--chance > 0);
			}
		};
	}

	public WorldGenBuilder loop(int n) {
		return loop(random -> n);
	}

	public WorldGenBuilder loop(ToIntFunction<Random> function) {
		return times(function::applyAsInt);
	}

	public WorldGenBuilder rare(int n) {
		return times(random -> 1.0 / n);
	}

	public WorldGenBuilder rare(ToIntFunction<Random> function) {
		return times(random -> 1.0 / function.applyAsInt(random));
	}

	public WorldGenBuilder times(double n) {
		return times(random -> n);
	}

	public WorldGenBuilder atBiome(Biome biome) {
		return atBiome(b -> b == biome);
	}

	public WorldGenBuilder atBiome(Biome... biomes) {
		return atBiome(b -> ArrayUtils.contains(biomes, b));
	}

	public WorldGenBuilder atDimension(int n) {
		return atDimension((int i) -> i == n);
	}

	public WorldGenBuilder atDimension(DimensionType type) {
		return atDimension((DimensionType t) -> t == type);
	}

	public WorldGenBuilder atDimension(int... n) {
		return atDimension((int i) -> ArrayUtils.contains(n, i));
	}

	public WorldGenBuilder atDimension(DimensionType... type) {
		return atDimension(((DimensionType t) -> ArrayUtils.contains(type, t)));
	}
}
