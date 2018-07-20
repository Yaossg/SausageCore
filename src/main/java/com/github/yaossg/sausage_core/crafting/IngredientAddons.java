package com.github.yaossg.sausage_core.crafting;

import com.github.yaossg.sausage_core.SausageCore;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IngredientAddons {
    static BiPredicate<String, String> getBiPredicate(String predicate) {
        switch (predicate) {
            case "startsWith":
                return String::startsWith;
            case "endsWith":
                return String::endsWith;
            case "contains":
                return String::contains;
            case "matches":
                return String::matches;
            case "equals":
                return String::equals;
            case "equalsIgnoreCase":
                return String::equalsIgnoreCase;
            default:
                throw new JsonSyntaxException("Unknown predicate \'" + predicate + "\'");
        }
    }
    static Predicate<String> getPredicate(JsonObject json) {
        return name -> getBiPredicate(JsonUtils.getString(json, "predicate")).test(name, JsonUtils.getString(json, "argument"));
    }
    static void register(String name, IIngredientFactory factory) {
        CraftingHelper.register(new ResourceLocation(SausageCore.MODID, name), factory);
    }
    public static void init() {
        register("ore", (context, json) ->
                new CompoundIngredient(Arrays
                        .stream(OreDictionary.getOreNames())
                        .filter(getPredicate(json))
                        .map(OreIngredient::new)
                        .collect(Collectors.toList())) {}
        );
        register("item_wildcard", (context, json) -> {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(context.appendModId(JsonUtils.getString(json, "item"))));
            if(item == null)
                throw new JsonSyntaxException("Unknown item \'" + JsonUtils.getString(json, "item") + "\'");
            NonNullList<ItemStack> list = NonNullList.create();
            item.getSubItems(CreativeTabs.SEARCH, list);
            return Ingredient.fromStacks(list.toArray(new ItemStack[0]));
        });
    }
}
