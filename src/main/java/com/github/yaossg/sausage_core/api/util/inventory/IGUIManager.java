package com.github.yaossg.sausage_core.api.util.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * default implementation:
 *  <code>
 *     BiFunction<EntityPlayer, TileEntity, Container> common;
 *     Function<Container, GuiContainer> client;
 *
 *     ManaCraftGUIs(BiFunction<EntityPlayer, TileEntity, Container> common, Function<Container, GuiContainer> client) {
 *         this.common = common;
 *         this.client = client;
 *     }
 *
 *     @Override
 *     public BiFunction<EntityPlayer, TileEntity, Container> getCommonBuilder() {
 *         return common;
 *     }
 *
 *     @Override
 *     public Function<Container, GuiContainer> getClientBuilder() {
 *         return client;
 *     }
 *  </code>
 * copy this to your Manager
 * then register with <code> IGUIManager.register(ManaCraft.instance, ManaCraftGUIs.values()); </code>
 *
 * */
public interface IGUIManager {
    BiFunction<EntityPlayer, TileEntity, Container> getCommonBuilder();

    Function<Container, GuiContainer> getClientBuilder();

    default Container getCommon(EntityPlayer player, TileEntity tileEntity) {
        return getCommonBuilder().apply(player, tileEntity);
    }

    default GuiContainer getClient(EntityPlayer player, TileEntity tileEntity) {
        return getClient(getCommon(player, tileEntity));
    }

    default GuiContainer getClient(Container container) {
        return getClientBuilder().apply(container);
    }

    @FunctionalInterface
    interface Matcher<R> {
        @Nullable
        R match(int id, EntityPlayer player, TileEntity tileEntity);
    }

    @FunctionalInterface
    interface Builder<R> {
        @Nonnull
        R build(IGUIManager self, EntityPlayer player, TileEntity tileEntity);
    }

    @Nullable
    static <E extends Enum<E> & IGUIManager, R>
    R get(E[] values, int id, Builder<R> builder, EntityPlayer player, TileEntity tileEntity) {
        return Arrays.stream(values)
                .filter(gui -> gui.ordinal() == id)
                .map(gui -> builder.build(gui, player, tileEntity))
                .findAny().orElse(null);
    }

    static IGuiHandler matching(Matcher<Container> common, Matcher<GuiContainer> client) {
        return new IGuiHandler() {
            @Nullable
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return common.match(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
            }

            @Nullable
            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return client.match(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
            }
        };
    }

    static <E extends Enum<E> & IGUIManager> IGuiHandler handler(E[] values) {
        return matching((id, player, tileEntity) -> get(values, id, IGUIManager::getCommon, player, tileEntity),
                (id, player, tileEntity) -> get(values, id, IGUIManager::getClient, player, tileEntity));
    }

    static <E extends Enum<E> & IGUIManager> void register(Object mod, E[] values) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, handler(values));
    }
}
