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

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registry.EntityTypeRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

public class EntityLootTableDatagen extends EntityLootSubProvider
{
    protected EntityLootTableDatagen ()
    {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate ()
    {
        this.add(EntityTypeRegistry.FROSTCOPPER_GOLEM.get(), LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(Items.COPPER_INGOT)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
            .withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(ItemRegistry.ANCIENT_PLATING.get()))
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.04F, 0.01F))));
        this.add(EntityTypeRegistry.UNBURNED.get(), LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(ItemRegistry.MEMORY_OF_FIRE.get())
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))))
        );
        this.add(EntityTypeRegistry.SCORCH.get(), LootTable.lootTable()
            .withPool(LootPool.lootPool())
        );
        this.add(EntityTypeRegistry.PYRACORN.get(), LootTable.lootTable()
            .withPool(LootPool.lootPool())
        );
        this.add(EntityTypeRegistry.PYROENT.get(), LootTable.lootTable()
            .withPool(LootPool.lootPool())
        );
        this.add(EntityTypeRegistry.RIMEGAZER.get(), LootTable.lootTable()
            .withPool(LootPool.lootPool())
        );
    }

    @Override
    protected java.util.stream.@NotNull Stream<EntityType<?>> getKnownEntityTypes ()
    {
        return EntityTypeRegistry.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
    }
}
