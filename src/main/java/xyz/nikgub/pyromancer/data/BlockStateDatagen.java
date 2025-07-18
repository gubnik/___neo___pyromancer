/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, Nikolay Gubankov (aka nikgub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package xyz.nikgub.pyromancer.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.BlockRegistry;

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
        BlockRegistry.PYROWOOD_LOG.get(), (blockStateDatagen -> blockStateDatagen.axisBlock(BlockRegistry.PYROWOOD_LOG.get(), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/pyrowood_log_side"), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/pyrowood_log_top"))),
        BlockRegistry.STRIPPED_PYROWOOD_LOG.get(), (blockStateDatagen -> blockStateDatagen.axisBlock(BlockRegistry.STRIPPED_PYROWOOD_LOG.get(), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/stripped_pyrowood_log_side"), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/stripped_pyrowood_log_top"))),
        BlockRegistry.PYROMOSSED_NETHERRACK.get(), (blockModelDatagen ->
        {
            var builder = blockModelDatagen.modelDatagen.generatedModels.get(ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/pyromossed_netherrack"));
            blockModelDatagen.simpleBlock(BlockRegistry.PYROMOSSED_NETHERRACK.get(), builder);
        }),
        BlockRegistry.RIMEBLOOD_CELL.get(), (blockModelDatagen ->
        {
            var builder = blockModelDatagen.modelDatagen.generatedModels.get(ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/rimeblood_cell"));
            blockModelDatagen.simpleBlock(BlockRegistry.RIMEBLOOD_CELL.get(), builder);
        }),
        BlockRegistry.PYROWOOD_LEAVES.get(), (blockModelDatagen ->
        {
            blockModelDatagen.simpleBlock(BlockRegistry.PYROWOOD_LEAVES.get(), blockModelDatagen.models().cubeAll("pyrowood_leaves", ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/pyrowood_leaves")).renderType("cutout_mipped"));
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
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.stairsBlockWithRenderType(stairBlock, all, "cutout_mipped");
            } else if (block instanceof SlabBlock slabBlock)
            {
                correctName = correctName.replaceFirst("_slab", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.slabBlock(slabBlock, ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId + "_full"), all);
            } else if (block instanceof FenceBlock fenceBlock)
            {
                correctName = correctName.replaceFirst("_fence", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.fenceBlockWithRenderType(fenceBlock, all, "cutout_mipped");
            } else if (block instanceof WallBlock wallBlock)
            {
                correctName = correctName.replaceFirst("_wall", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.wallBlockWithRenderType(wallBlock, all, "cutout_mipped");
            } else if (block instanceof FenceGateBlock fenceGateBlock)
            {
                correctName = correctName.replaceFirst("_fence_gate", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.fenceGateBlockWithRenderType(fenceGateBlock, all, "cutout_mipped");
            } else if (block instanceof TrapDoorBlock trapDoorBlock)
            {
                correctName = correctName.replaceFirst("_trapdoor", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                ResourceLocation other = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId);
                this.trapdoorBlockWithRenderType(trapDoorBlock, other, true, "cutout_mipped");
            } else if (block instanceof DoorBlock doorBlock)
            {
                correctName = correctName.replaceFirst("_door", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                ResourceLocation bottom = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId + "_bottom");
                ResourceLocation top = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId + "_top");
                this.doorBlockWithRenderType(doorBlock, bottom, top, "cutout_mipped");
            } else if (block instanceof IPlantable || block instanceof GrowingPlantBlock)
            {
                this.simpleBlock(block, models().cross(blockId, ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId)).renderType("cutout_mipped"));
            } else
            {
                this.simpleBlock(block);
            }
        }
    }
}
