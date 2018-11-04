package sausage_core.api.util.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sausage_core.api.util.nbt.NBTs;

import javax.annotation.concurrent.Immutable;

@Immutable
public class DimensionalBlockPos {
    private final int dim;
    private final BlockPos pos;

    public DimensionalBlockPos(World world, BlockPos pos) {
        this(world.provider.getDimension(), pos);
    }

    public DimensionalBlockPos(int dim, BlockPos pos) {
        this.dim = dim;
        this.pos = pos.toImmutable();
    }

    public int getDim() {
        return dim;
    }

    public BlockPos getPos() {
        return pos;
    }

    public static DimensionalBlockPos fromNBT(NBTTagCompound nbt) {
        return new DimensionalBlockPos(nbt.getInteger("dim"), NBTUtil.getPosFromTag(nbt.getCompoundTag("pos")));
    }

    public NBTTagCompound toNBT() {
        return NBTs.of("dim", NBTs.of(dim), "pos", NBTUtil.createPosTag(pos));
    }

    public static DimensionalBlockPos fromBytes(ByteBuf buf) {
        return new DimensionalBlockPos(buf.readInt(),
                new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        DimensionalBlockPos other = (DimensionalBlockPos) obj;
        return dim == other.dim && pos.equals(other.pos);
    }

    @Override
    public int hashCode() {
        return dim ^ pos.hashCode();
    }
}
