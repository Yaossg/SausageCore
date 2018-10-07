package sausage_core.api.util.common;

import com.google.common.collect.Comparators;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("unchecked")
public interface IItemComparators {
    Comparator<? extends IForgeRegistryEntry<?>> entry =
            Comparator.comparing(IForgeRegistryEntry::getRegistryName);

    static <T extends IForgeRegistryEntry<T>> Comparator<T> forgeRegistry() {
        return (Comparator<T>) entry;
    }

    Comparator<ItemStack> itemStack =
            Comparator.comparing(ItemStack::getItem, forgeRegistry())
                    .thenComparing(Comparator.comparingInt(ItemStack::getCount).reversed());
    Comparator<Ingredient> ingredient =
            Comparator.<Ingredient, Iterable<ItemStack>>comparing(ingredient -> Arrays.asList(ingredient.getMatchingStacks()),
                    Comparators.lexicographical(itemStack));
    Comparator<IngredientStack> ingredientStack =
            Comparator.comparing(IngredientStack::getIngredient, ingredient)
                    .thenComparing(Comparator.comparingInt(IngredientStack::getCount).reversed());
}