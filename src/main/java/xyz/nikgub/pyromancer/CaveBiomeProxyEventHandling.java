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
package xyz.nikgub.pyromancer;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.nikgub.pyromancer.registry.BiomeRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class CaveBiomeProxyEventHandling
{
    private static final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);

    @SubscribeEvent
    public static void onServerAboutToStart (ServerAboutToStartEvent event)
    {
        MinecraftServer server = event.getServer();
        Registry<DimensionType> dimensionTypeRegistry = server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
        Registry<LevelStem> levelStemTypeRegistry = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
        Registry<Biome> biomeRegistry = server.registryAccess().registryOrThrow(Registries.BIOME);
        for (LevelStem levelStem : levelStemTypeRegistry.stream().toList())
        {
            DimensionType dimensionType = levelStem.type().value();
            if (dimensionType != dimensionTypeRegistry.getOrThrow(BuiltinDimensionTypes.OVERWORLD)) return;
            ChunkGenerator chunkGenerator = levelStem.generator();

            // Inject biomes to biome source
            if (!(chunkGenerator.getBiomeSource() instanceof MultiNoiseBiomeSource noiseSource)) return;

            List<Pair<Climate.ParameterPoint, Holder<Biome>>> parameters = new ArrayList<>(noiseSource.parameters().values());
            addParameterPoint(parameters, new Pair<>
                (
                    Climate.parameters(Climate.Parameter.span(-1f, -0.6f), Climate.Parameter.span(-0.2f, 0.6f), Climate.Parameter.span(0.1F, 0.6F), FULL_RANGE, Climate.Parameter.span(0.2f, 0.9f), FULL_RANGE, 0f),
                    biomeRegistry.getHolderOrThrow(BiomeRegistry.PERMAFROST_CAVERNS)
                )
            );
            chunkGenerator.biomeSource = MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(parameters));
            chunkGenerator.featuresPerStep = Suppliers
                .memoize(() -> FeatureSorter.buildFeaturesPerStep(List.copyOf(chunkGenerator.biomeSource.possibleBiomes()), biome -> chunkGenerator.generationSettingsGetter.apply(biome).features(), true));

            // Inject surface rules
            if (!(chunkGenerator instanceof NoiseBasedChunkGenerator noiseGenerator)) return;

            NoiseGeneratorSettings noiseGeneratorSettings = noiseGenerator.settings.value();
            SurfaceRules.RuleSource currentRuleSource = noiseGeneratorSettings.surfaceRule();
            if (currentRuleSource instanceof SurfaceRules.SequenceRuleSource sequenceRuleSource)
            {
                List<SurfaceRules.RuleSource> surfaceRules = new ArrayList<>(sequenceRuleSource.sequence());
                addSurfaceRule
                    (surfaceRules, 1,
                        anySurfaceRule(BiomeRegistry.PERMAFROST_CAVERNS,
                            Blocks.SNOW_BLOCK.defaultBlockState(),
                            Blocks.SNOW_BLOCK.defaultBlockState(),
                            Blocks.PACKED_ICE.defaultBlockState())
                    );
                NoiseGeneratorSettings moddedNoiseGeneratorSettings = new NoiseGeneratorSettings(noiseGeneratorSettings.noiseSettings(), noiseGeneratorSettings.defaultBlock(), noiseGeneratorSettings.defaultFluid(),
                    noiseGeneratorSettings.noiseRouter(), SurfaceRules.sequence(surfaceRules.toArray(SurfaceRules.RuleSource[]::new)), noiseGeneratorSettings.spawnTarget(), noiseGeneratorSettings.seaLevel(),
                    noiseGeneratorSettings.disableMobGeneration(), noiseGeneratorSettings.aquifersEnabled(), noiseGeneratorSettings.oreVeinsEnabled(), noiseGeneratorSettings.useLegacyRandomSource());
                noiseGenerator.settings = new Holder.Direct<>(moddedNoiseGeneratorSettings);
            }

        }
    }

    private static SurfaceRules.RuleSource anySurfaceRule (ResourceKey<Biome> biomeKey, BlockState groundBlock, BlockState undergroundBlock, BlockState underwaterBlock)
    {
        return SurfaceRules.ifTrue(SurfaceRules.isBiome(biomeKey),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR),
                    SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), SurfaceRules.state(groundBlock)), SurfaceRules.state(underwaterBlock))),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, true, 0, CaveSurface.FLOOR), SurfaceRules.state(undergroundBlock))));
    }

    private static void addParameterPoint (List<Pair<Climate.ParameterPoint, Holder<Biome>>> parameters, Pair<Climate.ParameterPoint, Holder<Biome>> point)
    {
        if (!parameters.contains(point))
            parameters.add(point);
    }

    private static void addSurfaceRule (List<SurfaceRules.RuleSource> surfaceRules, int index, SurfaceRules.RuleSource rule)
    {
        if (!surfaceRules.contains(rule))
            surfaceRules.add(index, rule);
    }

}
