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

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

public class ItemModelDatagen extends ItemModelProvider
{
    public static final List<RegistryObject<? extends Item>> TOOLS = List.of(
        ItemRegistry.COURT_OF_EMBERS
    );

    public static final List<RegistryObject<? extends Item>> CUSTOM = new ArrayList<>(List.of(
        ItemRegistry.SIZZLING_HAND,
        ItemRegistry.HOARFROST_GREATSWORD,
        ItemRegistry.SPEAR_OF_MOROZ,
        ItemRegistry.ZWEIHANDER,
        ItemRegistry.MUSKET,
        ItemRegistry.COMPENDIUM_OF_FLAME
    ));

    private static final ItemDisplayContext THIRDPERSON = ItemDisplayContext.create("thirdperson", ResourceLocation.tryParse("thirdperson"), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);

    public ItemModelDatagen (PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, PyromancerMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels ()
    {
        List<Item> SPAWN_EGGS = ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof ForgeSpawnEggItem).toList();
        List<BlockItem> BLOCK_ITEM = ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem blockItem && !(blockItem.getBlock() instanceof DoorBlock)).map(item -> (BlockItem) item).toList();
        List<Item> TOOLS = new ArrayList<>(ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof TieredItem).toList());
        TOOLS.addAll(ItemModelDatagen.TOOLS.stream().map(RegistryObject::get).toList());
        List<Item> CUSTOM = new ArrayList<>(ItemModelDatagen.CUSTOM.stream().map(RegistryObject::get).toList());
        List<Item> ALL_ELSE = ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> !SPAWN_EGGS.contains(item) && !CUSTOM.contains(item) && !BLOCK_ITEM.contains(item) && !TOOLS.contains(item)).toList();
        for (Item item : SPAWN_EGGS)
            this.spawnEggItem(item);
        for (Item item : TOOLS)
        {
            ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(item);
            if (resourceLocation != null) this.tieredItem(resourceLocation);
        }
        for (BlockItem item : BLOCK_ITEM)
        {
            if (item.getBlock() instanceof IPlantable || item.getBlock() instanceof GrowingPlantBlock)
                getBuilder(item.toString())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "block/" + item.getDescriptionId().replace("block.pyromancer.", "")));
            else
                this.blockItem(item);
        }
        for (Item item : ALL_ELSE)
            this.basicItem(item);
    }

    public void spawnEggItem (Item item)
    {
        getBuilder(item.toString())
            .parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    public void tieredItem (ResourceLocation item)
    {
        getBuilder(item.toString())
            .parent(new ModelFile.UncheckedModelFile("item/handheld"))
            .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }

    public void blockItem (BlockItem item)
    {
        String mod = "";
        if (item.getBlock() instanceof FenceBlock) mod = "_inventory";
        else if (item.getBlock() instanceof TrapDoorBlock) mod = "_bottom";
        getBuilder(item.toString())
            .parent(new ModelFile.UncheckedModelFile("pyromancer:block/" + item + mod))
            .transforms()
            .transform(THIRDPERSON)
            .rotation(10, -45, 170)
            .scale(0.375f, 0.375f, 0.375f)
            .translation(0, 1.5f, -2.75f)
            .end()
            .end();
    }
}
