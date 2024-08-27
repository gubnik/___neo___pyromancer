package xyz.nikgub.pyromancer.common.block;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registry.BlockRegistry;

public class RimevineBlock extends GrowingPlantHeadBlock implements BonemealableBlock
{
    public RimevineBlock (Properties properties)
    {
        super(properties, Direction.DOWN,
                Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D),
                false, 0.05);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition (StateDefinition.@NotNull Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed (@NotNull RandomSource randomSource)
    {
        return 0;
    }

    @Override
    protected boolean canGrowInto (@NotNull BlockState blockState)
    {
        return false;
    }

    @Override
    protected @NotNull Block getBodyBlock ()
    {
        return BlockRegistry.RIMEVIME.get();
    }
}

