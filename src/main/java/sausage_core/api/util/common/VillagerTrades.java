package sausage_core.api.util.common;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

import static sausage_core.api.util.common.Conversions.To.stack;

public final class VillagerTrades {
	private VillagerTrades() {}
	public static EntityVillager.ITradeList item2gem(ItemStack item, int min, int max) {
		return item2gem(item, new EntityVillager.PriceInfo(min, max));
	}

	public static EntityVillager.ITradeList item2gem(ItemStack item, EntityVillager.PriceInfo buy) {
		return (merchant, recipeList, random) -> {
			int i = buy.getPrice(random);
			recipeList.add(new MerchantRecipe(stack(item, i < 0 ? 1 : i), stack(Items.EMERALD, i < 0 ? -i : 1)));
		};
	}

	public static EntityVillager.ITradeList gem2item(ItemStack item, int min, int max) {
		return gem2item(item, new EntityVillager.PriceInfo(min, max));
	}

	public static EntityVillager.ITradeList gem2item(ItemStack item, EntityVillager.PriceInfo sell) {
		return (merchant, recipeList, random) -> {
			int i = sell.getPrice(random);
			recipeList.add(new MerchantRecipe(stack(Items.EMERALD, i < 0 ? 1 : i), stack(item, i < 0 ? -i : 1)));
		};
	}

	public static EntityVillager.ITradeList enchanted(Item item, int min, int max) {
		return enchanted(item, new EntityVillager.PriceInfo(min, max));
	}

	public static EntityVillager.ITradeList enchanted(Item item, EntityVillager.PriceInfo buy) {
		return new EntityVillager.ListEnchantedItemForEmeralds(item, buy);
	}
}
