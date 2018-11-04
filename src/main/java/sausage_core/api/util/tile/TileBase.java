package sausage_core.api.util.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
