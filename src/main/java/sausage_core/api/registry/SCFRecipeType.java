package sausage_core.api.registry;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.JsonContext;
import sausage_core.api.util.common.SausageUtils;
import sausage_core.api.util.registry.IModdedRegistry;
import sausage_core.api.util.registry.SimpleRegistry;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SCFRecipeType<T> {

    public final ResourceLocation name;
    public final IModdedRegistry<T> registry;
    public final BiFunction<JsonContext, JsonObject, T> function;
    public final String version;
    public final String description;

    final Map<String, Path> defaults = new HashMap<>();

    SCFRecipeType(ResourceLocation name, Class<T> clazz, BiFunction<JsonContext, JsonObject, T> function, String version, String description) {
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
        return SausageUtils.nonnull(name.getResourceDomain());
    }




}
