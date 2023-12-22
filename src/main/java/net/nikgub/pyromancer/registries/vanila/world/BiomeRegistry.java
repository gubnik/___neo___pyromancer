package net.nikgub.pyromancer.registries.vanila.world;

import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.nikgub.pyromancer.PyromancerMod;

public class BiomeRegistry {
    public static DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, PyromancerMod.MOD_ID);
    //public static RegistryObject<Biome> FLAMING_GROVE = BIOMES.register("flaming_grove",
    //        ModNetherBiomes::flaming_grove);
}
