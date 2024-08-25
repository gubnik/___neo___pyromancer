package xyz.nikgub.pyromancer.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeEntityTypeTagsProvider;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registries.EntityTypeRegistry;

import java.util.concurrent.CompletableFuture;

public class EntityTagDatagen extends ForgeEntityTypeTagsProvider
{
    public EntityTagDatagen (PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, existingFileHelper);
    }

    public static final TagKey<EntityType<?>> FLAMING_GROVE_NATIVE = create("flaming_grove_native");

    public static TagKey<EntityType<?>> create (String tagName)
    {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(PyromancerMod.MOD_ID, tagName));
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider lookupProvider)
    {
        tag(FLAMING_GROVE_NATIVE)
                .add(EntityTypeRegistry.SCORCH.get(), EntityTypeRegistry.PYRACORN.get(), EntityTypeRegistry.UNBURNED.get());
    }
}
