package xyz.nikgub.pyromancer.data;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registries.ItemRegistry;

import java.util.function.Consumer;

public class RecipesDatagen extends RecipeProvider {
    public RecipesDatagen(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ItemRegistry.BLAZING_JOURNAL.get())
                .requires(ItemRegistry.AMBER.get())
                .requires(Items.BLAZE_POWDER)
                .requires(Items.BOOK)
                .unlockedBy("blaze_powder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_POWDER))
                .save(consumer, "blazing_journal");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.WOODEN_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', ItemTags.PLANKS)
                .define('s', Items.STICK)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer, "wooden_mace");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.STONE_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', ItemTags.STONE_TOOL_MATERIALS)
                .define('s', Items.STICK)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer, "stone_mace");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.IRON_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', Items.IRON_INGOT)
                .define('s', Items.STICK)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer, "iron_mace");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.GOLDEN_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', Items.GOLD_INGOT)
                .define('s', Items.STICK)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer, "golden_mace");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.DIAMOND_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', Items.DIAMOND)
                .define('s', Items.STICK)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer, "diamond_mace");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.NETHERITE_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', Items.NETHERITE_INGOT)
                .define('s', Items.STICK)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer, "netherite_mace");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.AMBER_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', ItemRegistry.AMBER.get())
                .define('s', Items.STICK)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer, "amber_mace");
    }
}
