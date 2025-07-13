/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, Nikolay Gubankov (aka nikgub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.block.SizzlingVineBlock;
import xyz.nikgub.pyromancer.common.worldgen.NetherPyrowoodTrunkPlacer;
import xyz.nikgub.pyromancer.registry.BlockRegistry;

import java.util.List;

public class ConfiguredFeatureDatagen
{
    public static final ResourceKey<ConfiguredFeature<?, ?>> PYROWOOD_NETHER = createKey("pyrowood_nether");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLAMING_GROVE_VEGETATION = createKey("flaming_grove_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SIZZLING_VINE = createKey("sizzling_vine");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMBER_DEPOSIT = createKey("amber_deposit");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RIMECELL_UNDERGROUND = createKey("rimecell_underground");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_PILLARS = createKey("ice_pillars");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_ICE_CHUNK = createKey("blue_ice_chunk");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_LAYER = createKey("snow_layer");

    public static void bootstrap (BootstapContext<ConfiguredFeature<?, ?>> context)
    {
        HolderGetter<PlacedFeature> placedFeatureGetter = context.lookup(Registries.PLACED_FEATURE);

        context.register(PYROWOOD_NETHER, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.PYROWOOD_LOG.get()), new NetherPyrowoodTrunkPlacer(9, 3, 1),
            BlockStateProvider.simple(BlockRegistry.PYROWOOD_LEAVES.get()),
            new RandomSpreadFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(3), 24),
            new TwoLayersFeatureSize(1, 0, 0)).dirt(BlockStateProvider.simple(Blocks.NETHERRACK)).build()));

        context.register(FLAMING_GROVE_VEGETATION, new ConfiguredFeature<>(Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(
            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(BlockRegistry.PYROMOSS_SPROUTS.get().defaultBlockState(), 55)
                .add(BlockRegistry.NETHER_LILY.get().defaultBlockState(), 20)
                .add(BlockRegistry.BLAZING_POPPY.get().defaultBlockState(), 11)
                .add(BlockRegistry.FIREBRIAR.get().defaultBlockState(), 4)
                .add(BlockRegistry.PYROWOOD_SAPLING.get().defaultBlockState(), 1))
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

        context.register(AMBER_DEPOSIT, new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(new TagMatchTest(BlockTagDatagen.AMBER_REPLACEABLE),
                BlockRegistry.NATURAL_AMBER.get().defaultBlockState(), 15, 0.05f)));

        context.register(RIMECELL_UNDERGROUND, new ConfiguredFeature<>(Feature.BLOCK_COLUMN, new BlockColumnConfiguration(
            List.of(
                BlockColumnConfiguration.layer
                    (
                        new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
                            .add(UniformInt.of(0, 8), 2)
                            .add(UniformInt.of(0, 4), 3)
                            .add(UniformInt.of(0, 6), 10).build()),
                        BlockStateProvider.simple(BlockRegistry.RIMEVIME.get())
                    ),
                BlockColumnConfiguration.layer
                    (ConstantInt.of(1), BlockStateProvider.simple(BlockRegistry.RIMEBLOOD_CELL.get())
                    )
            ),
            Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true
        )));

        context.register(ICE_PILLARS, new ConfiguredFeature<>(Feature.BLOCK_COLUMN,
            new BlockColumnConfiguration(List.of(
                BlockColumnConfiguration.layer
                    (ConstantInt.of(2), BlockStateProvider.simple(Blocks.BLUE_ICE)
                    ),
                BlockColumnConfiguration.layer
                    (
                        new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
                            .add(UniformInt.of(0, 13), 2)
                            .add(UniformInt.of(0, 4), 3)
                            .add(UniformInt.of(0, 7), 10).build()),
                        BlockStateProvider.simple(Blocks.PACKED_ICE)
                    )
            ),
                Direction.UP, BlockPredicate.noFluid(), true
            )));

        context.register(BLUE_ICE_CHUNK, new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(new TagMatchTest(BlockTagDatagen.BLUE_ICE_CHUNK_REPLACEABLE),
                Blocks.BLUE_ICE.defaultBlockState(), 48, 0.02f)));

        context.register(SNOW_LAYER, new ConfiguredFeature<>(Feature.BLOCK_COLUMN,
            new BlockColumnConfiguration(List.of(
                BlockColumnConfiguration.layer
                    (ConstantInt.of(1), new RandomizedIntStateProvider(BlockStateProvider.simple(Blocks.SNOW), SnowLayerBlock.LAYERS, UniformInt.of(1, 6)))
            ),
                Direction.UP, BlockPredicate.noFluid(), true
            )));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey (String name)
    {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, name));
    }
}
