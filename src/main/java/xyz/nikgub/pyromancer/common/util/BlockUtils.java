package xyz.nikgub.pyromancer.common.util;

import net.minecraft.world.level.block.state.BlockState;
import xyz.nikgub.pyromancer.data.BlockTagDatagen;

public class BlockUtils
{
    public static boolean flamingGrovePlantable(BlockState blockState)
    {
        return blockState.is(BlockTagDatagen.FLAMING_GROVE_PLANT_ON);
    }
}
