package sausage_core.api.util.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Map;

import static sausage_core.api.util.common.SausageUtils.nonnull;

@FunctionalInterface
public interface IItemML {
	void loadModel(Item item);

	default Runnable bind(Item item) {
		return () -> loadModel(item);
	}

	static void loadDefaultedModel(Item item) {
		loadVariantModel(item, 0, "inventory");
	}

	static void loadVariantModel(Item item, int metadata, String name) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(nonnull(item.getRegistryName()), name));
	}

	static IItemML mappingBy(Int2ObjectMap<String> map) {
		return item -> {
			for (Int2ObjectMap.Entry<String> entry : map.int2ObjectEntrySet())
				loadVariantModel(item, entry.getIntKey(), entry.getValue());
		};
	}

	static IItemML mappingBy(Map<Integer, String> map) {
		return item -> map.forEach((i, s) -> loadVariantModel(item, i, s));
	}

	static IItemML mappingBy(String... map) {
		return item -> {
			for (int i = 0; i < map.length; ++i) loadVariantModel(item, i, map[i]);
		};
	}
}
