package sausage_core.api.util.common;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.stream.Stream;

public final class OreDicts {
    public static Stream<String> names(ItemStack stack) {
        return Arrays.stream(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName);
    }

    public static boolean startsWith(ItemStack stack, String prefix) {
        return names(stack).anyMatch(s -> s.startsWith(prefix));
    }

    public static ItemStack replaceOreHead(ItemStack stack, String src, String dst) {
        return names(stack)
                .filter(str -> str.startsWith(src))
                .map(str -> str.replaceFirst(src, dst))
                .map(OreDictionary::getOres)
                .flatMap(NonNullList::stream)
                .findAny()
                .map(ItemStack::copy)
                .orElse(ItemStack.EMPTY);
    }
}
