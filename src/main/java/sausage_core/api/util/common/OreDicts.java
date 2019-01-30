package sausage_core.api.util.common;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public final class OreDicts {
    public static Stream<String> names(ItemStack stack) {
        return Arrays.stream(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName);
    }

    public static boolean startsWith(ItemStack stack, String prefix) {
        return names(stack).anyMatch(s -> s.startsWith(prefix));
    }

    public static Optional<ItemStack> getOre(String name) {
        NonNullList<ItemStack> ores = OreDictionary.getOres(name);
        return ores.isEmpty() ? Optional.empty() : Optional.of(ores.get(0));
    }
}
