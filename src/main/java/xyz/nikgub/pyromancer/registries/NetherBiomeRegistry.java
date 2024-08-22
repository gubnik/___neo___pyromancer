package xyz.nikgub.pyromancer.registries;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import xyz.nikgub.pyromancer.data.ConfiguredCarverDatagen;
import xyz.nikgub.pyromancer.data.PlacedFeatureDatagen;

import java.util.function.Consumer;

public class NetherBiomeRegistry
{
    public static void setupTerrablender ()
    {
        Regions.register(new FlamingGroveNetherRegion(PyromancerConfig.flamingGroveRate));
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, PyromancerMod.MOD_ID, SurfaceRule.nether());
    }

    public static Biome FLAMING_GROVE (HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers)
    {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
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
