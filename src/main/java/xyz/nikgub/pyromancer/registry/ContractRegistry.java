package xyz.nikgub.pyromancer.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.contract.AccursedContractEntry;

import java.util.function.Supplier;

public class ContractRegistry
{
    public static final ResourceKey<Registry<AccursedContractEntry<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(PyromancerMod.MOD_ID, "contracts"));
    public static final DeferredRegister<AccursedContractEntry<?>> CONTRACTS = DeferredRegister.create(REGISTRY_KEY, PyromancerMod.MOD_ID);
    public static final Supplier<IForgeRegistry<AccursedContractEntry<?>>> REGISTRY = CONTRACTS.makeRegistry(() -> new RegistryBuilder<AccursedContractEntry<?>>().disableOverrides());

    public static RegistryObject<AccursedContractEntry<?>> ZOMBIE = CONTRACTS.register("zombie",
            () -> new AccursedContractEntry<>(EntityType.ZOMBIE, 1));
    public static RegistryObject<AccursedContractEntry<?>> SKELETON = CONTRACTS.register("skeleton",
            () -> new AccursedContractEntry<>(EntityType.SKELETON, 5));
    public static RegistryObject<AccursedContractEntry<?>> CREEPER = CONTRACTS.register("creeper",
            () -> new AccursedContractEntry<>(EntityType.CREEPER, 20));
}
