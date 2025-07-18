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
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.BlockRegistry;

import java.util.Map;
import java.util.function.Consumer;

public class BlockModelDatagen extends BlockModelProvider
{
    public static final Map<Block, Consumer<BlockModelDatagen>> CUSTOM = Map.of(
        BlockRegistry.PYROWOOD_LOG.get(), (blockModelDatagen -> blockModelDatagen.cubeColumn("pyrowood_log", ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/pyrowood_log_side"), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/pyrowood_log_top"))),
        BlockRegistry.STRIPPED_PYROWOOD_LOG.get(), (blockModelDatagen -> blockModelDatagen.cubeColumn("stripped_pyrowood_log", ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/stripped_pyrowood_log_side"), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/stripped_pyrowood_log_top"))),
        BlockRegistry.PYROMOSSED_NETHERRACK.get(), (blockModelDatagen -> blockModelDatagen.cubeBottomTop("pyromossed_netherrack", ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/pyromoss_side"), ResourceLocation.fromNamespaceAndPath("minecraft", "block/netherrack"), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/pyromoss"))),
        BlockRegistry.RIMEBLOOD_CELL.get(), (blockModelDatagen -> blockModelDatagen.cubeBottomTop("rimeblood_cell", ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/rimeblood_cell_side"), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/rimeblood_cell_end"), ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/rimeblood_cell_end"))),
        BlockRegistry.HOGTRAP.get(), (blockModelDatagen ->
        {
        })
    );

    public BlockModelDatagen (PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, PyromancerMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels ()
    {
        for (RegistryObject<Block> entry : BlockRegistry.BLOCKS.getEntries())
        {
            Block block = entry.get();
            String blockId = entry.getId().getPath();

            if (CUSTOM.containsKey(block))
            {
                CUSTOM.get(block).accept(this);
                continue;
            }

            String correctName = blockId;
            if (block instanceof StairBlock)
            {
                correctName = correctName.replaceFirst("_stairs", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.stairs(blockId, all, all, all);
            } else if (block instanceof SlabBlock)
            {
                correctName = correctName.replaceFirst("_slab", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.slab(correctName, all, all, all);
                this.cubeBottomTop(correctName + "_slab_full", all, all, all);
            } else if (block instanceof FenceBlock)
            {
                correctName = correctName.replaceFirst("_fence", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.fenceSide(blockId + "_side", all);
                this.fenceInventory(blockId + "_inventory", all);
                this.fencePost(blockId + "_post", all);
            } else if (block instanceof WallBlock)
            {
                correctName = correctName.replaceFirst("_wall", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.wallSide(blockId + "_side", all);
                this.wallSideTall(blockId + "_side_tall", all);
                this.wallInventory(blockId + "_inventory", all);
                this.wallPost(blockId + "_post", all);
            } else if (block instanceof FenceGateBlock)
            {
                correctName = correctName.replaceFirst("_fence_gate", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.fenceGate(blockId, all);
                this.fenceGateWall(blockId + "_wall", all);
                this.fenceGateOpen(blockId + "_open", all);
                this.fenceGateWallOpen(blockId + "_wall_open", all);
            } else if (block instanceof TrapDoorBlock)
            {
                correctName = correctName.replaceFirst("_trapdoor", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                ResourceLocation other = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId);
                this.trapdoorOrientableBottom(blockId + "_bottom", other);
                this.trapdoorOrientableTop(blockId + "_top", other);
                this.trapdoorOrientableOpen(blockId + "_open", other);
            } else if (block instanceof DoorBlock)
            {
                correctName = correctName.replaceFirst("_door", "");
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                ResourceLocation bottom = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId + "_bottom");
                ResourceLocation top = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId + "_top");
                this.doorBottomLeft(blockId + "_bottom_left", bottom, top);
                this.doorBottomLeftOpen(blockId + "_bottom_left_open", bottom, top);
                this.doorBottomRight(blockId + "_bottom_right", bottom, top);
                this.doorBottomRightOpen(blockId + "_bottom_right_open", bottom, top);
                this.doorTopLeft(blockId + "_top_left", bottom, top);
                this.doorTopLeftOpen(blockId + "_top_left_open", bottom, top);
                this.doorTopRight(blockId + "_top_right", bottom, top);
                this.doorTopRightOpen(blockId + "_top_right_open", bottom, top);
            } else if (block instanceof IPlantable)
            {
                ResourceLocation all = ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + correctName);
                this.cross(blockId, all);
            } else
            {
                this.cubeAll(blockId, ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + blockId));
            }
        }
    }
}
