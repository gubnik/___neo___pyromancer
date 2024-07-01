package xyz.nikgub.pyromancer.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registries.BlockRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagDatagen extends IntrinsicHolderTagsProvider<Block> {

    public static final TagKey<Block> FLAMING_GROVE_PLANT_ON = BlockTags.create(new ResourceLocation(PyromancerMod.MOD_ID, "flaming_grove_plant_on"));
    public static final TagKey<Block> FLAMING_GROVE_NATIVE = BlockTags.create(new ResourceLocation(PyromancerMod.MOD_ID, "flaming_grove_native"));
    public static final TagKey<Block> AMBER_REPLACEABLE = BlockTags.create(new ResourceLocation(PyromancerMod.MOD_ID, "amber_replaceable"));


    protected BlockTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK, completableFuture, (p_255627_) -> p_255627_.builtInRegistryHolder().key(), modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        this.tag(AMBER_REPLACEABLE)
                .add(Blocks.NETHERRACK);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockRegistry.AMBER_BLOCK.get())
                .add(BlockRegistry.NATURAL_AMBER.get());

        this.tag(BlockTags.NYLIUM)
                .add(BlockRegistry.PYROMOSSED_NETHERRACK.get());

        this.tag(FLAMING_GROVE_PLANT_ON)
                .add(Blocks.CRIMSON_NYLIUM)
                .add(Blocks.WARPED_NYLIUM)
                .add(BlockRegistry.PYROMOSSED_NETHERRACK.get());

        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.getDescriptionId().contains("pyrowood")).toList())
        {
            this.tag(BlockTags.MINEABLE_WITH_AXE)
                    .add(block);
            this.tag(FLAMING_GROVE_NATIVE)
                    .add(block);
        }

        this.tag(FLAMING_GROVE_NATIVE)
                .add(BlockRegistry.SIZZLING_VINE.get())
                .add(BlockRegistry.PYROMOSS_SPROUTS.get())
                .add(BlockRegistry.FIREBRIAR.get());

        /* VANILLA */
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof FenceBlock).toList())
            this.tag(BlockTags.FENCES).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof FenceGateBlock).toList())
            this.tag(BlockTags.FENCE_GATES).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.getDescriptionId().contains("log")).toList())
            this.tag(BlockTags.LOGS).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof DoorBlock && DoorBlock.isWoodenDoor(block.defaultBlockState())).toList())
            this.tag(BlockTags.WOODEN_DOORS).add(block);
    }
}
