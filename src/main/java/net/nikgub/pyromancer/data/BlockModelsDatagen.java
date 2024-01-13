package net.nikgub.pyromancer.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.registries.vanila.WoodTypesRegistry;

public class BlockModelsDatagen extends BlockModelProvider {
    public BlockModelsDatagen(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, PyromancerMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        for(WoodType woodType : WoodTypesRegistry.VALUES)
        {
            String correctName = woodType.name();
            ResourceLocation planks = getPlankFromWoodType(correctName);
            this.cubeBottomTop(correctName + "_slab_full", planks, planks, planks);
            this.fenceInventory(correctName +"_fence_inventory", planks);
            this.cubeAll(correctName + "_planks", planks); // planks
        }
    }
    private ResourceLocation getPlankFromWoodType(String s)
    {
        return new ResourceLocation(PyromancerMod.MOD_ID, "block/" + s + "_planks");
    }
}
