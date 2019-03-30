package sausage_core.api.util.registry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static sausage_core.api.util.common.Conversions.To.item;

public class FluidRegistryManager {
	@SideOnly(Side.CLIENT)
	private static final class StateMapper extends StateMapperBase implements ItemMeshDefinition {
		private final ModelResourceLocation location;

		private StateMapper(ResourceLocation location) {
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

	public final String modid;
	final IBRegistryManager inner;
	final Map<Fluid, Function<Fluid, Block>> fluids = new HashMap<>();

	public FluidRegistryManager(String modid) {
		this.modid = modid;
		this.inner = new IBRegistryManager(modid);
	}

	private static final Function NOOP = Function.identity();

	@SuppressWarnings("unchecked")
	public <T extends Fluid> T addFluid(T fluid) {
		fluids.put(fluid, NOOP);
		return fluid;
	}

	public <T extends Fluid> T addFluid(T fluid, Function<Fluid, Block> function) {
		fluids.put(fluid, function);
		return fluid;
	}

	public void register() {
		fluids.keySet().forEach(FluidRegistry::registerFluid);
		if(FluidRegistry.isUniversalBucketEnabled())
			fluids.keySet().forEach(FluidRegistry::addBucketForFluid);
		fluids.forEach((fluid, function) -> {
			if(function != NOOP)
				inner.addBlock(fluid.getName(), fluid.setBlock(function.apply(fluid)).getBlock());
		});
		inner.registerAll();
	}

	@SideOnly(Side.CLIENT)
	public void loadModel() {
		inner.blocks.forEach(block -> {
			StateMapper mapper = new StateMapper(block.getRegistryName());
			ModelLoader.setCustomMeshDefinition(item(block), mapper);
			ModelLoader.setCustomStateMapper(block, mapper);
		});
	}
}

