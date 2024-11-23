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

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registry.BlockRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class BlockLootTableDatagen extends BlockLootSubProvider
{
    private final Map<Block, Function<Block, LootTable.Builder>> CUSTOM_DROP_BUILDERS = Map.of(
            BlockRegistry.NATURAL_AMBER.get(), block -> this.createOreDrop(block, ItemRegistry.AMBER.get()),
            BlockRegistry.PYROWOOD_LEAVES.get(), block -> this.createLeavesDrops(block, BlockRegistry.PYROWOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES),
            BlockRegistry.SIZZLING_VINE.get(), block -> createShearsOnlyDrop(BlockRegistry.SIZZLING_VINE.get()),
            BlockRegistry.PYROMOSS_SPROUTS.get(), block -> createShearsOnlyDrop(BlockRegistry.PYROMOSS_SPROUTS.get()),
            BlockRegistry.PYROMOSSED_NETHERRACK.get(), block -> this.createSingleItemTableWithSilkTouch(block, Blocks.NETHERRACK),
            BlockRegistry.RIMEBLOOD_CELL.get(), block -> this.createSingleItemTableWithSilkTouch(block, ItemRegistry.RIMEBLOOD.get(), UniformGenerator.between(1, 5))
    );

    protected BlockLootTableDatagen ()
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate ()
    {
        for (Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).toList())
        {
            if (CUSTOM_DROP_BUILDERS.containsKey(block))
                add(block, CUSTOM_DROP_BUILDERS.get(block));
            else
                dropSelf(block);
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks ()
    {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
