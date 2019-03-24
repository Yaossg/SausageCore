package sausage_core.api.util.common;

import com.google.gson.*;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class JsonRecipeHelper {

    public static Fluid getFluid(JsonElement json) {
        return FluidRegistry.getFluid(json.getAsString());
    }

    public static FluidStack getFluidStack(JsonObject json) {
        int amount = JsonUtils.getInt(json, "amount");
        return new FluidStack(getFluid(json.get("fluid")), amount);
    }

    public static FluidStack getFluidStackNBT(JsonObject json) {
        if(json.has("nbt")) {
            try {
                int amount = JsonUtils.getInt(json, "amount");
                JsonElement element = json.get("nbt");
                NBTTagCompound nbt;
                if(element.isJsonObject())
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

    public static List<Path> walkJson(Path value) {
        try {
            return Files.walk(value)
                    .filter(path -> "json".equals(
                            FilenameUtils.getExtension(path.getFileName().toString())))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    public static final Method loadConstants = ReflectionHelper.findMethod(JsonContext.class, "loadConstants", "loadConstants", JsonObject[].class);
    public static <T> void loadEntries(String modid, Path where, BiFunction<JsonContext, JsonObject, T> parser, Consumer<T> consumer) {
        List<Path> paths = walkJson(where);
        if(paths.isEmpty()) return;
        JsonContext context = new JsonContext(modid);
        for (Path path : paths) {
            if("_constants.json".equals(path.getFileName().toString())) {
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    JsonObject[] json = JsonUtils.fromJson(GSON, reader, JsonObject[].class);
                    loadConstants.invoke(context, (Object) json);
                } catch (Exception e) {
                    LogManager.getLogger(modid).error("Unexpected Exception: ", e);
                }
            }
        }
        paths.removeIf(path -> path.getFileName().toString().startsWith("_"));
        paths.forEach(path -> {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                JsonElement json = JsonUtils.fromJson(GSON, reader, JsonElement.class);
                if(json instanceof JsonObject)
                    consumer.accept(parser.apply(context, (JsonObject)json));
                else if(json instanceof JsonArray)
                    for(JsonElement element : ((JsonArray) json))
                        consumer.accept(parser.apply(context, (JsonObject) element));
                else throw new IOException();
            } catch (Exception e) {
                LogManager.getLogger(modid).error("Unexpected Exception: ", e);
            }
        });
    }

}
