package net.nikgub.pyromancer.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.registries.vanila.ItemRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementDatagen extends ForgeAdvancementProvider {
    public AdvancementDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
        super(output, registries, existingFileHelper, subProviders);
        subProviders.add(new PyromancerAdvancements());
    }
    public static class PyromancerAdvancements implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {
            Advancement blazing_journal_acquired = Advancement.Builder.advancement()
                    .display(new ItemStack(ItemRegistry.BLAZING_JOURNAL.get()),
                            Component.translatable("advancement.pyromancer.blazing_journal.title"),
                            Component.translatable("advancement.pyromancer.blazing_journal.description"),
                            new ResourceLocation(PyromancerMod.MOD_ID, "textures/block/pyrowood_planks.png"),
                            FrameType.TASK,
                            false, false, false)
                    .addCriterion("acquired_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.BLAZING_JOURNAL.get()))
                    .save(saver, "pyromancer/root");

            Advancement ember_acquired = Advancement.Builder.advancement().parent(blazing_journal_acquired)
                    .display(new ItemStack(ItemRegistry.BLAZING_JOURNAL.get()),
                            Component.translatable("advancement.pyromancer.ember_obtained.title"),
                            Component.translatable("advancement.pyromancer.ember_obtained.description"),
                            null,
                            FrameType.TASK,
                            true, false, false)
                    .addCriterion("ember_acquired", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.EMBER_ITEM.get()))
                    .save(saver, "pyromancer/ember_acquired");

            Advancement quill_applied = Advancement.Builder.advancement().parent(blazing_journal_acquired)
                    .display(new ItemStack(ItemRegistry.BLAZING_JOURNAL.get()),
                            Component.translatable("advancement.pyromancer.quill_applied.title"),
                            Component.translatable("advancement.pyromancer.quill_applied.description"),
                            null,
                            FrameType.TASK,
                            true, false, false)
                    .addCriterion("quill_applied", new ImpossibleTrigger.TriggerInstance())
                    .save(saver, "pyromancer/quill_applied");
        }
    }
}
