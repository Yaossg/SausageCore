package com.github.yaossg.sausage_core.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.yaossg.sausage_core.api.util.Conversions.To.stack;

public class OreDicts {
    public static Stream<String> names(ItemStack stack) {
        return IntStream.of(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName);
    }
    public static Optional<ItemStack> replaceOreHead(ItemStack item, String src, String dst) {
        return replaceOreHead(item, src, dst, item.getCount());
    }
    public static Optional<ItemStack> replaceOreHead(ItemStack item, String src, String dst, int count) {
        return names(item)
                .filter(str -> str.startsWith(src))
                .map(str -> str.replaceFirst(src, dst))
                .map(OreDictionary::getOres)
                .flatMap(NonNullList::stream)
                .findAny()
                .map(stack -> stack(stack, count));
    }
}
