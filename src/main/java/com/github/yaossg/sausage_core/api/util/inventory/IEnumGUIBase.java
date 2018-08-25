package com.github.yaossg.sausage_core.api.util.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IEnumGUIBase {
    @Nonnull
    Object getServer(EntityPlayer player, World world, int x, int y, int z);
    @Nonnull
    Object getClient(EntityPlayer player, World world, int x, int y, int z);

    default int ID() {
        return ((Enum<?>) this).ordinal();
    }

    class InnerHandler implements IGuiHandler {
        IEnumGUIBase[] values;
        InnerHandler(IEnumGUIBase[] values) {
            this.values = values;
        }
        @Nullable
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            for (IEnumGUIBase value : values)
                if(ID == value.ID()) return value.getServer(player, world, x, y, z);
            return null;
        }

        @Nullable
        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            for (IEnumGUIBase value : values)
                if(ID == value.ID()) return value.getClient(player, world, x, y, z);
            return null;
        }
    }
    static <E extends Enum<E> & IEnumGUIBase> void register(Object mod, E[] values) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, new InnerHandler(values));
    }
}
