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
            BlockRegistry.PYROWOOD_LOG.get(), (blockModelDatagen -> blockModelDatagen.cubeColumn("pyrowood_log", new ResourceLocation(PyromancerMod.MOD_ID, "block/pyrowood_log_side"), new ResourceLocation(PyromancerMod.MOD_ID, "block/pyrowood_log_top"))),
            BlockRegistry.STRIPPED_PYROWOOD_LOG.get(), (blockModelDatagen -> blockModelDatagen.cubeColumn("stripped_pyrowood_log", new ResourceLocation(PyromancerMod.MOD_ID, "block/stripped_pyrowood_log_side"), new ResourceLocation(PyromancerMod.MOD_ID, "block/stripped_pyrowood_log_top"))),
            BlockRegistry.PYROMOSSED_NETHERRACK.get(), (blockModelDatagen -> blockModelDatagen.cubeBottomTop("pyromossed_netherrack", new ResourceLocation(PyromancerMod.MOD_ID, "block/pyromoss_side"), new ResourceLocation("minecraft", "block/netherrack"), new ResourceLocation(PyromancerMod.MOD_ID, "block/pyromoss"))),
            BlockRegistry.RIMEBLOOD_CELL.get(), (blockModelDatagen -> blockModelDatagen.cubeBottomTop("rimeblood_cell", new ResourceLocation(PyromancerMod.MOD_ID, "block/rimeblood_cell_side"), new ResourceLocation(PyromancerMod.MOD_ID, "block/rimeblood_cell_end"), new ResourceLocation(PyromancerMod.MOD_ID, "block/rimeblood_cell_end")))
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
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.stairs(blockId, all, all, all);
            } else if (block instanceof SlabBlock)
            {
                correctName = correctName.replaceFirst("_slab", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.slab(correctName, all, all, all);
                this.cubeBottomTop(correctName + "_slab_full", all, all, all);
            } else if (block instanceof FenceBlock)
            {
                correctName = correctName.replaceFirst("_fence", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.fenceSide(blockId + "_side", all);
                this.fenceInventory(blockId + "_inventory", all);
                this.fencePost(blockId + "_post", all);
            } else if (block instanceof WallBlock)
            {
                correctName = correctName.replaceFirst("_wall", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.wallSide(blockId + "_side", all);
                this.wallSideTall(blockId + "_side_tall", all);
                this.wallInventory(blockId + "_inventory", all);
                this.wallPost(blockId + "_post", all);
            } else if (block instanceof FenceGateBlock)
            {
                correctName = correctName.replaceFirst("_fence_gate", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.fenceGate(blockId, all);
                this.fenceGateWall(blockId + "_wall", all);
                this.fenceGateOpen(blockId + "_open", all);
                this.fenceGateWallOpen(blockId + "_wall_open", all);
            } else if (block instanceof TrapDoorBlock)
            {
                correctName = correctName.replaceFirst("_trapdoor", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                ResourceLocation other = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId);
                this.trapdoorOrientableBottom(blockId + "_bottom", other);
                this.trapdoorOrientableTop(blockId + "_top", other);
                this.trapdoorOrientableOpen(blockId + "_open", other);
            } else if (block instanceof DoorBlock)
            {
                correctName = correctName.replaceFirst("_door", "");
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                ResourceLocation bottom = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId + "_bottom");
                ResourceLocation top = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId + "_top");
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
                ResourceLocation all = new ResourceLocation(PyromancerMod.MOD_ID, "block/" + correctName);
                this.cross(blockId, all);
            } else
            {
                this.cubeAll(blockId, new ResourceLocation(PyromancerMod.MOD_ID, "block/" + blockId));
            }
        }
    }
}
