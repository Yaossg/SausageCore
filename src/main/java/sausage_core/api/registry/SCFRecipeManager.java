package sausage_core.api.registry;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sausage_core.api.util.common.SausageUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Sausage Core Family Recipe Manager
 */
public abstract class SCFRecipeManager {
	protected static final Logger logger = LogManager.getLogger("SCFRecipeManager");
	private static SCFRecipeManager INSTANCE;

	public static SCFRecipeManager instance() {
		if(INSTANCE == null) {
			try {
				INSTANCE = (SCFRecipeManager) Class.forName("sausage_core.impl.SCFRecipeManagerImpl").getField("IMPL").get(null);
			} catch(Exception e) {
				logger.fatal("Failed to find a SCFRecipeManager", e);
				throw new RuntimeException(e);
			}
		}
		return INSTANCE;
	}

	protected final Map<ResourceLocation, SCFRecipeType<?>> types = new HashMap<>();

	public static <T> SCFRecipeType<T> newType(ResourceLocation name, Class<T> clazz, Function<JsonObject, T> function, String version, String description) {
		SCFRecipeType<T> type = new SCFRecipeType<>(name, clazz, function, version, description);
		instance().types.put(type.name, type);
		return type;
	}

	public static <T> SCFRecipeType<T> where(ResourceLocation name) {
		if(!instance().types.containsKey(name)) throw new NoSuchElementException(name + " has never registered!");
		return SausageUtils.rawtype(instance().types.get(name));
	}

	public Map<String, Path> getDefaults(ResourceLocation name) {
		return Collections.unmodifiableMap(where(name).defaults);
	}
}
