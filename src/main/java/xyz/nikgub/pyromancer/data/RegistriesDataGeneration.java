package xyz.nikgub.pyromancer.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistriesDataGeneration extends DatapackBuiltinEntriesProvider
{

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, DamageTypeDatagen::generate)
            .add(Registries.BIOME, BiomeDatagen::bootstrap)
            .add(Registries.CONFIGURED_CARVER, ConfiguredCarverDatagen::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureDatagen::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatureDatagen::bootstrap);

    private RegistriesDataGeneration (PackOutput output, CompletableFuture<HolderLookup.Provider> provider)
    {
        super(output, provider, BUILDER, Set.of("minecraft", PyromancerMod.MOD_ID));
    }

    public static void addProviders (boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper)
    {
        generator.addProvider(isServer, new RegistriesDataGeneration(output, provider));
        List<ForgeAdvancementProvider.AdvancementGenerator> list = List.of(
                new AdvancementDatagen.PyromancerAdvancements()
        );
        generator.addProvider(isServer, new AdvancementDatagen(output, provider, helper, list));
        generator.addProvider(isServer, new RecipeDatagen(output));
        var blockTags = generator.addProvider(isServer, new BlockTagDatagen(output, provider, PyromancerMod.MOD_ID, helper));
        generator.addProvider(isServer, new DamageTypeDatagen(output, provider.thenApply(RegistriesDataGeneration::append), helper));
        generator.addProvider(isServer, new ItemModelDatagen(output, helper));
        var blockModelsDatagen = generator.addProvider(isServer, new BlockModelDatagen(output, helper));
        generator.addProvider(isServer, new BlockStateDatagen(output, helper, blockModelsDatagen));
        generator.addProvider(isServer, new LootTableDatagen(output));
        generator.addProvider(isServer, new ItemTagDatagen(output, provider, blockTags.contentsGetter(), helper));
        generator.addProvider(isServer, new PoiTypesDatagen(output, provider, helper));
        generator.addProvider(isServer, new SoundsDefinitionsDatagen(output, helper));
        generator.addProvider(isServer, new EntityTagDatagen(output, provider, helper));
    }

    private static HolderLookup.Provider append (HolderLookup.Provider original)
    {
        return RegistriesDataGeneration.BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}