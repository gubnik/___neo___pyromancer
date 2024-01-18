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
import xyz.nikgub.pyromancer.registries.vanila.BlockRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagDatagen extends IntrinsicHolderTagsProvider<Block> {
    public static final TagKey<Block> FLAMING_GROVE_PLANT_ON = BlockTags.create(new ResourceLocation(PyromancerMod.MOD_ID, "flaming_grove_plant_on"));
    public static final TagKey<Block> FLAMING_GROVE_NATIVE = BlockTags.create(new ResourceLocation(PyromancerMod.MOD_ID, "flaming_grove_native"));
    public static final TagKey<Block> VANILLA_FENCES = BlockTags.create(new ResourceLocation("minecraft", "fences"));
    public static final TagKey<Block> VANILLA_FENCE_GATES = BlockTags.create(new ResourceLocation("minecraft", "fence_gates"));
    public static final TagKey<Block> VANILLA_WOODEN_DOORS = BlockTags.create(new ResourceLocation("minecraft", "wooden_doors"));
    public static final TagKey<Block> VANILLA_LOGS = BlockTags.create(new ResourceLocation("minecraft", "logs"));
    protected BlockTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK, completableFuture, (p_255627_) -> p_255627_.builtInRegistryHolder().key(), modId, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(FLAMING_GROVE_PLANT_ON).add(Blocks.CRIMSON_NYLIUM).add(Blocks.WARPED_NYLIUM);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.getDescriptionId().contains("pyrowood")).toList())
            this.tag(FLAMING_GROVE_NATIVE).add(block);

        /* VANILLA */
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof FenceBlock).toList())
            this.tag(VANILLA_FENCES).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof FenceGateBlock).toList())
            this.tag(VANILLA_FENCE_GATES).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.getDescriptionId().contains("log")).toList())
            this.tag(VANILLA_LOGS).add(block);
        for(Block block : BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof DoorBlock && DoorBlock.isWoodenDoor(block.defaultBlockState())).toList())
            this.tag(VANILLA_WOODEN_DOORS).add(block);
    }
}
