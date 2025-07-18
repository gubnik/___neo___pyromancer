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

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.BiomeRegistry;
import xyz.nikgub.pyromancer.registry.BlockRegistry;
import xyz.nikgub.pyromancer.registry.EntityTypeRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementDatagen extends ForgeAdvancementProvider
{
    public AdvancementDatagen (PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders)
    {
        super(output, registries, existingFileHelper, subProviders);
    }

    public static class PyromancerAdvancements implements AdvancementGenerator
    {
        @Override
        @SuppressWarnings("unused")
        public void generate (HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper)
        {

            Advancement blazing_journal_acquired = Advancement.Builder.advancement()
                .display(ItemRegistry.BLAZING_JOURNAL.get(),
                    Component.translatable("advancement.pyromancer.blazing_journal.title"),
                    Component.translatable("advancement.pyromancer.blazing_journal.description"),
                    ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "textures/block/pyrowood_planks.png"),
                    FrameType.TASK,
                    false, false, false)
                .addCriterion("acquired_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.BLAZING_JOURNAL.get()))
                .save(saver, "pyromancer:pyromancer/root");

            Advancement ancient_plating_obtained = Advancement.Builder.advancement().parent(blazing_journal_acquired)
                .display(ItemRegistry.ANCIENT_PLATING.get(),
                    Component.translatable("advancement.pyromancer.ancient_plating_obtained.title"),
                    Component.translatable("advancement.pyromancer.ancient_plating_obtained.description"),
                    null,
                    FrameType.GOAL,
                    true, true, false)
                .addCriterion("ancient_plating_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.ANCIENT_PLATING.get()))
                .save(saver, "pyromancer:pyromancer/ancient_plating_obtained");

            Advancement quill_applied = Advancement.Builder.advancement().parent(blazing_journal_acquired)
                .display(ItemRegistry.BLAZING_QUILL.get(),
                    Component.translatable("advancement.pyromancer.quill_applied.title"),
                    Component.translatable("advancement.pyromancer.quill_applied.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .addCriterion("quill_applied", new ImpossibleTrigger.TriggerInstance())
                .save(saver, "pyromancer:pyromancer/quill_applied");

            Advancement journal_projection = Advancement.Builder.advancement().parent(quill_applied)
                .display(ItemRegistry.BLAZING_JOURNAL.get(),
                    Component.translatable("advancement.pyromancer.journal_projection.title"),
                    Component.translatable("advancement.pyromancer.journal_projection.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .addCriterion("journal_projection", new ImpossibleTrigger.TriggerInstance())
                .save(saver, "pyromancer:pyromancer/journal_projection");

            Advancement flaming_grove_visited = Advancement.Builder.advancement().parent(blazing_journal_acquired)
                .display(BlockRegistry.PYROWOOD_SAPLING.get(),
                    Component.translatable("advancement.pyromancer.flaming_grove_visited.title"),
                    Component.translatable("advancement.pyromancer.flaming_grove_visited.description"),
                    null,
                    FrameType.GOAL,
                    true, true, false)
                .addCriterion("flaming_grove_visited", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeRegistry.FLAMING_GROVE)))
                .save(saver, "pyromancer:pyromancer/flaming_grove_visited");

            Advancement amber_obtained = Advancement.Builder.advancement().parent(flaming_grove_visited)
                .display(ItemRegistry.AMBER.get(),
                    Component.translatable("advancement.pyromancer.amber_obtained.title"),
                    Component.translatable("advancement.pyromancer.amber_obtained.description"),
                    null,
                    FrameType.GOAL,
                    true, true, false)
                .addCriterion("amber_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.AMBER.get()))
                .save(saver, "pyromancer:pyromancer/amber_obtained");

            Advancement pyromancy_obtained = Advancement.Builder.advancement().parent(amber_obtained)
                .display(ItemRegistry.COURT_OF_EMBERS.get(),
                    Component.translatable("advancement.pyromancer.pyromancy_obtained.title"),
                    Component.translatable("advancement.pyromancer.pyromancy_obtained.description"),
                    null,
                    FrameType.GOAL,
                    true, true, false)
                .addCriterion("pyromancy_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTagDatagen.PYROMANCY).build()))
                .save(saver, "pyromancer:pyromancer/pyromancy_obtained");

            Advancement sizzling_hand_obtained = Advancement.Builder.advancement().parent(pyromancy_obtained)
                .display(ItemRegistry.SIZZLING_HAND.get(),
                    Component.translatable("advancement.pyromancer.sizzling_hand_obtained.title"),
                    Component.translatable("advancement.pyromancer.sizzling_hand_obtained.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .addCriterion("sizzling_hand_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SIZZLING_HAND.get()))
                .save(saver, "pyromancer:pyromancer/sizzling_hand_obtained");

            Advancement court_of_embers_obtained = Advancement.Builder.advancement().parent(pyromancy_obtained)
                .display(ItemRegistry.COURT_OF_EMBERS.get(),
                    Component.translatable("advancement.pyromancer.court_of_embers_obtained.title"),
                    Component.translatable("advancement.pyromancer.court_of_embers_obtained.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .addCriterion("court_of_embers_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.COURT_OF_EMBERS.get()))
                .save(saver, "pyromancer:pyromancer/court_of_embers_obtained");

            Advancement unburned_defeated = Advancement.Builder.advancement().parent(flaming_grove_visited)
                .display(ItemRegistry.MEMORY_OF_FIRE.get(),
                    Component.translatable("advancement.pyromancer.unburned_defeated.title"),
                    Component.translatable("advancement.pyromancer.unburned_defeated.description"),
                    null,
                    FrameType.GOAL,
                    true, true, false)
                .rewards(AdvancementRewards.Builder.experience(500))
                .addCriterion("unburned_defeated", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityTypeRegistry.UNBURNED.get())))
                .save(saver, "pyromancer:pyromancer/unburned_defeated");

            Advancement pyromancer_armor_obtained = Advancement.Builder.advancement().parent(unburned_defeated)
                .display(ItemRegistry.PYROMANCER_CHESTPLATE.get(),
                    Component.translatable("advancement.pyromancer.pyromancer_armor_obtained.title"),
                    Component.translatable("advancement.pyromancer.pyromancer_armor_obtained.description"),
                    null,
                    FrameType.CHALLENGE,
                    true, true, false)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("pyromancer_armor_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.PYROMANCER_HELMET.get(), ItemRegistry.PYROMANCER_CHESTPLATE.get(), ItemRegistry.PYROMANCER_LEGGINGS.get(), ItemRegistry.PYROMANCER_BOOTS.get()))
                .save(saver, "pyromancer:pyromancer/pyromancer_armor_obtained");

            Advancement tainted_monarch_armor_obtained = Advancement.Builder.advancement().parent(pyromancer_armor_obtained)
                .display(ItemRegistry.TAINTED_MONARCH_CHESTPLATE.get(),
                    Component.translatable("advancement.pyromancer.tainted_monarch_armor_obtained.title"),
                    Component.translatable("advancement.pyromancer.tainted_monarch_armor_obtained.description"),
                    null,
                    FrameType.CHALLENGE,
                    true, true, false)
                .rewards(AdvancementRewards.Builder.experience(200))
                .addCriterion("tainted_monarch_armor_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.TAINTED_MONARCH_HELMET.get(), ItemRegistry.TAINTED_MONARCH_CHESTPLATE.get(), ItemRegistry.TAINTED_MONARCH_LEGGINGS.get(), ItemRegistry.TAINTED_MONARCH_BOOTS.get()))
                .save(saver, "pyromancer:pyromancer/tainted_monarch_armor_obtained");

            Advancement zweihander_obtained = Advancement.Builder.advancement().parent(ancient_plating_obtained)
                .display(ItemRegistry.ZWEIHANDER.get(),
                    Component.translatable("advancement.pyromancer.zweihander_obtained.title"),
                    Component.translatable("advancement.pyromancer.zweihander_obtained.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .addCriterion("zweihander_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.ZWEIHANDER.get()))
                .save(saver, "pyromancer:pyromancer/zweihander_obtained");

            Advancement musket_obtained = Advancement.Builder.advancement().parent(ancient_plating_obtained)
                .display(ItemRegistry.MUSKET.get(),
                    Component.translatable("advancement.pyromancer.musket_obtained.title"),
                    Component.translatable("advancement.pyromancer.musket_obtained.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .addCriterion("musket_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.MUSKET.get()))
                .save(saver, "pyromancer:pyromancer/musket_obtained");

            Advancement permafrost_caverns_visited = Advancement.Builder.advancement().parent(blazing_journal_acquired)
                .display(BlockRegistry.RIMEBLOOD_CELL.get(),
                    Component.translatable("advancement.pyromancer.permafrost_caverns_visited.title"),
                    Component.translatable("advancement.pyromancer.permafrost_caverns_visited.description"),
                    null,
                    FrameType.GOAL,
                    true, true, false)
                .addCriterion("permafrost_caverns_visited", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeRegistry.PERMAFROST_CAVERNS)))
                .save(saver, "pyromancer:pyromancer/permafrost_caverns_visited");

            Advancement spear_of_moroz_obtained = Advancement.Builder.advancement().parent(permafrost_caverns_visited)
                .display(ItemRegistry.SPEAR_OF_MOROZ.get(),
                    Component.translatable("advancement.pyromancer.spear_of_moroz_obtained.title"),
                    Component.translatable("advancement.pyromancer.spear_of_moroz_obtained.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("spear_of_moroz_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SPEAR_OF_MOROZ.get()))
                .save(saver, "pyromancer:pyromancer/spear_of_moroz_obtained");

            Advancement hoarfrost_greatsword_obtained = Advancement.Builder.advancement().parent(permafrost_caverns_visited)
                .display(ItemRegistry.HOARFROST_GREATSWORD.get(),
                    Component.translatable("advancement.pyromancer.hoarfrost_greatsword_obtained.title"),
                    Component.translatable("advancement.pyromancer.hoarfrost_greatsword_obtained.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("hoarfrost_greatsword_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.HOARFROST_GREATSWORD.get()))
                .save(saver, "pyromancer:pyromancer/hoarfrost_greatsword_obtained");

            Advancement symbol_of_sun_obtained = Advancement.Builder.advancement().parent(unburned_defeated)
                .display(ItemRegistry.SYMBOL_OF_SUN.get(),
                    Component.translatable("advancement.pyromancer.symbol_of_sun.title"),
                    Component.translatable("advancement.pyromancer.symbol_of_sun.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .addCriterion("symbol_of_sun_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SYMBOL_OF_SUN.get()))
                .save(saver, "pyromancer:pyromancer/symbol_of_sun_obtained");

            Advancement flammenklinge_obtained = Advancement.Builder.advancement().parent(unburned_defeated)
                .display(ItemRegistry.FLAMMENKLINGE.get(),
                    Component.translatable("advancement.pyromancer.flammenklinge.title"),
                    Component.translatable("advancement.pyromancer.flammenklinge.description"),
                    null,
                    FrameType.TASK,
                    true, true, false)
                .addCriterion("flammenklinge_obtained", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.FLAMMENKLINGE.get()))
                .save(saver, "pyromancer:pyromancer/flammenklinge_obtained");

            Advancement flammenklinge_plunge = Advancement.Builder.advancement().parent(flammenklinge_obtained)
                .display(ItemRegistry.FLAMMENKLINGE.get(),
                    Component.translatable("advancement.pyromancer.flammenklinge_plunge.title"),
                    Component.translatable("advancement.pyromancer.flammenklinge_plunge.description"),
                    null,
                    FrameType.CHALLENGE,
                    true, true, false)
                .addCriterion("flammenklinge_plunge", new ImpossibleTrigger.TriggerInstance())
                .save(saver, "pyromancer:pyromancer/flammenklinge_plunge");

            Advancement flammenklinge_death = Advancement.Builder.advancement().parent(flammenklinge_obtained)
                .display(ItemRegistry.FLAMMENKLINGE.get(),
                    Component.translatable("advancement.pyromancer.flammenklinge_death.title"),
                    Component.translatable("advancement.pyromancer.flammenklinge_death.description"),
                    null,
                    FrameType.CHALLENGE,
                    true, true, false)
                .addCriterion("flammenklinge_death", new ImpossibleTrigger.TriggerInstance())
                .save(saver, "pyromancer:pyromancer/flammenklinge_death");
        }
    }
}
