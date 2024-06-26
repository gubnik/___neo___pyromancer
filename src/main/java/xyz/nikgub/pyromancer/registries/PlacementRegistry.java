package xyz.nikgub.pyromancer.registries;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.List;
import java.util.function.Supplier;

public class PlacementRegistry {

    //public static final DeferredRegister<PlacedFeature> PLACED_FEATURE_REGISTRY = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, PyromancerMod.MOD_ID);
//
    //public static final RegistryObject<PlacedFeature> PYROWOOD_NETHER = register("pyrowood_nether_placement",
    //        ModFeatures.PYROWOOD_NETHER, () ->  List.of(CountOnEveryLayerPlacement.of(10), PlacementUtils.filteredByBlockSurvival(BlockRegistry.PYROMOSS_SPROUTS.get()), BiomeFilter.biome()));
//
    //public static RegistryObject<PlacedFeature> FLAMING_GROVE_VEGETATION = register("flaming_grove_vegetation_placement",
    //        ModFeatures.FLAMING_GROVE_VEGETATION, () -> List.of(CountPlacement.of(255), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome()));
//
    //public static final RegistryObject<PlacedFeature> SIZZLING_VINES = register("cave_vines",
    //        ModFeatures.SIZZLING_VINE, ()-> List.of(CountPlacement.of(255), InSquarePlacement.spread(),
    //                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
    //                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
    //                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
    //                BiomeFilter.biome()));
//
    //public static RegistryObject<PlacedFeature> register(String key, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, Supplier<List<PlacementModifier>> modifiers) {
    //    return PLACED_FEATURE_REGISTRY.register(key, () -> new PlacedFeature(Holder.hackyErase(feature.getHolder().orElseThrow()), List.copyOf(modifiers.get())));
    //}
    //public static RegistryObject<PlacedFeature> register(String key, RegistryObject<? extends ConfiguredFeature<?, ?>> feature) {
    //    return register(key, feature, List::of);
    //}
}
