package sausage_core.api.util.oredict;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class OreDicts {
	private OreDicts() {}
	public static Stream<String> names(ItemStack stack) {
		return Arrays.stream(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName);
	}

	public static Optional<ItemStack> getOre(String name) {
		NonNullList<ItemStack> ores = OreDictionary.getOres(name);
		return ores.isEmpty() ? Optional.empty() : Optional.of(ores.get(0));
	}

	public static List<String> partsOf(String ore) {
		int begin = 0, end = 0;
		List<String> parts = new ArrayList<>();
		while (end < ore.length()) {
			while (++end < ore.length()
					&& Character.isLowerCase(ore.charAt(end))) ;
			parts.add(ore.substring(begin, end));
			begin = end;
		}
		return parts;
	}

	public static Optional<String> shapeOf(String ore) {
		List<String> parts = partsOf(ore);
		return parts.size() < 2 ? Optional.empty() : Optional.of(String.join("", parts.subList(0, parts.size() - 1)));
	}

	public static String materialOf(String ore) {
		List<String> parts = partsOf(ore);
		return parts.get(parts.size() - 1);
	}
}
