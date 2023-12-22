package net.nikgub.pyromancer.registries.vanila.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.world.trees.trunks.NetherPyrowoodTrunkPlacer;

public class TrunkTypeRegistry {
    public static DeferredRegister<TrunkPlacerType<?>> TRUNK_TYPE_REGISTRY = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, PyromancerMod.MOD_ID);
    public static final RegistryObject<TrunkPlacerType<?>> NETHER_PYROWOOD_TRUNK_PLACER = TRUNK_TYPE_REGISTRY.register("nether_pyrowood_trunk_placer",
            ()-> new TrunkPlacerType<>(NetherPyrowoodTrunkPlacer.CODEC));
}
