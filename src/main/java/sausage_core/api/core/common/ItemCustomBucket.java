package sausage_core.api.core.common;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

import static net.minecraftforge.fml.common.eventhandler.Event.Result.*;

/**
 * Fork from knightminer.ceramics.items.ItemClayBucket
 * */
public class ItemCustomBucket extends Item {

    public static final String TAG_FLUIDS = "fluids";
    public static final ItemStack MILK_BUCKET = new ItemStack(Items.MILK_BUCKET);

    public ItemCustomBucket() {
        setHasSubtypes(true);
        MinecraftForge.EVENT_BUS.register(this);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
    }

    /* Bucket behavior */

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        // milk we set active and return success, drinking code is done elsewhere
        if(isMilk(stack)) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        // just call the event for all logic
        ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, rayTrace(world, player, !hasFluid(stack)));
        if(ret != null) return ret;

        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onBucketEvent(FillBucketEvent event) {
        // event was already handled
        if(event.getResult() != DEFAULT) return;

        // ensure its our item
        ItemStack stack = event.getEmptyBucket();
        if(stack.getItem() != this) return;

        // validate ray trace
        RayTraceResult target = event.getTarget();
        if(target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) return;

        // make sure we have permission
        World world = event.getWorld();
        BlockPos pos = target.getBlockPos();
        EntityPlayer player = event.getEntityPlayer();
        if(!world.isBlockModifiable(player, pos)) {
            event.setCanceled(true);
            return;
        }

        // if we clicked a cauldron, try that first
        IBlockState state = world.getBlockState(pos);
        ItemStack result = null;
        if(state.getBlock() == Blocks.CAULDRON && !player.isSneaking() && canFit(FluidRegistry.WATER)) {
            result = interactWithCauldron(event, player, world, pos, state, stack);

            // deny means cauldron is not right state to fill
            if(event.getResult() == DENY) return;
        }

        // if the cauldron passed or there was no cauldron, try placing normal fluids
        if (result == null) if(hasFluid(stack)) {
            // check permissions
            if(!player.canPlayerEdit(pos, target.sideHit, stack)) {
                event.setCanceled(true);
                return;
            }

            BlockPos targetPos = pos.offset(target.sideHit);
            if(!isMilk(stack)) result = tryPlaceFluid(stack, player, world, targetPos);
        } else result = tryFillBucket(stack, player, world, pos, target.sideHit);

        if(result != null) {
            event.setResult(ALLOW);
            event.setFilledBucket(result);
        } else event.setResult(DENY);
    }

    private ItemStack tryFillBucket(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        // first, try filling using fluid logic
        ItemStack single = stack.copy();
        single.setCount(1);
        FluidActionResult result = FluidUtil.tryPickUpFluid(single, player, world, pos, side);

        // if it worked, return that
        if(result.isSuccess()) return result.getResult();
        return null;
    }

    private ItemStack tryPlaceFluid(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
        stack = stack.copy();
        FluidStack fluidStack = getFluid(stack);
        FluidActionResult result = FluidUtil.tryPlaceFluid(player, player.getEntityWorld(), pos, stack, fluidStack);
        if(result.isSuccess()) {
            // water and lava place non-flowing for some reason
            if(fluidStack.getFluid() == FluidRegistry.WATER || fluidStack.getFluid() == FluidRegistry.LAVA) {
                IBlockState state = world.getBlockState(pos);
                world.neighborChanged(pos, state.getBlock(), pos);
            }

            return result.getResult();
        }

        return null;
    }

    private ItemStack interactWithCauldron(FillBucketEvent event, EntityPlayer player, World world, BlockPos pos, IBlockState state, ItemStack stack) {
        int level = state.getValue(BlockCauldron.LEVEL);

        // if we have a fluid, try filling
        if (!hasFluid(stack)) {
            // if empty, try emptying
            if(level == 3) {
                // empty cauldron logic
                if(player != null) player.addStat(StatList.CAULDRON_USED);
                if(!world.isRemote) Blocks.CAULDRON.setWaterLevel(world, pos, state, 0);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 1f);

                return withFluid(FluidRegistry.WATER);
            }

            // deny so it stops here
            event.setResult(DENY);
        } else if(getFluid(stack).getFluid() == FluidRegistry.WATER) {
            if(level < 3) {
                // fill cauldron logic
                if(player != null) player.addStat(StatList.CAULDRON_FILLED);
                if(!world.isRemote) Blocks.CAULDRON.setWaterLevel(world, pos, state, 3);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1f, 1f);

                // return empty bucket
                return getEmptyBucket();
            }

            // deny so it stops here
            event.setResult(DENY);
        }

        return null;
    }

    /* Milk bucket logic */

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        // milk has drinking animation
        return isMilk(stack) ? EnumAction.DRINK : EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        // milk requires drinking time
        return isMilk(stack) ? 32 : 0;
    }

    @Override
    @Nullable
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if(!isMilk(stack)) return stack;

        if(entityLiving instanceof EntityPlayer && !((EntityPlayer) entityLiving).isCreative())
            stack = getEmptyBucket();

        if(!worldIn.isRemote) entityLiving.curePotionEffects(MILK_BUCKET);

        if(entityLiving instanceof EntityPlayer)
            ((EntityPlayer) entityLiving).addStat(StatList.getObjectUseStats(this));

        return stack;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        // only work if the bucket is empty and right clicking a cow
        if(!hasFluid(stack) && target instanceof EntityCow && !player.isCreative() && canMilkFit()) {
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1f, 1f);

            // modify items
            // because the action expects mutating the item stack
            if(stack.getCount() == 1) stack.setItemDamage(1);
            else {
                stack.shrink(1);
                ItemHandlerHelper.giveItemToPlayer(player, getMilkBucket());
            }

            return true;
        }
        return false;
    }


    /* Item stack properties */

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return hasFluid(stack) ? 1 : 16;
    }

    @Override
    public int getItemBurnTime(ItemStack stack) {
        FluidStack fluid = getFluid(stack);
        if(fluid != null && fluid.getFluid() == FluidRegistry.LAVA) return 20000;
        return 0;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return getEmptyBucket();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    /**
     * For item <code>"modid:itemid"</code>, the translate key is one of
     * <ul>
     * <li><code>"item.modid.itemid.name"</code> for universal buckets, using <code>"%s"</code> to be the placeholder of fluid's name</li>
     * <li><code>"item.modid.itemid.milk.name"</code> for the milk bucket (if present)</li>
     * <li><code>"item.modid.itemid.empty.name"</code> for the empty bucket</li>
     * </ul>
     * */
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = "item." + getRegistryName().toString().replace(":", ".");
        if(isMilk(stack)) return I18n.format(name + ".milk.name");
        FluidStack fluidStack = getFluid(stack);
        if(fluidStack == null) return I18n.format(name + ".empty.name");

        return I18n.format(name + ".name", fluidStack.getLocalizedName());
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(isInCreativeTab(tab)) {
            // empty
            subItems.add(getEmptyBucket());
            // milk
            if(canMilkFit())
                subItems.add(getMilkBucket());
            // add all fluids that the bucket can be filled with
            // skip milk if registered since we add it manually whether it is a fluid or not
            for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
                if(!fluid.getName().equals("milk") && canFit(fluid)) subItems.add(withFluid(fluid));
        }
    }

    public class FluidCustomBucketWrapper extends FluidBucketWrapper {
        private final ItemCustomBucket outer = ItemCustomBucket.this;
        public FluidCustomBucketWrapper(ItemStack container) {
            super(container);
        }

        @Override
        @Nullable
        public FluidStack getFluid() {
            return outer.getFluid(container);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return canFit(fluid.getFluid());
        }

        @Override
        protected void setFluid(FluidStack stack) {
            container = stack == null ? getEmptyBucket() : outer.withFluid(stack.getFluid());
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME || outer.hasFluid(container) || !canFillFluidType(resource))
                return 0;
            if (doFill) setFluid(resource);
            return Fluid.BUCKET_VOLUME;
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidCustomBucketWrapper(stack);
    }

    /* Utils */

    public FluidStack getFluid(ItemStack container) {
        // milk logic, if milk is registered we use that basically
        if(isMilk(container)) return FluidRegistry.getFluidStack("milk", Fluid.BUCKET_VOLUME);
        NBTTagCompound tags = container.getTagCompound();
        if(tags != null) return FluidStack.loadFluidStackFromNBT(tags.getCompoundTag(TAG_FLUIDS));

        return null;
    }

    /**
     * Returns whether a bucket has fluid. Note the fluid may still be null if
     * true due to milk buckets
     */
    public boolean hasFluid(ItemStack container) {
        if(isMilk(container)) return true;
        return getFluid(container) != null;
    }

    public boolean isMilk(ItemStack stack) {
        return canMilkFit() && stack.getItemDamage() == 1;
    }

    public ItemStack withFluid(Fluid fluid) {
        ItemStack stack = getEmptyBucket();
        if(!canFit(fluid))
            return stack;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag(TAG_FLUIDS, new FluidStack(fluid, Fluid.BUCKET_VOLUME).writeToNBT(new NBTTagCompound()));
        stack.setTagCompound(tag);
        return stack;
    }

    public ItemStack getEmptyBucket() {
        return new ItemStack(this);
    }

    public ItemStack getMilkBucket() {
        return canMilkFit() ? new ItemStack(this, 1, 1) : getEmptyBucket();
    }

    /**
     * Returns whether the bucket can take this fluid
     * For milk, please use {@link #canMilkFit()}
     * */
    public boolean canFit(Fluid fluid) {
        return true;
    }

    /**
     * Returns whether the bucket can take milk
     * */
    public boolean canMilkFit() {
        return true;
    }
}