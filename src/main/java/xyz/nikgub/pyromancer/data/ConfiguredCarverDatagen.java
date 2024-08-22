package xyz.nikgub.pyromancer.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import xyz.nikgub.pyromancer.PyromancerMod;

public class ConfiguredCarverDatagen
{
    public static final ResourceKey<ConfiguredWorldCarver<?>> FLAMING_GROVE_CARVER = createKey("flaming_grove");

    private static ResourceKey<ConfiguredWorldCarver<?>> createKey (String pName)
    {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, new ResourceLocation(PyromancerMod.MOD_ID, pName));
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
    }
}
