package sausage_core.api.util.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static sausage_core.api.util.common.Conversions.stack2state;

@FunctionalInterface
public interface IBlockCM extends IItemCM {
	int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex);

	default int colorMultiplier(ItemStack stack, int tintIndex) {
		return colorMultiplier(stack2state(stack), null, null, tintIndex);
	}
}