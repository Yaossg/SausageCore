package sausage_core.api.core.multiblock;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sausage_core.api.util.common.LazyOptional;
import sausage_core.api.util.function.IRotatableBSPredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Fixed sized and pattern structure detector
 * The usage is very similar to {@link net.minecraft.block.state.pattern.BlockPattern}
 * but much more faster than that so there's no need to worry about efficiency
 * */
public class SimpleFixedDetector implements IMultiBlockDetector {
	private final List<Map<BlockPos, IRotatableBSPredicate>> matchers = new ArrayList<>(12);

	public SimpleFixedDetector(Map<BlockPos, IRotatableBSPredicate> matcher) {
		for (Rotation rotation : Rotation.values())
			matchers.add(translate(matcher, rotation));
	}

	private Map<BlockPos, IRotatableBSPredicate> translate(Map<BlockPos, IRotatableBSPredicate> matcher, Rotation rotation) {
		Map<BlockPos, IRotatableBSPredicate> map = new HashMap<>();
		for (Map.Entry<BlockPos, IRotatableBSPredicate> entry : matcher.entrySet())
			map.put(entry.getKey().rotate(rotation), entry.getValue().rotate(rotation));
		return map;
	}

	@Override
	public LazyOptional<MultiblockStructure> detect(IBlockAccess world, BlockPos master) {
		for (Map<BlockPos, IRotatableBSPredicate> matcher : matchers) {
			boolean matched = true;
			for (Map.Entry<BlockPos, IRotatableBSPredicate> entry : matcher.entrySet()) {
				if (!entry.getValue().test(world.getBlockState(master.add(entry.getKey())))) {
					matched = false;
					break;
				}
			}
			if (matched) {
				return LazyOptional.of(() -> new MultiblockStructure(master,
						matcher.keySet().stream().map(pos -> pos.add(master)).collect(Collectors.toList())));
			}
		}
		return LazyOptional.empty();
	}

	public static PatternBuilder patternBuilder(IRotatableBSPredicate master) {
		return new PatternBuilder(master);
	}

	public static class PatternBuilder {
		private Int2ObjectMap<String[]> layers = new Int2ObjectArrayMap<>();
		private Char2ObjectMap<IRotatableBSPredicate> mappings = new Char2ObjectArrayMap<>();

		public PatternBuilder(IRotatableBSPredicate master) {
			mappings.put('M', master);
		}

		public PatternBuilder layer(int y, String... layer) {
			layers.put(y, layer);
			return this;
		}

		public PatternBuilder mapping(char ch, IRotatableBSPredicate state) {
			if (ch == ' ' || mappings.containsKey(ch)) throw new IllegalArgumentException();
			mappings.put(ch, state);
			return this;
		}

		public SimpleFixedDetector build() {
			String[] layers0 = layers.get(0);
			int masterX = 0, masterZ = 0;
			boolean masterFound = false;
			for (int z = 0; z < layers0.length; ++z) {
				int x = layers0[z].indexOf('M');
				if (x != -1) {
					masterX = x;
					masterZ = z;
					masterFound = true;
					break;
				}
			}
			if (!masterFound) throw new IllegalArgumentException();
			Map<BlockPos, IRotatableBSPredicate> map = new HashMap<>();
			for (Int2ObjectMap.Entry<String[]> entry : layers.int2ObjectEntrySet()) {
				int y = entry.getIntKey();
				String[] layer = entry.getValue();
				for (int z = 0; z < layer.length; ++z) {
					for (int x = 0; x < layer[z].length(); ++x) {
						char key = layer[z].charAt(x);
						if (key == ' ') continue;
						IRotatableBSPredicate state = mappings.get(key);
						map.put(new BlockPos(x - masterX, y, z - masterZ), state);
					}
				}
			}
			return new SimpleFixedDetector(map);
		}
	}
}
