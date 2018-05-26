package yaossg.mod.sausage_core.api.util;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IGUIManager {
    BiFunction<EntityPlayer, TileEntity, Container> getServerBuilder();
    Function<Container, GuiContainer> getClientBuilder();
    default Container getServer(EntityPlayer player, TileEntity tileEntity) {
        return getServerBuilder().apply(player, tileEntity);
    }
    default GuiContainer getClient(EntityPlayer player, TileEntity tileEntity) {
        return getClient(getServer(player, tileEntity));
    }
    default GuiContainer getClient(Container container) {
        return getClientBuilder().apply(container);
    }
    @FunctionalInterface
    interface Matcher<T> {
        T match(int id, EntityPlayer player, TileEntity tileEntity);
    }
    @FunctionalInterface
    interface Builder<R> {
        R build(IGUIManager self, EntityPlayer player, TileEntity tileEntity);
    }
    @Nullable
    static <T extends Enum<T> & IGUIManager, R>
    R get(T[] values, int id, Builder<R> builder, EntityPlayer player, TileEntity tileEntity) {
        return Arrays.stream(values)
                .filter(gui -> gui.ordinal() == id)
                .map(gui -> builder.build(gui, player, tileEntity))
                .findAny().orElse(null);
    }
    static IGuiHandler handler(Matcher<Container> server, Matcher<GuiContainer> client) {
        return new IGuiHandler() {
            @Nullable
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return server.match(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
            }
            @Nullable
            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return client.match(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
            }
        };
    }
    static <T extends Enum<T> & IGUIManager> IGuiHandler buildHandler(T[] values) {
        return handler((id, player, tileEntity) -> get(values, id, IGUIManager::getServer, player, tileEntity),
                (id, player, tileEntity) -> get(values, id, IGUIManager::getClient, player, tileEntity));
    }
    static <T extends Enum<T> & IGUIManager> void register(Object mod, T[] values) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, buildHandler(values));
    }
    static ResourceLocation getTexture(String modid, String texture) {
        return new ResourceLocation(modid , "textures/gui/container/" + texture + ".png");
    }
}
