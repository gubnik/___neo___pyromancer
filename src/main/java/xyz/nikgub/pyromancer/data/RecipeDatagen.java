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

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.BlockRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RecipeDatagen extends RecipeProvider
{
    public RecipeDatagen (PackOutput packOutput)
    {
        super(packOutput);
    }

    protected static void nineBlockStorageRecipes (@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull RecipeCategory pUnpackedCategory, ItemLike
        pUnpacked, @NotNull RecipeCategory pPackedCategory, ItemLike pPacked)
    {
        nineBlockStorageRecipes(pFinishedRecipeConsumer, pUnpackedCategory, pUnpacked, pPackedCategory, pPacked, getSimpleRecipeName(pPacked), null, getSimpleRecipeName(pUnpacked), null);
    }

    protected static void nineBlockStorageRecipes (@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull RecipeCategory pUnpackedCategory, ItemLike pUnpacked, @NotNull RecipeCategory pPackedCategory, ItemLike pPacked, String pPackedName, @Nullable String pPackedGroup, String pUnpackedName, @Nullable String pUnpackedGroup)
    {
        ShapelessRecipeBuilder.shapeless(pUnpackedCategory, pUnpacked, 9).requires(pPacked).group(pUnpackedGroup).unlockedBy(getHasName(pPacked), has(pPacked)).save(pFinishedRecipeConsumer, new ResourceLocation(PyromancerMod.MOD_ID, pUnpackedName));
        ShapedRecipeBuilder.shaped(pPackedCategory, pPacked).define('#', pUnpacked).pattern("###").pattern("###").pattern("###").group(pPackedGroup).unlockedBy(getHasName(pUnpacked), has(pUnpacked)).save(pFinishedRecipeConsumer, new ResourceLocation(PyromancerMod.MOD_ID, pPackedName));

    }

    @Override
    protected void buildRecipes (@NotNull Consumer<FinishedRecipe> consumer)
    {
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

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.PYROMANCER_HELMET.get())
            .pattern("###")
            .pattern("#G#")
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .define('G', ItemRegistry.AMBER.get())
            .unlockedBy("hoglin_hide", has(Items.LEATHER))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.PYROMANCER_CHESTPLATE.get())
            .pattern("#G#")
            .pattern("###")
            .pattern("###")
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .define('G', ItemRegistry.AMBER.get())
            .unlockedBy("hoglin_hide", has(Items.LEATHER))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.PYROMANCER_LEGGINGS.get())
            .pattern("###")
            .pattern("#G#")
            .pattern("# #")
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .define('G', ItemRegistry.AMBER.get())
            .unlockedBy("hoglin_hide", has(Items.LEATHER))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.PYROMANCER_BOOTS.get())
            .pattern("# #")
            .pattern("#G#")
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .define('G', ItemRegistry.AMBER.get())
            .unlockedBy("hoglin_hide", has(Items.LEATHER))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SIZZLING_HAND.get())
            .pattern("#a#")
            .pattern("#H#")
            .pattern("#a#")
            .define('a', ItemRegistry.AMBER.get())
            .define('H', ItemRegistry.MEMORY_OF_FIRE.get())
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .unlockedBy("memory_of_fire", has(ItemRegistry.MEMORY_OF_FIRE.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.COURT_OF_EMBERS.get())
            .pattern("aHa")
            .pattern("asa")
            .pattern(" s ")
            .define('a', ItemRegistry.AMBER.get())
            .define('H', ItemRegistry.MEMORY_OF_FIRE.get())
            .define('s', Items.STICK)
            .unlockedBy("memory_of_fire", has(ItemRegistry.MEMORY_OF_FIRE.get()))
            .save(consumer);

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ItemRegistry.MEMORY_OF_FIRE.get()),
                Ingredient.of(ItemRegistry.NETHERITE_MACE.get()),
                Ingredient.of(ItemRegistry.AMBER.get()),
                RecipeCategory.COMBAT,
                ItemRegistry.SYMBOL_OF_SUN.get())
            .unlocks("memory_of_fire", has(ItemRegistry.MEMORY_OF_FIRE.get()))
            .save(consumer, "pyromancer:symbol_of_sun");

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ItemRegistry.MEMORY_OF_FIRE.get()),
                Ingredient.of(Items.NETHERITE_SWORD),
                Ingredient.of(ItemRegistry.AMBER.get()),
                RecipeCategory.COMBAT,
                ItemRegistry.FLAMMENKLINGE.get())
            .unlocks("memory_of_fire", has(ItemRegistry.MEMORY_OF_FIRE.get()))
            .save(consumer, "pyromancer:flammenklinge");

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ItemRegistry.MEMORY_OF_FIRE.get()),
                Ingredient.of(ItemRegistry.PYROMANCER_HELMET.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.COMBAT,
                ItemRegistry.TAINTED_MONARCH_HELMET.get())
            .unlocks("memory_of_fire", has(ItemRegistry.MEMORY_OF_FIRE.get()))
            .save(consumer, "pyromancer:tainted_monarch_helmet");

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ItemRegistry.MEMORY_OF_FIRE.get()),
                Ingredient.of(ItemRegistry.PYROMANCER_CHESTPLATE.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.COMBAT,
                ItemRegistry.TAINTED_MONARCH_CHESTPLATE.get())
            .unlocks("memory_of_fire", has(ItemRegistry.MEMORY_OF_FIRE.get()))
            .save(consumer, "pyromancer:tainted_monarch_chestplate");

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ItemRegistry.MEMORY_OF_FIRE.get()),
                Ingredient.of(ItemRegistry.PYROMANCER_LEGGINGS.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.COMBAT,
                ItemRegistry.TAINTED_MONARCH_LEGGINGS.get())
            .unlocks("memory_of_fire", has(ItemRegistry.MEMORY_OF_FIRE.get()))
            .save(consumer, "pyromancer:tainted_monarch_leggings");

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ItemRegistry.MEMORY_OF_FIRE.get()),
                Ingredient.of(ItemRegistry.PYROMANCER_BOOTS.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.COMBAT,
                ItemRegistry.TAINTED_MONARCH_BOOTS.get())
            .unlocks("memory_of_fire", has(ItemRegistry.MEMORY_OF_FIRE.get()))
            .save(consumer, "pyromancer:tainted_monarch_boots");

        RecipeDatagen.nineBlockStorageRecipes(consumer, RecipeCategory.MISC, ItemRegistry.RIMEBLOOD.get(), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.RIMEBLOOD_BLOCK.get());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.RIMEBRASS_INGOT.get())
            .requires(ItemRegistry.RIMEBLOOD.get(), 4).requires(Items.COPPER_INGOT, 2).requires(Items.IRON_INGOT, 2)
            .unlockedBy(getHasName(ItemRegistry.RIMEBLOOD.get()), has(ItemRegistry.RIMEBLOOD.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.HOARFROST_GREATSWORD.get())
            .pattern(" # ")
            .pattern("C#C")
            .pattern(" C ")
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .define('C', ItemRegistry.RIMEBRASS_INGOT.get())
            .unlockedBy(getHasName(ItemRegistry.RIMEBRASS_INGOT.get()), has(ItemRegistry.RIMEBRASS_INGOT.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SPEAR_OF_MOROZ.get())
            .pattern(" # ")
            .pattern(" # ")
            .pattern("CCC")
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .define('C', ItemRegistry.RIMEBRASS_INGOT.get())
            .unlockedBy(getHasName(ItemRegistry.RIMEBRASS_INGOT.get()), has(ItemRegistry.RIMEBRASS_INGOT.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.ZWEIHANDER.get())
            .pattern(" I ")
            .pattern("III")
            .pattern("#S#")
            .define('I', Items.IRON_INGOT)
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .define('S', Items.STICK)
            .unlockedBy(getHasName(ItemRegistry.ANCIENT_PLATING.get()), has(ItemRegistry.ANCIENT_PLATING.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.MUSKET.get())
            .pattern("III")
            .pattern("#I ")
            .pattern("#S ")
            .define('I', Items.IRON_INGOT)
            .define('#', ItemRegistry.ANCIENT_PLATING.get())
            .define('S', Items.STICK)
            .unlockedBy(getHasName(ItemRegistry.ANCIENT_PLATING.get()), has(ItemRegistry.ANCIENT_PLATING.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.MEMORY_OF_FIRE.get())
            .pattern("FAF")
            .pattern("SFS")
            .pattern(" A ")
            .define('A', ItemRegistry.AMBER.get())
            .define('F', ItemRegistry.AMBER.get())
            .define('S', ItemRegistry.AMBER.get())
            .unlockedBy(getHasName(ItemRegistry.ANCIENT_PLATING.get()), has(ItemRegistry.ANCIENT_PLATING.get()))
            .save(consumer);
    }
}
