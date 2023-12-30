package net.nikgub.pyromancer.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.nikgub.pyromancer.data.tags.BlockTagDatagen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagsDatagen extends IntrinsicHolderTagsProvider<Block> {
    protected BlockTagsDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK, completableFuture, (p_255627_) -> p_255627_.builtInRegistryHolder().key(), modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(BlockTagDatagen.FLAMING_GROVE_PLANT_ON).add(Blocks.CRIMSON_NYLIUM).add(Blocks.WARPED_NYLIUM);
    }
}
