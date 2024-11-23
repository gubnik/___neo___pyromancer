package xyz.nikgub.pyromancer.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;

public class ParticleRegistry
{
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, PyromancerMod.MOD_ID);
    public static final RegistryObject<SimpleParticleType> BRIMSTONE_FLAME_PARTICLE = PARTICLES.register("brimstone_flame", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> MERCURY_VAPOR = PARTICLES.register("mercury_vapor", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> VAPORIZER_MERCURY = PARTICLES.register("vaporizer_mercury", () -> new SimpleParticleType(false));
}
