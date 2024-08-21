package xyz.nikgub.pyromancer.data;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registries.BlockRegistry;
import xyz.nikgub.pyromancer.registries.ItemRegistry;

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

    protected BlockLootTableDatagen()
	{
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate()
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
    protected @NotNull Iterable<Block> getKnownBlocks()
	{
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
