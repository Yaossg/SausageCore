package sausage_core.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import sausage_core.SausageCore;
import sausage_core.config.SausageCoreConfig;

import java.util.function.UnaryOperator;

public class ItemInfoCard extends Item {
    public ItemInfoCard() {
        setMaxStackSize(1);
        if(SausageCoreConfig.spawnInfoCard)
            MinecraftForge.EVENT_BUS.register(this);
    }
    public static final String HAS = SausageCore.MODID + ".has_info_card";
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
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
        if(!worldIn.isRemote) {
            TextComponentString info = new TextComponentString("");
            info.appendSibling(title(String.format("%s %s - By Yaossg\n", SausageCore.NAME, SausageCore.VERSION)));
            info.appendSibling(translate("Please visit"));
            info.appendText(" ");
            info.appendSibling(withStyle(translate("HERE"), style -> atURL(style, "https://github.com/Yaossg/SausageCore").setColor(TextFormatting.BLUE)));
            info.appendText(" ");
            info.appendSibling(translate("for more information"));
            info.appendText("\n");
            info.appendSibling(gold("Why not try my sister mods "));
            info.appendSibling(withStyle(gold("ManaCraft"), style -> atURL(style, "https://github.com/Yaossg/ManaCraft")));
            info.appendSibling(gold(" and "));
            info.appendSibling(withStyle(gold("Sausage's Factory"),
                    style -> style.setItalic(true).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            withColor("Working in progress and coming soon (or later?)", TextFormatting.GREEN)))));
            info.appendSibling(gold("?"));
            if(!Loader.isModLoaded("mana_craft")) {
                info.appendText("\n");
                info.appendSibling(red("No %s is detected", "ManaCraft").appendText(" :("));
            }
            if(!Loader.isModLoaded("sausages_factory")) {
                info.appendText("\n");
                info.appendSibling(red("No %s is detected", "Sausage's Factory").appendText(" :("))
                        .appendText(" but it hasn't released, has it? :)");
            }
            playerIn.sendMessage(info);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
    // Helpers
    static ITextComponent translate(String s, Object... args) {
        return new TextComponentTranslation("sausage_core.info_card." + s, args);
    }
    static ITextComponent title(String s) {
        return withStyle(new TextComponentString(s), style -> style.setBold(true));
    }
    static ITextComponent gold(String s) {
        return withColor(s, TextFormatting.GOLD);
    }
    static ITextComponent red(String s, String a) {
        return translate(s, a).setStyle(new Style().setColor(TextFormatting.RED));
    }
    static ITextComponent withColor(String s, TextFormatting color) {
        return new TextComponentString(s).setStyle(new Style().setColor(color));
    }
    static ITextComponent withStyle(ITextComponent text, UnaryOperator<Style> style) {
        return text.setStyle(style.apply(text.getStyle()));
    }
    static Style atURL(Style style, String url) {
        return style.setUnderlined(true).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
    }
}
