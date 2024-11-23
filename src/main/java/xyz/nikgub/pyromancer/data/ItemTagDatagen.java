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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.item.IPyromancyItem;
import xyz.nikgub.pyromancer.common.item.MaceItem;
import xyz.nikgub.pyromancer.registry.ItemRegistry;
import xyz.nikgub.pyromancer.registry.VaporizerAmmoRegistry;

import java.util.concurrent.CompletableFuture;

public class ItemTagDatagen extends ItemTagsProvider
{
    public static final TagKey<Item> MACE = create("mace");
    public static final TagKey<Item> FROST_WEAPON = create("frost_weapon");
    public static final TagKey<Item> PYROMANCY = create("pyromancy");
    public static final TagKey<Item> DYNAMIC_WEAPON = create("dynamic_weapon");
    public static final TagKey<Item> FLAMMENKLINGE_PLUNGE_COMPATIBLE = create("flammenklinge_plunge_compatible");
    public static final TagKey<Item> VAPORIZER_AMMO = create("vaporizer_ammo");

    public ItemTagDatagen (PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, provider, blockProvider, PyromancerMod.MOD_ID, existingFileHelper);
    }

    private static TagKey<Item> create (String s)
    {
        return TagKey.create(Registries.ITEM, new ResourceLocation(PyromancerMod.MOD_ID, s));
    }

    @Override
    protected void addTags (HolderLookup.@NotNull Provider p_256380_)
    {
        for (Item item : ForgeRegistries.ITEMS.getValues().stream().toList())
        {
            if (item instanceof IPyromancyItem)
                tag(PYROMANCY).add(item);
            if (item instanceof MaceItem)
                tag(MACE).add(item);
            if (VaporizerAmmoRegistry.isAmmo(item))
                tag(VAPORIZER_AMMO).add(item);
        }
        tag(FROST_WEAPON)
                .add(
                        ItemRegistry.HOARFROST_GREATSWORD.get(),
                        ItemRegistry.SPEAR_OF_MOROZ.get()
                );
        tag(DYNAMIC_WEAPON)
                .addTags(MACE)
                .add(
                        ItemRegistry.FLAMMENKLINGE.get(),
                        ItemRegistry.SPEAR_OF_MOROZ.get()
                );
        tag(FLAMMENKLINGE_PLUNGE_COMPATIBLE)
                .add(
                        ItemRegistry.FLAMMENKLINGE.get(),
                        ItemRegistry.SYMBOL_OF_SUN.get()
                );
    }
}
