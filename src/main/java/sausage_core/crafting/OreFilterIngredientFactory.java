package sausage_core.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OreFilterIngredientFactory implements IIngredientFactory {
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

	@Nonnull
	@Override
	public Ingredient parse(JsonContext context, JsonObject json) {
		return new CompoundIngredient(Arrays
				.stream(OreDictionary.getOreNames())
				.filter(getPredicate(json))
				.map(OreIngredient::new)
				.collect(Collectors.toList())) {
		};
	}
}
