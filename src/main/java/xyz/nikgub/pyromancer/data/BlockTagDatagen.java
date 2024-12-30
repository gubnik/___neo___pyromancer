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

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.BlockRegistry;

import java.util.concurrent.CompletableFuture;

public class BlockTagDatagen extends IntrinsicHolderTagsProvider<Block>
{
    public static final TagKey<Block> FLAMING_GROVE_PLANT_ON = create("flaming_grove_plant_on");
    public static final TagKey<Block> FLAMING_GROVE_NATIVE = create("flaming_grove_native");
    public static final TagKey<Block> AMBER_REPLACEABLE = create("amber_replaceable");
    public static final TagKey<Block> BLUE_ICE_CHUNK_REPLACEABLE = create("blue_ice_replaceable");


    protected BlockTagDatagen (PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, Registries.BLOCK, completableFuture, (p_255627_) -> p_255627_.builtInRegistryHolder().key(), modId, existingFileHelper);
    }

    protected static TagKey<Block> create (String tagKey)
    {
        return BlockTags.create(new ResourceLocation(PyromancerMod.MOD_ID, tagKey));
    }

    @Override
    protected void addTags (HolderLookup.@NotNull Provider provider)
    {

        this.tag(AMBER_REPLACEABLE)
                .add(Blocks.NETHERRACK);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockRegistry.PYROMOSSED_NETHERRACK.get())
                .add(BlockRegistry.AMBER_BLOCK.get())
                .add(BlockRegistry.NATURAL_AMBER.get())
                .add(BlockRegistry.RIMEBLOOD_CELL.get())
                .add(BlockRegistry.RIMEBLOOD_BLOCK.get());

        this.tag(BlockTags.NYLIUM)
                .add(BlockRegistry.PYROMOSSED_NETHERRACK.get());

        this.tag(FLAMING_GROVE_PLANT_ON)
                .add(BlockRegistry.PYROMOSSED_NETHERRACK.get());

        for (Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.getDescriptionId().contains("pyrowood")).toList())
        {
            this.tag(BlockTags.MINEABLE_WITH_AXE)
                    .add(block);
            this.tag(FLAMING_GROVE_NATIVE)
                    .add(block);
        }

        this.tag(BlockTags.LEAVES)
                .add(BlockRegistry.PYROWOOD_LEAVES.get());

        this.tag(FLAMING_GROVE_NATIVE)
                .add(BlockRegistry.SIZZLING_VINE.get())
                .add(BlockRegistry.PYROMOSS_SPROUTS.get())
                .add(BlockRegistry.FIREBRIAR.get())
                .add(BlockRegistry.NETHER_LILY.get())
                .add(BlockRegistry.BLAZING_POPPY.get());

        this.tag(BLUE_ICE_CHUNK_REPLACEABLE)
                .addTag(BlockTags.OVERWORLD_CARVER_REPLACEABLES)
                .add(Blocks.AIR)
                .add(Blocks.SNOW_BLOCK);

        /* VANILLA */
        for (Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof FenceBlock).toList())
            this.tag(BlockTags.FENCES).add(block);
        for (Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof FenceGateBlock).toList())
            this.tag(BlockTags.FENCE_GATES).add(block);
        for (Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.getDescriptionId().contains("log")).toList())
            this.tag(BlockTags.LOGS).add(block);
        for (Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof DoorBlock && DoorBlock.isWoodenDoor(block.defaultBlockState())).toList())
            this.tag(BlockTags.WOODEN_DOORS).add(block);
    }
}
