package com.github.yaossg.sausage_core.api.util.common;

import com.google.common.collect.Comparators;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("unchecked")
public interface IItemComparators {
    Comparator<? extends IForgeRegistryEntry<?>> entry =
            Comparator.comparing(IForgeRegistryEntry::getRegistryName);
    Comparator<Item> item = (Comparator<Item>) entry;
    Comparator<ItemStack> itemStack =
            Comparator.comparing(ItemStack::getItem, item)
                    .thenComparing(Comparator.comparingInt(ItemStack::getCount).reversed());
    Comparator<Ingredient> ingredient =
            Comparator.<Ingredient, Iterable<ItemStack>>comparing(ingredient -> Arrays.asList(ingredient.getMatchingStacks()),
                    Comparators.lexicographical(itemStack));
    Comparator<IngredientStack> ingredientStack =
            Comparator.comparing(IngredientStack::getIngredient, ingredient)
                    .thenComparing(Comparator.comparingInt(IngredientStack::getCount).reversed());
}