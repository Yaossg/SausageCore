package sausage_core.api.registry;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import sausage_core.api.core.registry.IModdedRegistry;
import sausage_core.api.core.registry.SimpleRegistry;
import sausage_core.api.util.common.SausageUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SCFRecipeType<T> {
	public final ResourceLocation name;
	public final IModdedRegistry<T> registry;
	public final Function<JsonObject, T> function;
	public final String version;
	public final String description;
	final Map<String, Path> defaults = new HashMap<>();

	SCFRecipeType(ResourceLocation name, Class<T> clazz, Function<JsonObject, T> function, String version, String description) {
		this.name = name;
		this.function = function;
		registry = new SimpleRegistry<>(clazz);
		this.version = version;
		this.description = description;
	}

	public void addDefaults(String modid, Path path) {
		defaults.put(modid, path);
	}

	public String owner() {
		return SausageUtils.nonnull(name.getNamespace());
	}
}
