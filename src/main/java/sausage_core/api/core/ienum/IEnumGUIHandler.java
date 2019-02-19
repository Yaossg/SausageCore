package sausage_core.api.core.ienum;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IEnumGUIHandler {
    @Nonnull
    Object getServer(EntityPlayer player, World world, BlockPos pos);
    @Nonnull
    Object getClient(EntityPlayer player, World world, BlockPos pos);

    default int ID() {
        return ((Enum<?>) this).ordinal();
    }

    class InnerHandler implements IGuiHandler {
        final IEnumGUIHandler[] values;
        InnerHandler(IEnumGUIHandler[] values) {
            this.values = values;
        }
        @Nullable
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            for (IEnumGUIHandler value : values)
                if(ID == value.ID()) return value.getServer(player, world, new BlockPos(x, y, z));
            return null;
        }

        @Nullable
        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            for (IEnumGUIHandler value : values)
                if(ID == value.ID()) return value.getClient(player, world, new BlockPos(x, y, z));
            return null;
        }
    }
    static <E extends Enum<E> & IEnumGUIHandler> void register(Object mod, E[] values) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, new InnerHandler(values));
    }
}
