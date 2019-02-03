package sausage_core.api.util.explosion;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import sausage_core.api.util.math.BufferedRandom;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public class ExExplosion extends Explosion {
    boolean hurtEntity, spawnParticles;
    IBlockState fire, fill;
    Predicate<IBlockState> air;
    Random random;
    public ExExplosion(World worldIn, Entity entityIn,
                       double x, double y, double z, float size,
                       boolean causesFire, boolean damagesTerrain, boolean hurtEntity, boolean spawnParticles,
                       IBlockState fire, IBlockState fill, Predicate<IBlockState> air) {
        super(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain);
        this.hurtEntity = hurtEntity;
        this.spawnParticles = spawnParticles;
        this.fire = fire;
        this.fill = fill;
        this.air = air;
        random = BufferedRandom.boxed(new Random(seed()));
    }
    
    private long seed() {
        return (Arrays.hashCode(new double[] {x, y, z, size}) 
                | (long) Arrays.hashCode(new boolean[] {causesFire, damagesTerrain, hurtEntity, spawnParticles}) << 32) 
                ^ fire.hashCode() ^ fill.hashCode()
                ^ (long) air.hashCode() << 32
                ^ world.getWorldTime();
    }

    public static Builder builder(World world) {
        return new Builder(world);
    }

    public void doExplosionA() {
        Set<BlockPos> set = Sets.newHashSet();
        for(int i = 0; i < 16; ++i)
            for(int j = 0; j < 16; ++j)
                for(int k = 0; k < 16; ++k)
                    if(i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {
                        double dx = i / 7.5 - 1;
                        double dy = j / 7.5 - 1;
                        double dz = k / 7.5 - 1;
                        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        dx = dx / length;
                        dy = dy / length;
                        dz = dz / length;
                        double x_affected = x;
                        double y_affected = y;
                        double z_affected = z;

                        for(float power = size * (0.7F + random.nextFloat() * 0.6F);
                            power > 0; power -= 0.225F) {
                            BlockPos pos = new BlockPos(x_affected, y_affected, z_affected);
                            IBlockState state = world.getBlockState(pos);

                            if(!air.test(state)) {
                                float resistance = exploder != null ? exploder.getExplosionResistance(this, world, pos, state) : state.getBlock().getExplosionResistance(world, pos, null, this);
                                power -= (resistance + 0.3F) * 0.3F;
                            }

                            if(power > 0 && (exploder == null || exploder.canExplosionDestroyBlock(this, world, pos, state, power)))
                                set.add(pos);

                            x_affected += dx * 0.3;
                            y_affected += dy * 0.3;
                            z_affected += dz * 0.3;
                        }
                    }

        affectedBlockPositions.addAll(set);
        if(!hurtEntity) return;
        float power = size * 2;
        int left = MathHelper.floor(x - power - 1);
        int right = MathHelper.floor(x + power + 1);
        int down = MathHelper.floor(y - power - 1);
        int up = MathHelper.floor(y + power + 1);
        int front = MathHelper.floor(z - power - 1);
        int back = MathHelper.floor(z + power + 1);
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(exploder, new AxisAlignedBB(left, down, front, right, up, back));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(world, this, list, power);
        Vec3d vec3d = new Vec3d(x, y, z);
        for(Entity entity : list)
            if(!entity.isImmuneToExplosions()) {
                double effect = entity.getDistance(x, y, z) / power;

                if(effect <= 1) {
                    double dx = entity.posX - x;
                    double dy = entity.posY + entity.getEyeHeight() - y;
                    double dz = entity.posZ - z;
                    double length = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);

                    if(length != 0) {
                        dx = dx / length;
                        dy = dy / length;
                        dz = dz / length;
                        double density = world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        density *= (1 - effect);
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float) ((density * density + density) / 2 * 7 * power + 1));
                        double motion = density;

                        if(entity instanceof EntityLivingBase)
                            motion = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, density);

                        entity.motionX += dx * motion;
                        entity.motionY += dy * motion;
                        entity.motionZ += dz * motion;

                        if(entity instanceof EntityPlayer) {
                            EntityPlayer player = (EntityPlayer) entity;

                            if(!player.isSpectator() && (!player.isCreative() || !player.capabilities.isFlying))
                                playerKnockbackMap.put(player, new Vec3d(dx * density, dy * density, dz * density));
                        }
                    }
                }
            }
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        spawnParticles = spawnParticles && this.spawnParticles;
        world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1 + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F);
        if(spawnParticles)
        if(size >= 2 && damagesTerrain)
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x, y, z, 1, 0, 0);
        else world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 1, 0, 0);

        if(damagesTerrain) for(BlockPos pos : affectedBlockPositions) {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if(spawnParticles) {
                double rx = ((float) pos.getX() + random.nextFloat());
                double ry = ((float) pos.getY() + random.nextFloat());
                double rz = ((float) pos.getZ() + random.nextFloat());
                double dx = rx - x;
                double dy = ry - y;
                double dz = rz - z;
                double length = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                dx = dx / length;
                dy = dy / length;
                dz = dz / length;
                double power = 0.5D / (length / size + 0.1D);
                power = power * (random.nextFloat() * random.nextFloat() + 0.3F);
                dx = dx * power;
                dy = dy * power;
                dz = dz * power;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (rx + x) / 2, (ry + y) / 2, (rz + z) / 2, dx, dy, dz);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, rx, ry, rz, dx, dy, dz);
            }

            if(!air.test(state)) {
                if(block.canDropFromExplosion(this))
                    block.dropBlockAsItemWithChance(world, pos, world.getBlockState(pos), 1 / size, 0);

                block.onBlockExploded(world, pos, this);
            }

            if(!air.test(state)) {
                world.setBlockState(pos, fill);
            }
        }

        if(causesFire && !world.isRemote) for(BlockPos pos : affectedBlockPositions)
            if(air.test(world.getBlockState(pos)) && !air.test(world.getBlockState(pos.down())) && random.nextInt(3) == 0)
                world.setBlockState(pos, fire);
    }

    public ExExplosion apply() {
        if(!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, this)) {
            doExplosionA();
            doExplosionB(FMLCommonHandler.instance().getSide() == Side.CLIENT);
        }
        return this;
    }

    public static class Builder {
        private final World world;
        private Entity entity = null;
        private boolean initPos = false;
        private double x = 0;
        private double y = 0;
        private double z = 0;
        private boolean initSize = false;
        private float size = 0;
        private boolean causesFire = false;
        private boolean damagesTerrain = false;
        private boolean hurtEntity = false;
        private boolean spawnParticles = false;
        private IBlockState fill = Blocks.AIR.getDefaultState();
        private IBlockState fire = Blocks.FIRE.getDefaultState();
        private Predicate<IBlockState> air = state -> state.getMaterial() == Material.AIR;

        private Builder(World world) {
            this.world = world;
        }

        public Builder at(BlockPos pos) {
            x = pos.getX() + 0.5;
            y = pos.getY() + 0.5;
            z = pos.getZ() + 0.5;
            initPos = true;
            return this;
        }

        public Builder at(Vec3d vec3d) {
            x = vec3d.x;
            y = vec3d.y;
            z = vec3d.z;
            initPos = true;
            return this;
        }

        public Builder at(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            initPos = true;
            return this;
        }

        public Builder by(Entity entity) {
            this.entity = entity;
            return this;
        }

        public Builder sizeOf(float size) {
            this.size = size;
            initSize = true;
            return this;
        }

        public Builder causesFire() {
            causesFire = true;
            return this;
        }

        public Builder damagesTerrain() {
            damagesTerrain = true;
            return this;
        }

        public Builder hurtEntity() {
            hurtEntity = true;
            return this;
        }

        public Builder spawnParticles() {
            spawnParticles = true;
            return this;
        }

        public Builder fire(IBlockState fire) {
            this.fire = fire;
            return this;
        }

        public Builder fill(IBlockState fill) {
            this.fill = fill;
            return this;
        }

        public Builder air(Predicate<IBlockState> air) {
            this.air = air;
            return this;
        }

        public ExExplosion build() {
            if(!(initPos && initSize))
                throw new IllegalStateException("ExExplosion.Builder: pos and size are required");
            return new ExExplosion(world, entity, x, y, z, size, causesFire, damagesTerrain, hurtEntity, spawnParticles, fire, fill, air);
        }

    }
}
