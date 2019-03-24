package sausage_core.impl;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import sausage_core.api.registry.SCFRecipeManager;
import sausage_core.api.registry.SCFRecipeType;
import sausage_core.api.util.common.JsonRecipeHelper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class SCFRecipeManagerImpl extends SCFRecipeManager {
    public static final SCFRecipeManagerImpl IMPL = new SCFRecipeManagerImpl();
    private Path configRoot;

    private static void writeMetadata(Path metadata, String version, String description) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(metadata, StandardOpenOption.CREATE)) {
            GSON.toJson(ImmutableMap.of("version", version, "description", description), writer);
        }
    }

    public void load() {
        try {
            configRoot = Loader.instance().getConfigDir()
                    .toPath().resolve("SausageCore-Family-Recipes");
            boolean create = false; // 1.3 Compatible
            if(!Files.exists(configRoot)) {
                Files.createDirectories(configRoot);
                create = true;
            }
            Path metadata = configRoot.resolve("_metadata.json");
            if(Files.notExists(metadata)) {
                if(!create) {
                    Files.move(configRoot, configRoot.getParent().resolve("1.3-SausageCore-Family-Recipes"));
                    Files.createDirectories(configRoot);
                    logger.warn("Old version files has been dumped");
                }
                writeMetadata(metadata, "1.0", "Recipes of SausageCore family mods");
            }
        } catch(IOException e) {
            logger.fatal("Failed to init SCFRecipeManager", e);
            throw new RuntimeException(e);
        }
        types.forEach(this::load);
    }

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    @SuppressWarnings("unchecked")
    private void load(ResourceLocation name, SCFRecipeType type) {
        String modid = type.owner();
        Path domain = configRoot.resolve(modid).resolve(name.getResourcePath());
        getDefaults(name).forEach((by, start) -> {
            Path where = domain.resolve(by);
            if(Files.notExists(where)) {
                try {
                    Files.createDirectories(where);
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
                    logger.error("Unexpected Exception: ", e);
                }
            }

        });
        try {
            Path metadata = domain.resolve("_metadata.json");
            if(Files.notExists(metadata)) writeMetadata(metadata, type.version, type.description);
            Files.list(domain).forEach(path -> {
                if(Files.isDirectory(path))
                    JsonRecipeHelper.loadEntries(path.getFileName().toString(),
                            path, type.function, type.registry::register);
            });
        } catch(IOException e) {
            logger.error("Unexpected Exception: ", e);
        }
        logger.info("loaded {} recipe(s) of {}", type.registry.size(), type.name);

    }
}
