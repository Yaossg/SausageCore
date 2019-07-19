package sausage_core.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.MoreFiles;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import sausage_core.api.registry.IRecipeLocatable;
import sausage_core.api.registry.SCFRecipeManager;
import sausage_core.api.registry.SCFRecipeType;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

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
			if (!Files.exists(configRoot)) {
				Files.createDirectories(configRoot);
				create = true;
			}
			Path metadata = configRoot.resolve("_metadata.json");
			if (Files.notExists(metadata)) {
				if (!create) {
					Files.move(configRoot, configRoot.getParent().resolve("1.3-SausageCore-Family-Recipes"));
					Files.createDirectories(configRoot);
					logger.warn("Old version files has been dumped");
				}
				writeMetadata(metadata, "1.0", "Recipes of SausageCore family mods");
			}
		} catch (IOException e) {
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
			if (Files.notExists(where)) {
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
				} catch (IOException e) {}
			}
		});
		try {
			Path metadata = domain.resolve("_metadata.json");
			if (Files.notExists(metadata)) writeMetadata(metadata, type.version, type.description);
			Files.list(domain).forEach(path -> {
				if (Files.isDirectory(path))
					loadEntries(path, type.function, type.registry::register);
			});
		} catch (IOException e) {}
		logger.info("loaded {} recipe(s) of {}", type.registry.size(), type.name);
	}

	public static Stream<Path> walkJson(Path value) {
		try {
			return Files.walk(value)
					.filter(path -> "json".equals(MoreFiles.getFileExtension(path)))
					.filter(path -> !path.getFileName().toString().startsWith("_"));
		} catch (IOException e) {
			return Stream.empty();
		}
	}

	public static <T> void loadEntries(Path where, Function<JsonObject, T> parser, Consumer<T> consumer) {
		walkJson(where).forEach(path -> {
			String file = where.relativize(path).toString();
			file = file.substring(0, file.lastIndexOf(".json"));
			ResourceLocation location = new ResourceLocation(where.getFileName().toString(), file);
			try (BufferedReader reader = Files.newBufferedReader(path)) {
				JsonElement json = JsonUtils.fromJson(GSON, reader, JsonElement.class);
				load(location, json, parser, consumer);
			} catch (Exception e) {
				logger.error("Failed to load the recipe: " + location, e);
			}
		});
	}

	private static <T> void load(ResourceLocation location, @Nullable JsonElement json, Function<JsonObject, T> parser, Consumer<T> consumer) {
		if (json == null || json.isJsonNull()) throw new NullPointerException();
		if (json.isJsonObject()) {
			T apply = parser.apply((JsonObject) json);
			if (apply instanceof IRecipeLocatable)
				((IRecipeLocatable) apply).setLocation(location);
			consumer.accept(apply);
		}
		if (json.isJsonArray())
			for (JsonElement element : ((JsonArray) json))
				load(location, element, parser, consumer);
		if (json.isJsonPrimitive()) {
			JsonObject object = new JsonObject();
			object.add("value", json);
			load(location, object, parser, consumer);
		}
	}
}
