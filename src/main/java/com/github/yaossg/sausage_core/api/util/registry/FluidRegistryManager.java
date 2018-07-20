package com.github.yaossg.sausage_core.api.util.registry;

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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.github.yaossg.sausage_core.api.util.common.Conversions.block2item;

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

    public String modid;
    private Map<Fluid, Function<Fluid, Block>> fluids = new HashMap<>();
    private IBRegistryManager inner;

    public FluidRegistryManager(String modid) {
        this.modid = modid;
        inner = new IBRegistryManager(modid, null);
    }

    //provide default ResourceLocation
    public static Fluid createFluid(String modid, String name) {
        return createFluid(modid, name, s -> "fluid/" + s);
    }

    //provide default ResourceLocation tail
    public static Fluid createFluid(String modid, String name, UnaryOperator<String> pathFactory) {
        return new Fluid(name,
                new ResourceLocation(modid, pathFactory.apply(name) + "_still"),
                new ResourceLocation(modid, pathFactory.apply(name) + "_flow"));
    }

    public Fluid addFluid(String name, Function<Fluid, Block> blockFactory) {
        return addFluid(name, blockFactory, FluidRegistryManager::createFluid);
    }

    public Fluid addFluid(String name, Function<Fluid, Block> blockFactory, UnaryOperator<String> pathFactory) {
        return addFluid(name, blockFactory, (modid0, name0) -> createFluid(modid0, name0, pathFactory));
    }

    public Fluid addFluid(String name, Function<Fluid, Block> blockFactory, BiFunction<String, String, Fluid> fluidFactory) {
        Fluid fluid = fluidFactory.apply(modid, name);
        fluids.put(fluid, blockFactory);
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
            StateMapper mapper = new StateMapper(modid,
                    block.getRegistryName().getResourcePath());
            ModelLoader.setCustomMeshDefinition(block2item(block), mapper);
            ModelLoader.setCustomStateMapper(block, mapper);
        });
    }
}
