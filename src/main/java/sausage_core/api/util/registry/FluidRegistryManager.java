package sausage_core.api.util.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.api.core.client.FluidStateMapper;

import java.util.function.Function;

import static sausage_core.api.util.common.Conversions.To.block;

public final class FluidRegistryManager extends RegistryManagerBase {
	private final IBRegistryManager inner;

	private static void loadBlockModel(Item item) {
		loadBlockModelClient(item);
	}

	@SideOnly(Side.CLIENT)
	private static void loadBlockModelClient(Item item) {
		Block block = block(item);
		FluidStateMapper mapper = new FluidStateMapper(block.getRegistryName());
		ModelLoader.setCustomMeshDefinition(item, mapper);
		ModelLoader.setCustomStateMapper(block, mapper);
	}

	public FluidRegistryManager(String modid) {
		super(modid, false);
		inner = new IBRegistryManager(modid);
	}

	public <T extends Fluid> T register(T fluid) {
		FluidRegistry.registerFluid(fluid);
		if (FluidRegistry.isUniversalBucketEnabled())
			FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	public <T extends Fluid> T register(T fluid, Function<Fluid, Block> function) {
		Block block = function.apply(register(fluid));
		inner.addBlock(fluid.setBlock(block).getName(), block, FluidRegistryManager::loadBlockModel);
		return fluid;
	}

	public <T extends Fluid> T registerOnlyBlock(T fluid, Function<Fluid, Block> function) {
		Block block = function.apply(register(fluid));
		inner.addOnlyBlock(fluid.setBlock(block).getName(), block);
		return fluid;
	}
}

