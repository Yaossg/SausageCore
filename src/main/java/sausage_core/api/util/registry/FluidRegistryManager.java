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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static sausage_core.api.util.common.Conversions.To.item;

public final class FluidRegistryManager {
    public static final class StateMapper extends StateMapperBase implements ItemMeshDefinition {
        ModelResourceLocation location;

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
    final Map<Fluid, Function<? super Fluid, Block>> fluids = new HashMap<>();
    final IBRegistryManager inner;
    final UnaryOperator<String> pathFactory;

    public FluidRegistryManager(String modid) {
        this(modid, s -> "fluid/" + s);
    }

    public FluidRegistryManager(String modid, UnaryOperator<String> pathFactory) {
        this.modid = modid;
        this.pathFactory = pathFactory;
        inner = new IBRegistryManager(modid, null);
    }

    private Fluid createFluid(String name) {
        return new Fluid(name,
                new ResourceLocation(modid, pathFactory.apply(name) + "_still"),
                new ResourceLocation(modid, pathFactory.apply(name) + "_flow"));
    }

    public Fluid addFluid(String name, Function<Fluid, Block> blockFactory) {
        return addFluid(name, blockFactory, this::createFluid);
    }

    @SuppressWarnings("unchecked")
    public <T extends Fluid> T addFluid(String name, Function<? super T, Block> blockFactory, Function<String, T> fluidFactory) {
        T fluid = fluidFactory.apply(name);
        fluids.put(fluid.setUnlocalizedName(modid + "." + name), (Function) blockFactory);
        return fluid;
    }

    public void register() {
        fluids.keySet().forEach(FluidRegistry::registerFluid);
        if(FluidRegistry.isUniversalBucketEnabled())
            fluids.keySet().forEach(FluidRegistry::addBucketForFluid);
        fluids.forEach((key, value) -> inner.addBlock(value.apply(key), key.getName()));
        inner.registerAll();
    }

    public void loadModel() {
        inner.blocks.forEach(block -> {
            StateMapper mapper = new StateMapper(modid, block.getRegistryName().getResourcePath());
            ModelLoader.setCustomMeshDefinition(item(block), mapper);
            ModelLoader.setCustomStateMapper(block, mapper);
        });
    }
}
