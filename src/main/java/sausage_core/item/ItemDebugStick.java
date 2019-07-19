package sausage_core.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.SausageCore;
import sausage_core.api.core.ienum.IEnumLocalizer;
import sausage_core.api.util.nbt.NBTs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDebugStick extends Item {
	public enum Mode implements IEnumLocalizer {
		SWITCH {
			@Override
			EnumActionResult onClickBlock(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, EnumHand hand) {
				IBlockState state = worldIn.getBlockState(pos);
				ImmutableList<IBlockState> states = state.getBlock().getBlockState().getValidStates();
				int i = states.size() + states.indexOf(state) + (player.isSneaking() ? -1 : 1);
				IBlockState newState = states.get(i % states.size());
				worldIn.setBlockState(pos, newState);
				return state != newState ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
			}
		},
		BLOCK {
			@Override
			EnumActionResult onClickBlock(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, EnumHand hand) {
				if (!worldIn.isRemote) {
					player.sendMessage(new TextComponentString("BlockState: " + worldIn.getBlockState(pos)));
					TileEntity tileEntity = worldIn.getTileEntity(pos);
					if (tileEntity != null) {
						NBTTagCompound nbt = tileEntity.writeToNBT(new NBTTagCompound());
						player.sendMessage(new TextComponentString("TileEntity: ").appendSibling(NBTs.highlight(nbt)));
					}
				}
				return EnumActionResult.SUCCESS;
			}
		},
		ENTITY {
			@Override
			boolean onClickEntity(ItemStack stack, EntityPlayer playerIn, Entity target, EnumHand hand) {
				if (!playerIn.world.isRemote) {
					NBTTagCompound nbt = target.serializeNBT();
					playerIn.sendMessage(new TextComponentString("Entity: " + EntityList.getKey(target)));
					playerIn.sendMessage(new TextComponentString("NBT: ").appendSibling(NBTs.highlight(nbt)));
				}
				return true;
			}
		};

		EnumActionResult onClickBlock(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, EnumHand hand) {
			return EnumActionResult.PASS;
		}

		boolean onClickEntity(ItemStack stack, EntityPlayer playerIn, Entity target, EnumHand hand) {
			return false;
		}

		public String localize() {
			return localize(SausageCore.MODID, "debug_stick");
		}
	}

	public Mode getMode(ItemStack stack) {
		return Mode.values()[NBTs.getOrCreateSubTag(stack, "mode", NBTs.of(0)).getInt()];
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(getMode(stack).localize());
	}

	public ItemDebugStick() {
		setMaxStackSize(1);
		setMaxDamage(Mode.values().length);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack heldItem = playerIn.getHeldItem(handIn);
		boolean sneaking = playerIn.isSneaking();
		if (sneaking) {
			NBTTagInt mode = NBTs.getOrCreateSubTag(heldItem, "mode", NBTs.of(0));
			int i = (mode.getInt() + 1) % Mode.values().length;
			NBTs.setOrCreateSubTag(heldItem, "mode", NBTs.of(i));
			playerIn.sendStatusMessage(new TextComponentString(Mode.values()[i].localize()), true);
		}
		return new ActionResult<>(sneaking ? EnumActionResult.SUCCESS : EnumActionResult.PASS, heldItem);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
		return getMode(player.getHeldItem(hand)).onClickBlock(player, worldIn, pos, facing, hand);
	}

	@SubscribeEvent
	public void onClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
		Entity target = event.getTarget();
		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack stack = event.getItemStack();
		event.setCancellationResult(EnumActionResult.SUCCESS);
		event.setCanceled(getMode(player.getHeldItem(hand)).onClickEntity(stack, player, target, hand));
	}
}
