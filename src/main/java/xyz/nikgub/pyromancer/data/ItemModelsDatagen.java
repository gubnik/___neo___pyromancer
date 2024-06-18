package xyz.nikgub.pyromancer.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registries.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

public class ItemModelsDatagen extends ItemModelProvider {
    private static final ItemDisplayContext THIRDPERSON = ItemDisplayContext.create("thirdperson", ResourceLocation.tryParse("thirdperson"), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
    public ItemModelsDatagen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PyromancerMod.MOD_ID, existingFileHelper);
    }
    @Override
    protected void registerModels() {
        List<Item> SPAWN_EGGS = ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof ForgeSpawnEggItem).toList();
        List<Item> TOOLS = new ArrayList<>(ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof TieredItem).toList());
        TOOLS.add(ItemRegistry.COURT_OF_EMBERS.get());
        List<Item> CUSTOM = List.of(ItemRegistry.SIZZLING_HAND.get(), ItemRegistry.COMPENDIUM_OF_FLAME.get());
        List<BlockItem> BLOCK_ITEM = ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem blockItem && !(blockItem.getBlock() instanceof DoorBlock)).map(item -> (BlockItem) item).toList();
        List<Item> ALL_ELSE = ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> !SPAWN_EGGS.contains(item) && !CUSTOM.contains(item) && !BLOCK_ITEM.contains((BlockItem) item) && !TOOLS.contains(item)).toList();
        for (Item item : SPAWN_EGGS)
            this.spawnEggItem(item);
        for (Item item : TOOLS) {
            ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(item);
            if(resourceLocation != null) this.tieredItem(resourceLocation);
        }
        for(BlockItem item : BLOCK_ITEM)
        {
            this.blockItem(item);
        }
        for (Item item : ALL_ELSE)
            this.basicItem(item);
    }

    public ItemModelBuilder spawnEggItem(Item item)
    {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation("minecraft:item/spawn_egg"));
    }

    public ItemModelBuilder tieredItem(ResourceLocation item)
    {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }

    public ItemModelBuilder blockItem(BlockItem item)
    {
        String mod = "";
        if(item.getBlock() instanceof FenceBlock) mod = "_inventory";
        else if(item.getBlock() instanceof TrapDoorBlock) mod = "_bottom";
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("pyromancer:block/" + item + mod))
                .transforms()
                .transform(THIRDPERSON)
                .rotation(10, -45, 170)
                .scale(0.375f,0.375f,0.375f)
                .translation(0, 1.5f, -2.75f)
                .end()
                .end();
    }
}
