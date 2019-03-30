package sausage_core.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

public class ItemWildcardIngredientFactory implements IIngredientFactory {
	@Nonnull
	@Override
	public Ingredient parse(JsonContext context, JsonObject json) {
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(context.appendModId(JsonUtils.getString(json, "item"))));
		if(item == null)
			throw new JsonSyntaxException("Unknown item \'" + JsonUtils.getString(json, "item") + "\'");
		NonNullList<ItemStack> list = NonNullList.create();
		item.getSubItems(CreativeTabs.SEARCH, list);
		return Ingredient.fromStacks(list.toArray(new ItemStack[0]));
	}
}
