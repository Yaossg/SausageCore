package sausage_core.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSausage extends ItemFood {
	public ItemSausage() {
		super(0, 0, false);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 128;
	}

	private static final DamageSource overeating = new DamageSource("overeating").setDamageIsAbsolute();

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		player.attackEntityFrom(overeating, 256);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("item.sausage_core.sausage.info"));
	}
}
