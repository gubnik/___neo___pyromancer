package xyz.nikgub.pyromancer.data;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.WOODEN_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', ItemTags.PLANKS)
                .define('s', Items.STICK)
                .unlockedBy("planks", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.STONE_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', ItemTags.STONE_TOOL_MATERIALS)
                .define('s', Items.STICK)
                .unlockedBy("stone", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.STONE_TOOL_MATERIALS).build()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.IRON_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', Items.IRON_INGOT)
                .define('s', Items.STICK)
                .unlockedBy("iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.GOLDEN_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', Items.GOLD_INGOT)
                .define('s', Items.STICK)
                .unlockedBy("gold_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.DIAMOND_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', Items.DIAMOND)
                .define('s', Items.STICK)
                .unlockedBy("diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.NETHERITE_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', Items.NETHERITE_INGOT)
                .define('s', Items.STICK)
                .unlockedBy("netherite", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.AMBER_MACE.get())
                .pattern("aaa")
                .pattern("asa")
                .pattern(" s ")
                .define('a', ItemRegistry.AMBER.get())
                .define('s', Items.STICK)
                .unlockedBy("amber", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.AMBER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.AMBER_AXE.get()).define('#', Items.STICK).define('X', ItemRegistry.AMBER.get()).pattern("XX").pattern("X#").pattern(" #").unlockedBy("amber", has(ItemRegistry.AMBER.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.AMBER_HOE.get()).define('#', Items.STICK).define('X', ItemRegistry.AMBER.get()).pattern("XX").pattern(" #").pattern(" #").unlockedBy("amber", has(ItemRegistry.AMBER.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.AMBER_PICKAXE.get()).define('#', Items.STICK).define('X', ItemRegistry.AMBER.get()).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("amber", has(ItemRegistry.AMBER.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.AMBER_SHOVEL.get()).define('#', Items.STICK).define('X', ItemRegistry.AMBER.get()).pattern("X").pattern("#").pattern("#").unlockedBy("amber", has(ItemRegistry.AMBER.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.AMBER_SWORD.get()).define('#', Items.STICK).define('X', ItemRegistry.AMBER.get()).pattern("X").pattern("X").pattern("#").unlockedBy("amber", has(ItemRegistry.AMBER.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.MARAUDER_HELM.get())
                .pattern("###")
                .pattern("# #")
                .define('#', ItemRegistry.HOGLIN_HIDE.get())
                .unlockedBy("hoglin_hide", has(ItemRegistry.HOGLIN_HIDE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.MARAUDER_CAPE.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', ItemRegistry.HOGLIN_HIDE.get())
                .unlockedBy("hoglin_hide", has(ItemRegistry.HOGLIN_HIDE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.MARAUDER_PANTS.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', ItemRegistry.HOGLIN_HIDE.get())
                .unlockedBy("hoglin_hide", has(ItemRegistry.HOGLIN_HIDE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.MARAUDER_BOOTS.get())
                .pattern("# #")
                .pattern("# #")
                .define('#', ItemRegistry.HOGLIN_HIDE.get())
                .unlockedBy("hoglin_hide", has(ItemRegistry.HOGLIN_HIDE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SIZZLING_HAND.get())
                .pattern("#a#")
                .pattern("#a#")
                .pattern("#a#")
                .define('a', ItemRegistry.AMBER.get())
                .define('#', ItemRegistry.HOGLIN_HIDE.get())
                .unlockedBy("everburning_heart", has(ItemRegistry.EVENBURNING_HEART.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.COURT_OF_EMBERS.get())
                .pattern("#a#")
                .pattern("#s#")
                .pattern(" s ")
                .define('a', ItemRegistry.AMBER.get())
                .define('#', ItemRegistry.HOGLIN_HIDE.get())
                .define('s', Items.STICK)
                .unlockedBy("everburning_heart", has(ItemRegistry.EVENBURNING_HEART.get()))
                .save(consumer);

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ItemRegistry.EVENBURNING_HEART.get()),
                Ingredient.of(ItemRegistry.NETHERITE_MACE.get()),
                Ingredient.of(ItemRegistry.AMBER.get()),
                RecipeCategory.COMBAT,
                ItemRegistry.SYMBOL_OF_SUN.get())
                .unlocks("everburning_heart", has(ItemRegistry.EVENBURNING_HEART.get()))
                .save(consumer, "pyromancer:symbol_of_sun");

    }
}
