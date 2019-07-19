package sausage_core.api.util.common;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class JsonRecipeHelper {
	private static final JsonContext MINECRAFT = new JsonContext("minecraft");

	public static Ingredient getIngredient(JsonElement json) {
		return CraftingHelper.getIngredient(json, MINECRAFT);
	}

	public static ItemStack getItemStack(JsonObject json) {
		String itemName = JsonUtils.getString(json, "item");

		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

		if (item == null)
			throw new JsonSyntaxException("Unknown item '" + itemName + "'");

		if (item.getHasSubtypes() && !json.has("data"))
			throw new JsonParseException("Missing data for item '" + itemName + "'");

		if (json.has("nbt")) {
			try {
				JsonElement element = json.get("nbt");
				NBTTagCompound nbt;
				if (element.isJsonObject())
					nbt = JsonToNBT.getTagFromJson(GSON.toJson(element));
				else
					nbt = JsonToNBT.getTagFromJson(element.getAsString());

				NBTTagCompound tmp = new NBTTagCompound();
				if (nbt.hasKey("ForgeCaps")) {
					tmp.setTag("ForgeCaps", nbt.getTag("ForgeCaps"));
					nbt.removeTag("ForgeCaps");
				}

				tmp.setTag("tag", nbt);
				tmp.setString("id", itemName);
				tmp.setInteger("Count", JsonUtils.getInt(json, "count", 1));
				tmp.setInteger("Damage", JsonUtils.getInt(json, "data", 0));

				return new ItemStack(tmp);
			} catch (NBTException e) {
				throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
			}
		}
		return new ItemStack(item, JsonUtils.getInt(json, "count", 1), JsonUtils.getInt(json, "data", 0));
	}

	public static ItemStack getItemStackBasic(JsonObject json) {
		String itemName = JsonUtils.getString(json, "item");

		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

		if (item == null)
			throw new JsonSyntaxException("Unknown item '" + itemName + "'");

		if (item.getHasSubtypes() && !json.has("data"))
			throw new JsonParseException("Missing data for item '" + itemName + "'");

		return new ItemStack(item, 1, JsonUtils.getInt(json, "data", 0));
	}

	public static Fluid getFluid(JsonElement json) {
		return FluidRegistry.getFluid(json.getAsString());
	}

	public static FluidStack getFluidStack(JsonObject json) {
		int amount = JsonUtils.getInt(json, "amount");
		return new FluidStack(getFluid(json.get("fluid")), amount);
	}

	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

	public static FluidStack getFluidStackNBT(JsonObject json) {
		if (json.has("nbt")) {
			try {
				int amount = JsonUtils.getInt(json, "amount");
				JsonElement element = json.get("nbt");
				NBTTagCompound nbt;
				if (element.isJsonObject())
					nbt = JsonToNBT.getTagFromJson(GSON.toJson(element));
				else
					nbt = JsonToNBT.getTagFromJson(element.getAsString());
				return new FluidStack(getFluid(json.get("fluid")), amount, nbt);
			} catch (NBTException e) {
				throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
			}
		}
		return getFluidStack(json);
	}
}
