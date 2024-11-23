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
