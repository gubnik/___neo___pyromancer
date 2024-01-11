package net.nikgub.pyromancer.data;

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
import net.nikgub.pyromancer.PyromancerMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistriesDataGeneration extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, DamageTypeDatagen::generate);
    private RegistriesDataGeneration(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", PyromancerMod.MOD_ID));
    }

    public static void addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        generator.addProvider(isServer, new RegistriesDataGeneration(output, provider));
        List<ForgeAdvancementProvider.AdvancementGenerator> list = new ArrayList<>();
        list.add(new AdvancementDatagen.PyromancerAdvancements());
        generator.addProvider(true, new AdvancementDatagen(output, provider, helper, list));
        generator.addProvider(true, new RecipesDatagen(output));
        generator.addProvider(true, new BlockTagDatagen(output, provider, PyromancerMod.MOD_ID, helper));
        generator.addProvider(isServer, new DamageTypeDatagen(output, provider.thenApply(RegistriesDataGeneration::append), helper));
    }
    private static HolderLookup.Provider append(HolderLookup.Provider original) {
        return RegistriesDataGeneration.BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}