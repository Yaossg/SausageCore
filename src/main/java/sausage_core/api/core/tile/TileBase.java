package sausage_core.api.core.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public abstract class TileBase extends TileEntity {
    protected boolean network = false;

    @Override
    public NBTTagCompound getUpdateTag() {
        return network ? writeToNBT(new NBTTagCompound()) : super.getUpdateTag();
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return network ? new SPacketUpdateTileEntity(getPos(), 3, getUpdateTag()) : super.getUpdatePacket();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        if(network) readFromNBT(pkt.getNbtCompound());
    }

    protected void notifyClient() {
        notifyClient(false);
    }

    protected void notifyClient(boolean notifyNeighbors) {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        if(notifyNeighbors)
            world.notifyNeighborsOfStateChange(pos, state.getBlock(), true);
    }

    // following methods are overridden to rename their parameters

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock(); // also keep same as vanilla
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
        return super.hasCapability(capability, side);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        return super.getCapability(capability, side);
    }
}
