package net.nikgub.pyromancer.data.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.nikgub.pyromancer.PyromancerMod;

public class BlockTagList {
    public static final TagKey<Block> FLAMING_GROVE_PLANT_ON = BlockTags.create(new ResourceLocation(PyromancerMod.MOD_ID, "flaming_grove_plant_on"));
    public static final TagKey<Block> VANILLA_FENCES = BlockTags.create(new ResourceLocation("minecraft", "fences"));
    public static final TagKey<Block> VANILLA_FENCE_GATES = BlockTags.create(new ResourceLocation("minecraft", "fence_gates"));
    public static final TagKey<Block> VANILLA_WOODEN_DOORS = BlockTags.create(new ResourceLocation("minecraft", "wooden_doors"));
    public static final TagKey<Block> VANILLA_LOGS = BlockTags.create(new ResourceLocation("minecraft", "logs"));

}
