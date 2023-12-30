package net.nikgub.pyromancer.util;

import net.minecraft.world.level.block.state.BlockState;
import net.nikgub.pyromancer.data.tags.BlockTagList;

public class BlockUtils {
    public static boolean flamingGrovePlantable(BlockState blockState){
        return blockState.is(BlockTagList.FLAMING_GROVE_PLANT_ON);
    }
}
