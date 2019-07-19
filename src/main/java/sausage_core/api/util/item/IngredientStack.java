package sausage_core.api.util.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import sausage_core.api.util.common.JsonRecipeHelper;

import javax.annotation.concurrent.Immutable;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.*;
import static sausage_core.api.util.common.Conversions.To.stack;

/**
 * {@link Ingredient} with a count
 */
@Immutable
public final class IngredientStack implements Predicate<ItemStack> {
	private final Ingredient ingredient;
	private final int count;

	public IngredientStack(Ingredient ingredient) {
		this(ingredient, 1);
	}

	public IngredientStack(Ingredient ingredient, int count) {
		checkArgument(0 < count && count <= 64, "Invalid count (%s), it should be (0, 64]", count);
		this.ingredient = checkNotNull(ingredient);
		this.count = count;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public int getCount() {
		return count;
	}

	public IngredientStack withCount(int count) {
		return new IngredientStack(ingredient, count);
	}

	@Override
	public boolean test(ItemStack input) {
		return input.getCount() >= count && ingredient.apply(input);
	}

	public ItemStack[] getMatchingStacks() {
		return Arrays.stream(ingredient.getMatchingStacks())
				.map(stack -> stack(stack, count))
				.toArray(ItemStack[]::new);
	}

	private static OptionalInt seekCount(JsonElement json) {
		OptionalInt count = OptionalInt.empty();
		if (json.isJsonArray()) {
			for (JsonElement element : json.getAsJsonArray()) {
				if (element.isJsonObject()) {
					JsonObject object = element.getAsJsonObject();
					if ("minecraft:empty".equals(JsonUtils.getString(object, "type", "minecraft:item"))) {
						if (object.has("count")) {
							checkState(!count.isPresent(), "multi-defined count in one ingredient stack");
							count = OptionalInt.of(object.getAsInt());
						}
					}
				}
			}
		}
		if (json.isJsonObject()) {
			JsonObject object = json.getAsJsonObject();
			if (object.has("count"))
				count = OptionalInt.of(JsonUtils.getInt(object, "count"));
		}
		return count;
	}

	public static IngredientStack parse(JsonElement json) {
		return new IngredientStack(JsonRecipeHelper.getIngredient(json), seekCount(json).orElse(1));
	}
}
