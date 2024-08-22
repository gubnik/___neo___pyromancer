package xyz.nikgub.pyromancer.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class LootTableDatagen extends LootTableProvider
{
    LootTableDatagen (PackOutput packOutput)
    {
        super(packOutput, Set.of(), List.of(
                new SubProviderEntry(BlockLootTableDatagen::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(EntityLootTableDatagen::new, LootContextParamSets.ENTITY)
        ));

    }
}
