package sausage_core.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import sausage_core.SausageCore;
import sausage_core.api.event.InfoCardEvent;
import sausage_core.config.SausageCoreConfig;

public class ItemInfoCard extends Item {
	public ItemInfoCard() {
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
		InfoCardEvent.INFO_CARD_BUS.register(this);
	}

	public static final String HAS = SausageCore.MODID + ".has_info_card";

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if(!SausageCoreConfig.spawnInfoCard) return;
		NBTTagCompound entityData = event.player.getEntityData();
		NBTTagCompound data = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		if(!data.getBoolean(HAS)) {
			ItemHandlerHelper.giveItemToPlayer(event.player, new ItemStack(this));
			data.setBoolean(HAS, true);
			entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote)
			InfoCardEvent.fire(playerIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onInfoCard(InfoCardEvent event) {
		event.modInfo
				.addModTitle(SausageCore.MODID, SausageCore.NAME, SausageCore.VERSION, "Yaossg")
				.addI18nText("Please visit")
				.addText(" ")
				.withStyle(style -> style.setColor(TextFormatting.BLUE))
				.addURL("HERE", "https://github.com/Yaossg/SausageCore")
				.withStyle()
				.addText(" ")
				.addI18nText("for more information")
				.newline()
				.withStyle(style -> style.setColor(TextFormatting.GOLD))
				.addText("Why not try my sister mods ")
				.addURL("ManaCraft", "https://github.com/Yaossg/ManaCraft")
				.addText(" and ")
				.addText(style -> style.setItalic(true).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								greenish("Working in progress and coming soon (or later?)"))),
						"Sausage's Factory")
				.addText("?");
		if(!Loader.isModLoaded("mana_craft")) {
			event.modInfo
					.newline()
					.withStyle(style -> style.setColor(TextFormatting.RED))
					.addI18nText("No %s is detected", "ManaCraft")
					.addText(" :(");
		}
		if(!Loader.isModLoaded("sausages_factory")) {
			event.modInfo
					.newline()
					.withStyle(style -> style.setColor(TextFormatting.RED))
					.addI18nText("No %s is detected", "Sausage's Factory")
					.addText(" :(")
					.withStyle()
					.addText(" but it hasn't released, has it? :)");
		}
	}

	static ITextComponent greenish(String s) {
		return new TextComponentString(s).setStyle(new Style().setColor(TextFormatting.GREEN));
	}
}
