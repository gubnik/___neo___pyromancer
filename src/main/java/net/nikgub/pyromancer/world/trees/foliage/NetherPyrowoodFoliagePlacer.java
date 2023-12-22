package net.nikgub.pyromancer.world.trees.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.nikgub.pyromancer.registries.vanila.world.FoliageTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class NetherPyrowoodFoliagePlacer extends FancyFoliagePlacer {
    public static final Codec<NetherPyrowoodFoliagePlacer> CODEC = RecordCodecBuilder.create((p_70161_) ->
             blobParts(p_70161_).apply(p_70161_, NetherPyrowoodFoliagePlacer::new));
    public NetherPyrowoodFoliagePlacer(IntProvider p_161397_, IntProvider p_161398_, int p_161399_) {
        super(p_161397_, p_161398_, p_161399_);
    }
    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return FoliageTypeRegistry.NETHER_PYROWOOD_FOLIAGE_PLACER.get();
    }
    @Override
    protected boolean shouldSkipLocation(@NotNull RandomSource p_225595_, int p_225596_, int p_225597_, int p_225598_, int p_225599_, boolean p_225600_) {
        return (
                Mth.square(p_225596_)/4 + Mth.square(p_225599_)/9
                ) <= 2*3;
    }
}
