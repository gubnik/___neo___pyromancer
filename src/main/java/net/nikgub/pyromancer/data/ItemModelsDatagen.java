package net.nikgub.pyromancer.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.registries.vanila.ItemRegistry;

import java.util.List;

public class ItemModelsDatagen extends ItemModelProvider {
    public ItemModelsDatagen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PyromancerMod.MOD_ID, existingFileHelper);
    }
    @Override
    protected void registerModels() {
        List<Item> SPAWN_EGGS = ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof ForgeSpawnEggItem).toList();
        for (Item item : SPAWN_EGGS)
            this.spawnEggItem(new ResourceLocation(item.toString()));
    }
    public ItemModelBuilder spawnEggItem(ResourceLocation item)
    {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation("minecraft:item/spawn_egg"));
    }
}
