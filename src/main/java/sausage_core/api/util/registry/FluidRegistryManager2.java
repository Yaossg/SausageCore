package sausage_core.api.util.registry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static sausage_core.api.util.common.Conversions.To.item;

public class FluidRegistryManager2 {
    public static final class StateMapper extends StateMapperBase implements ItemMeshDefinition {
        private final ModelResourceLocation location;

        StateMapper(String modid, String name) {
            location = new ModelResourceLocation(modid + ":" + name, "fluid");
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
    final List<Fluid> fluids = new ArrayList<>();

    public FluidRegistryManager2(String modid) {
        this.modid = modid;
        this.inner = new IBRegistryManager(modid);
    }

    public <T extends Fluid> T addFluid(T fluid) {
        fluids.add(fluid);
        return fluid;
    }

    public void register() {
        fluids.forEach(FluidRegistry::registerFluid);
        if(FluidRegistry.isUniversalBucketEnabled())
            fluids.forEach(FluidRegistry::addBucketForFluid);
        fluids.forEach(fluid -> inner.addBlock(fluid.getName(), fluid.getBlock()));
        inner.registerAll();
    }

    @SideOnly(Side.CLIENT)
    public void loadModel() {
        inner.blocks.forEach(block -> {
            StateMapper mapper = new StateMapper(modid, block.getRegistryName().getResourcePath());
            ModelLoader.setCustomMeshDefinition(item(block), mapper);
            ModelLoader.setCustomStateMapper(block, mapper);
        });
    }
}

