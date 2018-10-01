package sausage_core.api.util.common;

import com.google.common.base.Strings;
import com.google.common.collect.Streams;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import sausage_core.SausageCore;

import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class SausageUtils {
    /**
     * used for {@link net.minecraft.block.Block#setLightLevel(float)}
     * convert integer light level to float one.
     * */
    public static float lightLevelOf(int level) {
        return level / 16f;
    }

    /**
     * give an advancement to player
     * all of pre-advancements will be given at the same time
     * @param player who will be given.
     */
    public static void giveAdvancement(Entity player, String modid, String... path) {
        if(player.getServer() != null && player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            String joined = String.join("/", path);
            Advancement advancement = player.getServer().getAdvancementManager()
                    .getAdvancement(new ResourceLocation(modid, joined));
            checkNotNull(advancement, "unable to find such an advancement: %s:%s", modid, joined);
            AdvancementProgress progress = playerMP.getAdvancements().getProgress(advancement);
            if(!progress.isDone())
                for (String s : progress.getRemaningCriteria())
                    playerMP.getAdvancements().grantCriterion(advancement, s);
        }
    }

    /**
     * @deprecated narrow usage
     * @param tileClass is a class whose name starts with "Tile"
     */
    @Deprecated
    public static void registerTile(Class<? extends TileEntity> tileClass, String modid) {
        GameRegistry.registerTileEntity(tileClass, modid + ":" + tileClass.getSimpleName().replaceFirst("Tile", ""));
    }

    public static void registerTileEntity( String modid, Class<? extends TileEntity> tileEntityClass) {
        String name = tileEntityClass.getSimpleName();
        int index = name.indexOf("TileEntity"), sub = "TileEntity".length();
        if(index < 0) {
            index = name.indexOf("Tile");
            sub = "Tile".length();
        }
        checkArgument(index >= 0, "Unable to get TileEntity's name from \"%s\"", name);
        name = name.substring(index + sub);
        checkArgument(!name.isEmpty(), "Unable to get TileEntity's name from \"%s\"", name);
        name = camelToUnderlined(name);
        GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(modid, name));
        SausageCore.logger.info("{} register a tileEntity named {} // class: {}", modid, name, tileEntityClass.getName());
    }

    /**
     * @return all items in the game, including all subtypes
     * */
    public static Stream<ItemStack> getAllItems() {
        return Streams.stream(ForgeRegistries.ITEMS)
                .flatMap(item -> {
                    if(item.getHasSubtypes()) {
                        NonNullList<ItemStack> subtypes = NonNullList.create();
                        item.getSubItems(CreativeTabs.SEARCH, subtypes);
                        return subtypes.stream();
                    } else {
                        return Stream.of(new ItemStack(item));
                    }
                })
                .filter(stack -> !stack.isEmpty());
    }

    /**sausage-private*/ public static void unstableWarning(Logger logger, String name, String version, String modid) {
        logger.info("{} (modid:{}) v{} is loading now", name, modid, version);
        logger.warn("The mod is still unstable and in early development and full of bugs");
        logger.warn("If you find any bug, please create a new issue on github.com/Yaossg/{} ", name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T rawtype(Object o) {
        return (T) o;
    }

    public static Optional<Path> getPath(Class<?> clazz, String meta) {
        URI uri;
        try {
            uri = clazz.getResource(meta).toURI();
            switch (uri.getScheme()) {
                case "file":
                    return Optional.of(Paths.get(uri));
                case "jar":
                    return Optional.of(FileSystems.newFileSystem(uri, Collections.emptyMap()).getPath(meta));
            }
        } catch (Exception e) {
            // NO-OP
        }
        return Optional.empty();
    }

    private static final Pattern UPPERS = Pattern.compile("[A-Z]+");
    private static final Pattern IDENTIFIER = Pattern.compile("\\p{javaJavaIdentifierStart}+\\p{javaJavaIdentifierPart}*");
    private static final Pattern UPPER_TAIL = Pattern.compile(".+[A-Z]{2,}");
    /**
     * convert a camel name to a underlined lower case name
     * @apiNote  here are some examples
     * null -> {@link NullPointerException}
     * "" -> ""
     * "GreatChanges" -> "great_changes"
     * "tinyChanges" -> "tiny_changes"
     * "SUPER" -> "super"
     * "SFItems" -> "sf_items"
     * "ILoveMc" -> "i_love_mc"
     * "ILoveMC" -> "i_love_mc"
     * "__Me__" -> "__me__"
     * "__me__" -> "__me__"
     * "happy!" -> {@link IllegalArgumentException}
     * "SF's chair" -> {@link IllegalArgumentException}.
     *
     * @param camel A camel name
     * @return A underlined lower case name
     * @throws NullPointerException if camel is null
     * @throws IllegalArgumentException if camel is not a valid name
     * */
    public static String camelToUnderlined(String camel) {
        if(camel.isEmpty()) return "";
        checkArgument(IDENTIFIER.matcher(camel).matches(), "Invalid Name: %s", camel);
        int underlines = 0;
        while(camel.charAt(underlines) == '_') ++underlines;
        String _s = Strings.repeat("_", underlines);
        camel = camel.substring(underlines);
        if(camel.matches(UPPERS.pattern()))
            return _s + camel.toLowerCase();
        StringBuilder builder = new StringBuilder(camel);
        Matcher matcher = UPPERS.matcher(camel);
        for(int offset = 0; matcher.find(); ) {
            String group = matcher.group();
            String replacement = group.length() > 1
                    ? "_" + group.substring(0, group.length() - 1).toLowerCase()
                    + "_" + group.substring(group.length() - 1).toLowerCase()
                    : "_" + group.toLowerCase();
            builder.replace(matcher.start() + offset, matcher.end() + offset, replacement);
            offset += group.length() > 1 ? 2 : 1;
        }
        if(camel.matches(UPPER_TAIL.pattern())) builder.deleteCharAt(builder.lastIndexOf("_"));
        if(builder.charAt(0) == '_') builder.deleteCharAt(0);
        return _s + builder.toString();
    }

}