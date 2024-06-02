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
import xyz.nikgub.pyromancer.common.registries.vanila.ItemRegistry;

import java.util.function.Consumer;

public class RecipesDatagen extends RecipeProvider {
    public RecipesDatagen(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ItemRegistry.BLAZING_JOURNAL.get())
                .pattern(" s ")
                .pattern(" n ")
                .pattern(" i ")
                .define('s', Ingredient.of(Items.FIRE_CHARGE))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("nothing", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_POWDER))
                .save(consumer, "blazing_journal");
    }
}
