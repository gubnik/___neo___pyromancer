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

public class RegistriesDataGeneration extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, DamageTypeDatagen::generate)
            .add(Registries.BIOME, BiomesDatagen::bootstrap)
            .add(Registries.CONFIGURED_CARVER, ConfiguredCarversDatagen::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeaturesDatagen::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatureDatagen::bootstrap);

    private RegistriesDataGeneration(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", PyromancerMod.MOD_ID));
    }

    public static void addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        generator.addProvider(isServer, new RegistriesDataGeneration(output, provider));
        List<ForgeAdvancementProvider.AdvancementGenerator> list = List.of(
                new AdvancementDatagen.PyromancerAdvancements()
        );
        generator.addProvider(isServer, new AdvancementDatagen(output, provider, helper, list));
        generator.addProvider(isServer, new RecipesDatagen(output));
        generator.addProvider(isServer, new BlockTagDatagen(output, provider, PyromancerMod.MOD_ID, helper));
        generator.addProvider(isServer, new DamageTypeDatagen(output, provider.thenApply(RegistriesDataGeneration::append), helper));
        generator.addProvider(isServer, new ItemModelsDatagen(output, helper));
        generator.addProvider(isServer, new BlockModelsDatagen(output, helper));
        generator.addProvider(isServer, new BlockStateDatagen(output, helper));
        generator.addProvider(isServer, new LootTablesDatagen(output));
    }

    private static HolderLookup.Provider append(HolderLookup.Provider original) {
        return RegistriesDataGeneration.BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}