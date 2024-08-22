package xyz.nikgub.pyromancer.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registries.BiomeRegistry;
import xyz.nikgub.pyromancer.registries.EntityTypeRegistry;
import xyz.nikgub.pyromancer.registries.NetherBiomeRegistry;

public class BiomeDatagen
{
    public static final ResourceKey<Biome> PERMAFROST_CAVERNS = register("permafrost_caverns");

    public static void bootstrap (BootstapContext<Biome> biomeBootstapContext)
    {
        HolderGetter<ConfiguredWorldCarver<?>> carverGetter = biomeBootstapContext.lookup(Registries.CONFIGURED_CARVER);
        HolderGetter<PlacedFeature> placedFeatureGetter = biomeBootstapContext.lookup(Registries.PLACED_FEATURE);

        register(biomeBootstapContext, BiomeRegistry.FLAMING_GROVE, NetherBiomeRegistry.FLAMING_GROVE(placedFeatureGetter, carverGetter));
        registerBiome(biomeBootstapContext, PERMAFROST_CAVERNS, permafrostCaverns(placedFeatureGetter, carverGetter));
    }

    private static void register (BootstapContext<Biome> context, ResourceKey<Biome> key, Biome biome)
    {
        context.register(key, biome);
    }

    public static Biome permafrostCaverns (HolderGetter<PlacedFeature> pPlacedFeatures, HolderGetter<ConfiguredWorldCarver<?>> pWorldCarvers)
    {
        MobSpawnSettings.Builder mobSpawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.caveSpawns(mobSpawnBuilder);
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 75, 4, 4));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 95, 4, 4));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 10, 1, 1));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 5, 1, 1));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.FROSTCOPPER_GOLEM.get(), 50, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 100, 2, 4));
        BiomeGenerationSettings.Builder biomeGenerationBuilder = new BiomeGenerationSettings.Builder(pPlacedFeatures, pWorldCarvers);
        biomeGenerationBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        biomeGenerationBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        biomeGenerationBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
        BiomeDefaultFeatures.addDefaultCrystalFormations(biomeGenerationBuilder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(biomeGenerationBuilder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenerationBuilder);
        BiomeDefaultFeatures.addSurfaceFreezing(biomeGenerationBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeGenerationBuilder);
        biomeGenerationBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, PlacedFeatureDatagen.RIMECELL_UNDERGROUND);
        biomeGenerationBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, PlacedFeatureDatagen.ICE_PILLARS);
        biomeGenerationBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, PlacedFeatureDatagen.BLUE_ICE_CHUNK);
        biomeGenerationBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, PlacedFeatureDatagen.SNOW_LAYER);
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(-0.1f)
                .downfall(0.5f)
                .temperatureAdjustment(Biome.TemperatureModifier.FROZEN)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(calculateSkyColor(-1f))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.SNOWFLAKE, 0.0025f)).build())
                .mobSpawnSettings(mobSpawnBuilder.build())
                .generationSettings(biomeGenerationBuilder.build())
                .build();
    }

    protected static int calculateSkyColor (float pTemperature)
    {
        float $$1 = pTemperature / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

    private static ResourceKey<Biome> register (String name)
    {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(PyromancerMod.MOD_ID, name));
    }

    private static void registerBiome (BootstapContext<Biome> context, ResourceKey<Biome> key, Biome biome)
    {
        context.register(key, biome);
    }
}
