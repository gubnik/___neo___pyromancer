package xyz.nikgub.pyromancer.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registries.BiomeRegistry;
import xyz.nikgub.pyromancer.registries.BlockRegistry;
import xyz.nikgub.pyromancer.registries.ItemRegistry;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementDatagen extends ForgeAdvancementProvider {
    public AdvancementDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
        super(output, registries, existingFileHelper, subProviders);
    }

    public static class PyromancerAdvancements implements AdvancementGenerator {
        @Override
        @SuppressWarnings("unused")
        public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {

            Advancement blazing_journal_acquired = Advancement.Builder.advancement()
                    .display(ItemRegistry.BLAZING_JOURNAL.get(),
                            Component.translatable("advancement.pyromancer.blazing_journal.title"),
                            Component.translatable("advancement.pyromancer.blazing_journal.description"),
                            new ResourceLocation(PyromancerMod.MOD_ID, "textures/block/pyrowood_planks.png"),
                            FrameType.TASK,
                            false, false, false)
                    .addCriterion("acquired_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.BLAZING_JOURNAL.get()))
                    .save(saver, "pyromancer:pyromancer/root");

            Advancement quill_applied = Advancement.Builder.advancement().parent(blazing_journal_acquired)
                    .display(ItemRegistry.BLAZING_QUILL.get(),
                            Component.translatable("advancement.pyromancer.quill_applied.title"),
                            Component.translatable("advancement.pyromancer.quill_applied.description"),
                            null,
                            FrameType.TASK,
                            true, true, false)
                    .addCriterion("quill_applied", new ImpossibleTrigger.TriggerInstance())
                    .save(saver, "pyromancer:pyromancer/quill_applied");

            Advancement journal_projection = Advancement.Builder.advancement().parent(blazing_journal_acquired)
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
                            FrameType.TASK,
                            true, true, false)
                    .addCriterion("flaming_grove_visited", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeRegistry.FLAMING_GROVE)))
                    .save(saver, "pyromancer:pyromancer/flaming_grove_visited");
        }
    }
}
