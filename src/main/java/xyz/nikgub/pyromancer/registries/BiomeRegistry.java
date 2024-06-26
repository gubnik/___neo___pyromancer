package xyz.nikgub.pyromancer.registries;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.List;

public class BiomeRegistry {

    private static final List<ResourceKey<Biome>> BIOMES = Lists.newArrayList();

    public static final ResourceKey<Biome> FLAMING_GROVE = register("flaming_grove");

    public static List<ResourceKey<Biome>> getBiomes()
    {
        return ImmutableList.copyOf(BIOMES);
    }

    private static ResourceKey<Biome> register(String name)
    {
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, new ResourceLocation(PyromancerMod.MOD_ID, name));
        BIOMES.add(key);
        return key;
    }
}
