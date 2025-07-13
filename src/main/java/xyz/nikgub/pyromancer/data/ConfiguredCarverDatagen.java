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

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import xyz.nikgub.pyromancer.PyromancerMod;

public class ConfiguredCarverDatagen
{
    public static final ResourceKey<ConfiguredWorldCarver<?>> FLAMING_GROVE_CARVER = createKey("flaming_grove");
    public static final ResourceKey<ConfiguredWorldCarver<?>> WIDER_CAVE = createKey("wider_cave");

    private static ResourceKey<ConfiguredWorldCarver<?>> createKey (String pName)
    {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, pName));
    }

    public static void bootstrap (BootstapContext<ConfiguredWorldCarver<?>> pContext)
    {
        HolderGetter<Block> holdergetter = pContext.lookup(Registries.BLOCK);
        pContext.register(FLAMING_GROVE_CARVER, WorldCarver.NETHER_CAVE.configured(
            new CaveCarverConfiguration(0.2F,
                UniformHeight.of(VerticalAnchor.absolute(0),
                    VerticalAnchor.belowTop(1)), ConstantFloat.of(0.6F),
                VerticalAnchor.aboveBottom(10), holdergetter.getOrThrow(BlockTags.NETHER_CARVER_REPLACEABLES),
                ConstantFloat.of(1.5F), ConstantFloat.of(1.2F), ConstantFloat.of(-0.7F))));
        pContext.register(WIDER_CAVE, WorldCarver.CAVE.configured(
            new CaveCarverConfiguration(0.15F,
                UniformHeight.of(VerticalAnchor.aboveBottom(8),
                    VerticalAnchor.absolute(180)),
                UniformFloat.of(0.2F, 1.1F),
                VerticalAnchor.aboveBottom(8),
                CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
                holdergetter.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
                UniformFloat.of(1.1F, 1.6F),
                UniformFloat.of(0.9F, 1.7F),
                UniformFloat.of(-1.0F, -0.4F))
        ));
    }
}
