package xyz.nikgub.pyromancer.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.items.IPyromancyItem;
import xyz.nikgub.pyromancer.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ItemTagsDatagen extends ItemTagsProvider {
    public ItemTagsDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockProvider, PyromancerMod.MOD_ID, existingFileHelper);
    }

    private static TagKey<Item> create(String s) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(s));
    }

    public static TagKey<Item> PYROMANCY = create("pyromancy");

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        for (Item item :ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).toList())
        {
            if (item instanceof IPyromancyItem)
                tag(PYROMANCY).add(item);
        }

    }
}
