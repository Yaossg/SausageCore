package sausage_core.api.util.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface IItemCM {
	int colorMultiplier(ItemStack stack, int tintIndex);

	int TRANSPARENT = 0xFFFFFF;

	static IItemCM mappingBy(Int2ObjectMap<ToIntFunction<ItemStack>> map) {
		return (stack, tintIndex) -> {
			ToIntFunction<ItemStack> function = map.get(tintIndex);
			return function == null ? TRANSPARENT : function.applyAsInt(stack);
		};
	}

	static IItemCM mappingBy(Map<Integer, ToIntFunction<ItemStack>> map) {
		return (stack, tintIndex) -> map.getOrDefault(tintIndex, stack0 -> TRANSPARENT).applyAsInt(stack);
	}

	@SafeVarargs
	static IItemCM mappingBy(ToIntFunction<ItemStack>... map) {
		return (stack, tintIndex) -> 0 <= tintIndex && tintIndex < map.length
				? map[tintIndex].applyAsInt(stack) : TRANSPARENT;
	}
}