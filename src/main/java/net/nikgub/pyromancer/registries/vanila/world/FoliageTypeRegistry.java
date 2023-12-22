package net.nikgub.pyromancer.registries.vanila.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.world.trees.foliage.NetherPyrowoodFoliagePlacer;

public class FoliageTypeRegistry {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_REGISTRY = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, PyromancerMod.MOD_ID);
    public static final RegistryObject<FoliagePlacerType<?>> NETHER_PYROWOOD_FOLIAGE_PLACER = FOLIAGE_PLACER_REGISTRY.register("nether_pyrowood_foliage_placer",
            ()-> new FoliagePlacerType<>(NetherPyrowoodFoliagePlacer.CODEC));
}
