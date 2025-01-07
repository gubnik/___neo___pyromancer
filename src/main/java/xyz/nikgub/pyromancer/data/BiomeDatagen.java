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
package xyz.nikgub.pyromancer.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.BiomeRegistry;
import xyz.nikgub.pyromancer.registry.BlockRegistry;
import xyz.nikgub.pyromancer.registry.EntityTypeRegistry;

import java.util.function.Consumer;

public class BiomeDatagen
{

    public static void setupTerrablender ()
    {
        Regions.register(new FlamingGroveNetherRegion(PyromancerConfig.flamingGroveRate));
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, PyromancerMod.MOD_ID, SurfaceRule.nether());
    }

    public static void bootstrap (BootstapContext<Biome> biomeBootstapContext)
    {
        HolderGetter<ConfiguredWorldCarver<?>> carverGetter = biomeBootstapContext.lookup(Registries.CONFIGURED_CARVER);
        HolderGetter<PlacedFeature> placedFeatureGetter = biomeBootstapContext.lookup(Registries.PLACED_FEATURE);

        register(biomeBootstapContext, BiomeRegistry.FLAMING_GROVE, flamingGrove(placedFeatureGetter, carverGetter));
        register(biomeBootstapContext, BiomeRegistry.PERMAFROST_CAVERNS, permafrostCaverns(placedFeatureGetter, carverGetter));
    }

    private static void register (BootstapContext<Biome> context, ResourceKey<Biome> key, Biome biome)
    {
        context.register(key, biome);
    }

    public static Biome flamingGrove (HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers)
    {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.PYRACORN.get(), 95, 1, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.SCORCH.get(), 75, 1, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.PYROENT.get(), 85, 2, 6));
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        biomeBuilder.addCarver(GenerationStep.Carving.AIR, ConfiguredCarverDatagen.FLAMING_GROVE_CARVER);
        BiomeDefaultFeatures.addNetherDefaultOres(biomeBuilder);
        biomeBuilder
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatureDatagen.PYROWOOD_NETHER)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatureDatagen.FLAMING_GROVE_VEGETATION)
            .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, PlacedFeatureDatagen.SIZZLING_VINE)
            .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacedFeatureDatagen.AMBER_DEPOSIT);
        return new Biome.BiomeBuilder()
            .hasPrecipitation(false).temperature(2f).downfall(0f)
            .specialEffects((new BiomeSpecialEffects.Builder())
                .waterColor(4159204).waterFogColor(329011)
                .foliageColorOverride(-6649233).grassColorOverride(-6649233)
                .skyColor(-12840192).fogColor(-12840192)
                .ambientParticle(new AmbientParticleSettings(ParticleTypes.FLAME, 0.01f))
                .build())
            .mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build())
            .build();
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
        biomeGenerationBuilder.addCarver(GenerationStep.Carving.AIR, ConfiguredCarverDatagen.WIDER_CAVE);
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

    public static class FlamingGroveNetherRegion extends Region
    {

        public static final ResourceLocation LOCATION = new ResourceLocation(PyromancerMod.MOD_ID, "nether_common");

        public FlamingGroveNetherRegion (int weight)
        {
            super(LOCATION, RegionType.NETHER, weight);
        }

        @Override
        public void addBiomes (Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper)
        {
            this.addBiome(mapper,
                Climate.Parameter.point(0.0F),
                Climate.Parameter.point(0.0F),
                Climate.Parameter.point(0.0F),
                Climate.Parameter.point(0.0F),
                Climate.Parameter.point(0.0F),
                Climate.Parameter.point(0.0F), 0.0F, BiomeRegistry.FLAMING_GROVE);
        }
    }

    public static class SurfaceRule
    {
        private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
        private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
        private static final SurfaceRules.RuleSource PYROMOSSED_NETHERRACK = makeStateRule(BlockRegistry.PYROMOSSED_NETHERRACK.get());

        public static SurfaceRules.RuleSource nether ()
        {
            SurfaceRules.ConditionSource isAbove32 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(32), 0);
            return SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                    SurfaceRules.isBiome(BiomeRegistry.FLAMING_GROVE),
                    SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK),
                        SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK),
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                            SurfaceRules.ifTrue(isAbove32, PYROMOSSED_NETHERRACK)),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, NETHERRACK),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, NETHERRACK)
                    )
                )
            );
        }

        private static SurfaceRules.RuleSource makeStateRule (Block block)
        {
            return SurfaceRules.state(block.defaultBlockState());
        }
    }
}
