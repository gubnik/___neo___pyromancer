package xyz.nikgub.pyromancer.data;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registries.BlockRegistry;

import java.util.List;

public class PlacedFeatureDatagen
{
    public static final ResourceKey<PlacedFeature> PYROWOOD_NETHER = createKey("pyrowood_nether");
    public static final ResourceKey<PlacedFeature> FLAMING_GROVE_VEGETATION = createKey("flaming_grove_vegetation");
    public static final ResourceKey<PlacedFeature> SIZZLING_VINE = createKey("sizzling_vine");
    public static final ResourceKey<PlacedFeature> AMBER_DEPOSIT = createKey("amber_deposit");

    public static final ResourceKey<PlacedFeature> RIMECELL_UNDERGROUND = createKey("rimecell_underground");
    public static final ResourceKey<PlacedFeature> ICE_PILLARS = createKey("ice_pillars");
    public static final ResourceKey<PlacedFeature> BLUE_ICE_CHUNK = createKey("blue_ice_chunk");
    public static final ResourceKey<PlacedFeature> SNOW_LAYER = createKey("snow_layer");

    public static void bootstrap (BootstapContext<PlacedFeature> context)
    {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureGetter = context.lookup(Registries.CONFIGURED_FEATURE);

        final Holder<ConfiguredFeature<?, ?>> PYROWOOD_NETHER_HOLDER = configuredFeatureGetter.getOrThrow(ConfiguredFeatureDatagen.PYROWOOD_NETHER);
        final Holder<ConfiguredFeature<?, ?>> FLAMING_GROVE_VEGETATION_HOLDER = configuredFeatureGetter.getOrThrow(ConfiguredFeatureDatagen.FLAMING_GROVE_VEGETATION);
        final Holder<ConfiguredFeature<?, ?>> SIZZLING_VINE_HOLDER = configuredFeatureGetter.getOrThrow(ConfiguredFeatureDatagen.SIZZLING_VINE);
        final Holder<ConfiguredFeature<?, ?>> AMBER_DEPOSIT_HOLDER = configuredFeatureGetter.getOrThrow(ConfiguredFeatureDatagen.AMBER_DEPOSIT);
        final Holder<ConfiguredFeature<?, ?>> RIMECELL_UNDERGROUND_HOLDER = configuredFeatureGetter.getOrThrow(ConfiguredFeatureDatagen.RIMECELL_UNDERGROUND);
        final Holder<ConfiguredFeature<?, ?>> ICE_PILLARS_HOLDER = configuredFeatureGetter.getOrThrow(ConfiguredFeatureDatagen.ICE_PILLARS);
        final Holder<ConfiguredFeature<?, ?>> BLUE_ICE_CHUNK_HOLDER = configuredFeatureGetter.getOrThrow(ConfiguredFeatureDatagen.BLUE_ICE_CHUNK);
        final Holder<ConfiguredFeature<?, ?>> SNOW_LAYER_HOLDER = configuredFeatureGetter.getOrThrow(ConfiguredFeatureDatagen.SNOW_LAYER);

        PlacementUtils.register(context, PYROWOOD_NETHER, PYROWOOD_NETHER_HOLDER, List.of(CountOnEveryLayerPlacement.of(10), PlacementUtils.filteredByBlockSurvival(BlockRegistry.PYROMOSS_SPROUTS.get())));

        PlacementUtils.register(context, FLAMING_GROVE_VEGETATION, FLAMING_GROVE_VEGETATION_HOLDER, List.of(CountOnEveryLayerPlacement.of(16), PlacementUtils.filteredByBlockSurvival(BlockRegistry.PYROMOSS_SPROUTS.get())));

        PlacementUtils.register(context, SIZZLING_VINE, SIZZLING_VINE_HOLDER, List.of(CountPlacement.of(255), InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()));

        PlacementUtils.register(context, AMBER_DEPOSIT, AMBER_DEPOSIT_HOLDER, List.of(CountPlacement.of(13), InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()));

        PlacementUtils.register(context, RIMECELL_UNDERGROUND, RIMECELL_UNDERGROUND_HOLDER, List.of(CountPlacement.of(64), InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()));

        PlacementUtils.register(context, ICE_PILLARS, ICE_PILLARS_HOLDER, List.of(CountOnEveryLayerPlacement.of(40), BiomeFilter.biome()));

        PlacementUtils.register(context, BLUE_ICE_CHUNK, BLUE_ICE_CHUNK_HOLDER, List.of(CountPlacement.of(13), InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.alwaysTrue(), 8),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()));

        PlacementUtils.register(context, SNOW_LAYER, SNOW_LAYER_HOLDER, List.of(CountOnEveryLayerPlacement.of(64), BiomeFilter.biome()));
    }

    protected static void register (BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> placedFeatureKey, Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... modifiers)
    {
        register(context, placedFeatureKey, configuredFeature, List.of(modifiers));
    }

    protected static void register (BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> placedFeatureKey, Holder<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> modifiers)
    {
        context.register(placedFeatureKey, new PlacedFeature(configuredFeature, modifiers));
    }

    public static ResourceKey<PlacedFeature> createKey (String name)
    {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(PyromancerMod.MOD_ID, name));
    }
}
