package net.nikgub.pyromancer.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.registries.vanila.BlockRegistry;
import net.nikgub.pyromancer.registries.vanila.WoodTypesRegistry;

import java.util.List;

public class BlockStateDatagen extends BlockStateProvider {
    public BlockStateDatagen(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PyromancerMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        List<WoodType> WOOD_TYPES = WoodTypesRegistry.VALUES;
        for(RegistryObject<Block> regBlock : BlockRegistry.BLOCKS.getEntries())
        {
            final String correctName = regBlock.getId().getPath().substring(0, regBlock.getId().getPath().indexOf('_'));
            if(WOOD_TYPES.stream().noneMatch(woodType -> woodType.name().contains(correctName))) continue;
            ResourceLocation planks = getPlankFromWoodType(correctName);
            Block block = regBlock.get();
            if(block instanceof StairBlock stairBlock)
            {
                this.stairsBlock(stairBlock, correctName, planks, planks, planks);
            }
            else if(block instanceof SlabBlock slabBlock)
            {
                this.slabBlock(slabBlock, new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName + "_slab_full"), planks, planks, planks);
            }
            else if(block instanceof FenceBlock fenceBlock)
            {
                this.fenceBlock(fenceBlock, planks);
            }
            else if(block instanceof FenceGateBlock fenceGateBlock)
            {
                this.fenceGateBlock(fenceGateBlock, planks);
            }
            else if(block instanceof DoorBlock doorBlock)
            {
                this.doorBlockWithRenderType(doorBlock, new ResourceLocation(PyromancerMod.MOD_ID, "block/pyrowood_door_bottom"), new ResourceLocation(PyromancerMod.MOD_ID, "block/pyrowood_door_top"), "cutout_mipped");
            }
            else if(block instanceof TrapDoorBlock trapDoorBlock)
            {
                this.trapdoorBlockWithRenderType(trapDoorBlock, new ResourceLocation(PyromancerMod.MOD_ID, "block/pyrowood_trapdoor"), true, "cutout_mipped");
            }
        }
    }
    private ResourceLocation getPlankFromWoodType(String s)
    {
        return new ResourceLocation(PyromancerMod.MOD_ID, "block/" + s + "_planks");
    }
}
