package sausage_core.api.core.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {
	private final ModelResourceLocation location;

	public FluidStateMapper(ResourceLocation location) {
		this.location = new ModelResourceLocation(location, "fluid");
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		return location;
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		return location;
	}
}
