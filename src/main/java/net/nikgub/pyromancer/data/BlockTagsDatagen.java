package net.nikgub.pyromancer.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.data.tags.BlockTagList;
import net.nikgub.pyromancer.registries.vanila.BlockRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagsDatagen extends IntrinsicHolderTagsProvider<Block> {
    protected BlockTagsDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK, completableFuture, (p_255627_) -> p_255627_.builtInRegistryHolder().key(), modId, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(BlockTagList.FLAMING_GROVE_PLANT_ON).add(Blocks.CRIMSON_NYLIUM).add(Blocks.WARPED_NYLIUM);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof FenceBlock).toList())
            this.tag(BlockTagList.VANILLA_FENCES).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof FenceGateBlock).toList())
            this.tag(BlockTagList.VANILLA_FENCE_GATES).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.getDescriptionId().contains("log")).toList())
            this.tag(BlockTagList.VANILLA_LOGS).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof DoorBlock && DoorBlock.isWoodenDoor(block.defaultBlockState())).toList())
            this.tag(BlockTagList.VANILLA_WOODEN_DOORS).add(block);
    }
}
