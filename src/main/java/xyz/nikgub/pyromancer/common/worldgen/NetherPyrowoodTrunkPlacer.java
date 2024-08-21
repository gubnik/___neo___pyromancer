package xyz.nikgub.pyromancer.common.worldgen;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

public class NetherPyrowoodTrunkPlacer extends TrunkPlacer
{
    public static DeferredRegister<TrunkPlacerType<?>> TRUNK_TYPE_REGISTRY = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, PyromancerMod.MOD_ID);

    public static final RegistryObject<TrunkPlacerType<?>> NETHER_PYROWOOD_TRUNK_PLACER = TRUNK_TYPE_REGISTRY.register("nether_pyrowood_trunk_placer",
            ()-> new TrunkPlacerType<>(NetherPyrowoodTrunkPlacer.CODEC));

    public static final Codec<NetherPyrowoodTrunkPlacer> CODEC = RecordCodecBuilder.create((p_70161_) -> trunkPlacerParts(p_70161_).apply(p_70161_, NetherPyrowoodTrunkPlacer::new));

    public NetherPyrowoodTrunkPlacer(int p_70268_, int p_70269_, int p_70270_)
	{
        super(p_70268_, p_70269_, p_70270_);
    }

    @Override
    protected @NotNull TrunkPlacerType<?> type()
	{
        return NETHER_PYROWOOD_TRUNK_PLACER.get();
    }
    @Override
    public @NotNull List<FoliagePlacer.FoliageAttachment> placeTrunk(@NotNull LevelSimulatedReader levelSimulatedReader, @NotNull BiConsumer<BlockPos, BlockState> bPosState, @NotNull RandomSource randomSource, int someInt, @NotNull BlockPos blockPos, @NotNull TreeConfiguration treeConfiguration)
	{
        setDirtAt(levelSimulatedReader, bPosState, randomSource, blockPos.below(), treeConfiguration);
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        int dx, dz;
        int DX = 0, DZ = 0;
        Direction ultDirection = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource);
        OptionalInt optionalInt = OptionalInt.empty();
        int k = 0;
        for(int dy = 0; dy < someInt; dy++)
	    {
            int y1 = y + dy;

            if(dy > someInt * 0.4)
	        {
                for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                    dx = direction.getStepX();
                    dz = direction.getStepZ();
                    if(this.placeLog(levelSimulatedReader, bPosState, randomSource, mutableBlockPos.set(x + dx*k + DX, y1, z + dz*k + DZ), treeConfiguration)){
                        optionalInt = OptionalInt.of(y1 + 1);
                    }
                }
                k++;
            }
            else if (dy <= someInt * 0.4)
	        {
                if(this.placeLog(levelSimulatedReader, bPosState, randomSource, mutableBlockPos.set(x + DX, y1, z + DZ), treeConfiguration)) {
                    optionalInt = OptionalInt.of(y1 + 1);
                }
                if((dy + 1) % 3 == 0)
	            {
                    DX += ultDirection.getStepX();
                    DZ += ultDirection.getStepZ();
                }
            }

        }
        int k1 = k -1;
        for(Direction direction : Direction.Plane.HORIZONTAL.stream().toList()){
            if(optionalInt.isPresent()){
                list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(x + DX + direction.getStepX()*k1, optionalInt.getAsInt() - 1, z + DZ + direction.getStepZ()*k1), 1, false));
            }
        }
        return list;
    }
}
