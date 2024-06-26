package xyz.nikgub.pyromancer.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.nikgub.pyromancer.registries.BiomeRegistry;
import xyz.nikgub.pyromancer.registries.NetherBiomeRegistry;

public class BiomesDatagen {

    public static void bootstrap(BootstapContext<Biome> biomeBootstapContext)
    {
        HolderGetter<ConfiguredWorldCarver<?>> carverGetter = biomeBootstapContext.lookup(Registries.CONFIGURED_CARVER);
        HolderGetter<PlacedFeature> placedFeatureGetter = biomeBootstapContext.lookup(Registries.PLACED_FEATURE);

        register(biomeBootstapContext, BiomeRegistry.FLAMING_GROVE, NetherBiomeRegistry.FLAMING_GROVE(placedFeatureGetter, carverGetter));
    }

    private static void register(BootstapContext<Biome> context, ResourceKey<Biome> key, Biome biome)
    {
        context.register(key, biome);
    }
}
