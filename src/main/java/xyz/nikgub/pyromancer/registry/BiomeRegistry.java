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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.List;

public class BiomeRegistry
{
    private static final List<ResourceKey<Biome>> BIOMES = Lists.newArrayList();

    public static final ResourceKey<Biome> FLAMING_GROVE = register("flaming_grove");
    public static final ResourceKey<Biome> PERMAFROST_CAVERNS = register("permafrost_caverns");

    public static List<ResourceKey<Biome>> getBiomes ()
    {
        return ImmutableList.copyOf(BIOMES);
    }

    private static ResourceKey<Biome> register (String name)
    {
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, new ResourceLocation(PyromancerMod.MOD_ID, name));
        BIOMES.add(key);
        return key;
    }
}
