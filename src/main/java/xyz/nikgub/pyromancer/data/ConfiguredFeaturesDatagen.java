package xyz.nikgub.pyromancer.data;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.blocks.SizzlingVineBlock;
import xyz.nikgub.pyromancer.common.worldgen.NetherPyrowoodTrunkPlacer;
import xyz.nikgub.pyromancer.registries.BlockRegistry;

import java.util.List;

public class ConfiguredFeaturesDatagen {

    public static final ResourceKey<ConfiguredFeature<?, ?>> PYROWOOD_NETHER = createKey("pyrowood_nether");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLAMING_GROVE_VEGETATION = createKey("flaming_grove_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SIZZLING_VINE = createKey("sizzling_vine");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context)
    {
        HolderGetter<PlacedFeature> placedFeatureGetter = context.lookup(Registries.PLACED_FEATURE);

        context.register(PYROWOOD_NETHER, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.PYROWOOD_LOG.get()), new NetherPyrowoodTrunkPlacer(7, 3, 1),
                BlockStateProvider.simple(BlockRegistry.PYROWOOD_LEAVES.get()),
                new RandomSpreadFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(3), 24),
                new TwoLayersFeatureSize(1, 0, 0)).dirt(BlockStateProvider.simple(Blocks.NETHERRACK)).build()));

        context.register(FLAMING_GROVE_VEGETATION, new ConfiguredFeature<>(Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(BlockRegistry.PYROMOSS_SPROUTS.get().defaultBlockState(), 50)
                        .add(BlockRegistry.FIREBRIAR.get().defaultBlockState(), 25)
                        .add(BlockRegistry.BLAZING_POPPY.get().defaultBlockState(), 13)
                        .add(BlockRegistry.NETHER_LILY.get().defaultBlockState(), 11))
                , 8, 4)));

        context.register(SIZZLING_VINE, new ConfiguredFeature<>(Feature.BLOCK_COLUMN, new BlockColumnConfiguration(
                List.of(BlockColumnConfiguration.layer(
                                new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
                                        .add(UniformInt.of(0, 19), 2)
                                        .add(UniformInt.of(0, 2), 3)
                                        .add(UniformInt.of(0, 6), 10).build()),
                                // SIZZLING_VINE_BODY_PROVIDER
                                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                        .add(BlockRegistry.SIZZLING_VINE.get().defaultBlockState(), 4)
                                        .add(BlockRegistry.SIZZLING_VINE.get().defaultBlockState().setValue(SizzlingVineBlock.THICK, Boolean.TRUE), 1))),
                        BlockColumnConfiguration.layer(ConstantInt.of(1),
                                // SIZZLING_VINE_HEAD_PROVIDER
                                new RandomizedIntStateProvider(
                                        new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                .add(BlockRegistry.SIZZLING_VINE.get().defaultBlockState(), 4)
                                                .add(BlockRegistry.SIZZLING_VINE.get().defaultBlockState().setValue(SizzlingVineBlock.THICK, Boolean.TRUE), 1)), SizzlingVineBlock.AGE, UniformInt.of(23, 25))
                        )), Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true)));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey, F feature, FC configuration)
    {
        context.register(configuredFeatureKey, new ConfiguredFeature<>(feature, configuration));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name)
    {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(PyromancerMod.MOD_ID, name));
    }
}
