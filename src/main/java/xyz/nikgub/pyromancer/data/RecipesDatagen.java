package xyz.nikgub.pyromancer.data;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.common.registries.ItemRegistry;

import java.util.function.Consumer;

public class RecipesDatagen extends RecipeProvider {
    public RecipesDatagen(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.BLAZING_JOURNAL.get())
                .pattern("bp ")
                .define('b', Ingredient.of(Items.BOOK))
                .define('p', Ingredient.of(Items.BLAZE_POWDER))
                .unlockedBy("nothing", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_POWDER))
                .save(consumer, "blazing_journal");
    }
}
