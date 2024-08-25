package xyz.nikgub.pyromancer.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registries.BlockRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BlockStateDatagen extends BlockStateProvider
{
    public static final List<Block> EXCEPTIONS = List.of(
            BlockRegistry.SIZZLING_VINE.get(),
            BlockRegistry.FIREBRIAR.get()
    );
    public static final Map<Block, Consumer<BlockStateDatagen>> CUSTOM = Map.of(
            BlockRegistry.PYROWOOD_LOG.get(), (blockStateDatagen -> blockStateDatagen.axisBlock(BlockRegistry.PYROWOOD_LOG.get(), new ResourceLocation(PyromancerMod.MOD_ID, "block/pyrowood_log_side"), new ResourceLocation(PyromancerMod.MOD_ID, "block/pyrowood_log_top"))),
            BlockRegistry.STRIPPED_PYROWOOD_LOG.get(), (blockStateDatagen -> blockStateDatagen.axisBlock(BlockRegistry.STRIPPED_PYROWOOD_LOG.get(), new ResourceLocation(PyromancerMod.MOD_ID, "block/stripped_pyrowood_log_side"), new ResourceLocation(PyromancerMod.MOD_ID, "block/stripped_pyrowood_log_top"))),
            BlockRegistry.PYROMOSSED_NETHERRACK.get(), (blockModelDatagen ->
            {
                var builder = blockModelDatagen.modelDatagen.generatedModels.get(new ResourceLocation(PyromancerMod.MOD_ID, "block/pyromossed_netherrack"));
                blockModelDatagen.simpleBlock(BlockRegistry.PYROMOSSED_NETHERRACK.get(), builder);
            }),
            BlockRegistry.RIMEBLOOD_CELL.get(), (blockModelDatagen ->
            {
                var builder = blockModelDatagen.modelDatagen.generatedModels.get(new ResourceLocation(PyromancerMod.MOD_ID, "block/rimeblood_cell"));
                blockModelDatagen.simpleBlock(BlockRegistry.RIMEBLOOD_CELL.get(), builder);
            }),
            BlockRegistry.PYROWOOD_LEAVES.get(), (blockModelDatagen ->
            {
                blockModelDatagen.simpleBlock(BlockRegistry.PYROWOOD_LEAVES.get(), blockModelDatagen.models().cubeAll("pyrowood_leaves", new ResourceLocation(PyromancerMod.MOD_ID, "block/pyrowood_leaves" )).renderType("cutout_mipped"));
            })
    );
    public final BlockModelDatagen modelDatagen;

    public BlockStateDatagen (PackOutput output, ExistingFileHelper exFileHelper, BlockModelDatagen modelDatagen)
    {
        super(output, PyromancerMod.MOD_ID, exFileHelper);
        this.modelDatagen = modelDatagen;
    }

    @Override
    protected void registerStatesAndModels ()
    {
        for (RegistryObject<Block> entry : BlockRegistry.BLOCKS.getEntries())
        {
            Block block = entry.get();
            String blockId = entry.getId().getPath();

            if (EXCEPTIONS.contains(block)) continue;

            if (CUSTOM.containsKey(block))
            {
                CUSTOM.get(block).accept(this);
                continue;
            }

            String correctName = blockId;
            if (block instanceof StairBlock stairBlock)
            {
                correctName = correctName.replaceFirst("_stairs", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.stairsBlockWithRenderType(stairBlock, all, "cutout_mipped");
            } else if (block instanceof SlabBlock slabBlock)
            {
                correctName = correctName.replaceFirst("_slab", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.slabBlock(slabBlock, new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId + "_full"), all);
            } else if (block instanceof FenceBlock fenceBlock)
            {
                correctName = correctName.replaceFirst("_fence", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.fenceBlockWithRenderType(fenceBlock, all, "cutout_mipped");
            } else if (block instanceof WallBlock wallBlock)
            {
                correctName = correctName.replaceFirst("_wall", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.wallBlockWithRenderType(wallBlock, all, "cutout_mipped");
            } else if (block instanceof FenceGateBlock fenceGateBlock)
            {
                correctName = correctName.replaceFirst("_fence_gate", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.fenceGateBlockWithRenderType(fenceGateBlock, all, "cutout_mipped");
            } else if (block instanceof TrapDoorBlock trapDoorBlock)
            {
                correctName = correctName.replaceFirst("_trapdoor", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                ResourceLocation other = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId);
                this.trapdoorBlockWithRenderType(trapDoorBlock, other, true, "cutout_mipped");
            } else if (block instanceof DoorBlock doorBlock)
            {
                correctName = correctName.replaceFirst("_door", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                ResourceLocation bottom = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId + "_bottom");
                ResourceLocation top = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId + "_top");
                this.doorBlockWithRenderType(doorBlock, bottom, top, "cutout_mipped");
            } else if (block instanceof IPlantable || block instanceof GrowingPlantBlock)
            {
                this.simpleBlock(block, models().cross(blockId, new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId)).renderType("cutout_mipped"));
            } else
            {
                this.simpleBlock(block);
            }
        }
    }
}
