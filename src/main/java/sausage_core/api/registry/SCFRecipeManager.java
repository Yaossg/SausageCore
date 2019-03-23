package sausage_core.api.registry;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sausage_core.api.util.common.JsonRecipeHelper;
import sausage_core.api.util.common.SausageUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Sausage Core Family Recipe Manager
 * */
public class SCFRecipeManager {
    private static Path configPath;
    private static final Logger logger = LogManager.getLogger("SCFRecipeManager");
    private static final Map<ResourceLocation, BiFunction<JsonContext, JsonObject, Object>> types = new HashMap<>();
    private static final Multimap<ResourceLocation, Object> recipes = MultimapBuilder.hashKeys().arrayListValues().build();
    private static final Map<ResourceLocation, Path> defaults = new HashMap<>();

    /**
     * Internal Use Only
     * */
    public static void load() {
        recipes.clear();
        if(configPath == null) init();
        types.keySet().forEach(SCFRecipeManager::load);
        types.forEach(SCFRecipeManager::load);
    }

    private static void load(ResourceLocation name) {
        String modid = name.getResourceDomain();
        Path where = configPath.resolve(modid).resolve(name.getResourcePath());
        if(Files.notExists(where)) {
            try {
                Files.createDirectories(where);
                Path start = defaults.get(name);
                Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        Files.createDirectories(where.resolve(start.relativize(dir).toString()));
                        return super.preVisitDirectory(dir, attrs);
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.copy(file, where.resolve(start.relativize(file).toString()));
                        return super.visitFile(file, attrs);
                    }
                });

            } catch(IOException e) {
                logger.error("Failed to init dictionary for " + modid, e);
            }
        }
    }

    private static void load(ResourceLocation name, BiFunction<JsonContext, JsonObject, Object> function) {
        String modid = name.getResourceDomain();
        Path where = configPath.resolve(modid).resolve(name.getResourcePath());
        JsonRecipeHelper.loadEntries(modid, where, function, o -> recipes.put(name, o));
    }

    private static void init() {
        try {
            init0();
        } catch(IOException e) {
            logger.fatal("Failed to init SCFRecipeManager", e);
            throw new RuntimeException(e);
        }
    }

    private static void init0() throws IOException {
        configPath = Loader.instance().getConfigDir()
                .toPath().resolve("SausageCore-Family-Recipes");
        if(!Files.exists(configPath))
            Files.createDirectories(configPath);
    }

    public static void registerType(ResourceLocation name, BiFunction<JsonContext, JsonObject, Object> function) {
        types.put(name, function);
    }

    public static void registerDefault(ResourceLocation name, Path where) {
        defaults.put(name, where);
    }

    public static <T> Collection<T> recipesOf(ResourceLocation name) {
        return SausageUtils.rawtype(recipes.get(name));
    }
}
