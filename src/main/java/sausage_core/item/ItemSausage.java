package sausage_core.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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
        player.attackEntityFrom(overeating, 128);
    }
}
