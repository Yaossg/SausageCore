package sausage_core.api.util.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;
import java.util.function.*;

public interface IWorldGenBuilder {
    static IWorldGenBuilder of(IWorldGenWrapper wrapper) {
        return new IWorldGenBuilder() {
            private ToDoubleFunction<Random> times = random -> 1;
            private BiFunction<Random, BlockPos, BlockPos> offsetEach = (random, blockPos) -> blockPos;
            private BiFunction<Random, BlockPos, BlockPos> offsetAll = (random, blockPos) -> blockPos;
            private Predicate<Biome> atBiome = biome -> true;
            private IntPredicate atDimensionA = i -> true;
            private Predicate<DimensionType> atDimensionB = type -> true;
            @Override
            public IWorldGenBuilder times(ToDoubleFunction<Random> function) {
                times = function;
                return this;
            }

            @Override
            public IWorldGenBuilder offsetEach(BiFunction<Random, BlockPos, BlockPos> offset) {
                offsetEach = offset;
                return this;
            }

            @Override
            public IWorldGenBuilder offsetAll(BiFunction<Random, BlockPos, BlockPos> offset) {
                offsetAll = offset;
                return this;
            }

            @Override
            public IWorldGenBuilder atBiome(Predicate<Biome> predicate) {
                atBiome = predicate;
                return this;
            }

            @Override
            public IWorldGenBuilder atDimension(IntPredicate predicate) {
                atDimensionA = predicate;
                return this;
            }

            @Override
            public IWorldGenBuilder atDimension(Predicate<DimensionType> predicate) {
                atDimensionB = predicate;
                return this;
            }

            @Override
            public IWorldGenBuilder copy() {
                return of(wrapper)
                        .times(times)
                        .offsetEach(offsetEach)
                        .offsetAll(offsetAll)
                        .atBiome(atBiome)
                        .atDimension(atDimensionA)
                        .atDimension(atDimensionB);
            }

            @Override
            public IWorldGenWrapper build() {
                return (random, world, pos) -> {
                    if(atBiome.test(world.getBiome(pos))
                            && atDimensionA.test(world.provider.getDimension())
                            && atDimensionB.test(world.provider.getDimensionType())) {
                        pos = offsetAll.apply(random, pos);
                        double chance = times.applyAsDouble(random);
                        do if(random.nextFloat() < chance)
                            wrapper.generate(random, world, offsetEach.apply(random, pos));
                        while (--chance > 0);
                    }
                };
            }
        };
    }

    default IWorldGenBuilder loop(int n) {
        return loop(random -> n);
    }
    default IWorldGenBuilder loop(ToIntFunction<Random> function) {
        return times(function::applyAsInt);
    }
    default IWorldGenBuilder rare(int n) {
        return times(random -> 1.0 / n);
    }
    default IWorldGenBuilder rare(ToIntFunction<Random> function) {
        return times(random -> 1.0 / function.applyAsInt(random));
    }

    default IWorldGenBuilder times(double n) {
        return times(random -> n);
    }
    IWorldGenBuilder times(ToDoubleFunction<Random> function);

    IWorldGenBuilder offsetEach(BiFunction<Random, BlockPos, BlockPos> offset);
    IWorldGenBuilder offsetAll(BiFunction<Random, BlockPos, BlockPos> offset);

    default IWorldGenBuilder atBiome(Biome biome) {
        return atBiome(b -> b == biome);
    }
    default IWorldGenBuilder atBiome(Biome... biomes) {
        return atBiome(b -> ArrayUtils.contains(biomes, b));
    }
    IWorldGenBuilder atBiome(Predicate<Biome> predicate);

    default IWorldGenBuilder atDimension(int n) {
        return atDimension((int i) -> i == n);
    }
    default IWorldGenBuilder atDimension(DimensionType type) {
        return atDimension((DimensionType t) -> t == type);
    }
    default IWorldGenBuilder atDimension(int... n) {
        return atDimension((int i) -> ArrayUtils.contains(n, i));
    }
    default IWorldGenBuilder atDimension(DimensionType... type) {
        return atDimension(((DimensionType t) -> ArrayUtils.contains(type, t)));
    }
    IWorldGenBuilder atDimension(IntPredicate predicate);
    IWorldGenBuilder atDimension(Predicate<DimensionType> predicate);
    IWorldGenBuilder copy();
    IWorldGenWrapper build();
}
