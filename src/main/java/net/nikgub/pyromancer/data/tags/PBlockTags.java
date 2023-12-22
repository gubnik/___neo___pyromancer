package net.nikgub.pyromancer.data.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.nikgub.pyromancer.PyromancerMod;

public class PBlockTags {
    public static final TagKey<Block> FLAMING_GROVE_PLANT_ON = BlockTags.create(new ResourceLocation(PyromancerMod.MOD_ID, "flaming_grove_plant_on"));
}
