package xyz.nikgub.pyromancer.registries;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.blocks.FirebriarBlock;
import xyz.nikgub.pyromancer.common.blocks.SizzlingVineBlock;
import xyz.nikgub.pyromancer.common.util.BlockUtils;

import java.util.function.Supplier;

public class BlockRegistry {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PyromancerMod.MOD_ID);
    //pyrowood
    public static final RegistryObject<Block> PYROWOOD_LOG = registerBlock("pyrowood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_STEM).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_PYROWOOD_LOG = registerBlock("stripped_pyrowood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(PYROWOOD_LOG.get())));
    public static final RegistryObject<Block> PYROWOOD_PLANKS = registerBlock("pyrowood_planks",
            () -> new Block((BlockBehaviour.Properties.copy(PYROWOOD_LOG.get()))));
    public static final RegistryObject<Block> PYROWOOD_STAIRS = registerBlock("pyrowood_stairs",
            () -> new StairBlock(() -> PYROWOOD_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get())));
    public static final RegistryObject<Block> PYROWOOD_SLAB = registerBlock("pyrowood_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get())));
    public static final RegistryObject<Block> PYROWOOD_FENCE = registerBlock("pyrowood_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get())));
    public static final RegistryObject<Block> PYROWOOD_FENCE_GATE = registerBlock("pyrowood_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get()), WoodTypesRegistry.PYROWOOD));
    public static final RegistryObject<Block> PYROWOOD_DOOR = registerBlock("pyrowood_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get()), BlockSetTypeRegistry.PYROWOOD));
    public static final RegistryObject<Block> PYROWOOD_TRAPDOOR = registerBlock("pyrowood_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get()), BlockSetTypeRegistry.PYROWOOD));
    public static final RegistryObject<Block> PYROWOOD_LEAVES = registerBlock("pyrowood_leaves",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.JUNGLE_LEAVES).lightLevel(s -> 15).emissiveRendering(BlockRegistry::always)));
    //public static final RegistryObject<Block> PYROWOOD_SAPLING = registerBlock("pyrowood_sapling",
    //        () -> new WeirdSaplingBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING), new PyrowoodTreeGrower()), CreativeModeTab.TAB_DECORATIONS);

    // flaming grove
    public static final RegistryObject<Block> PYROMOSSED_NETHERRACK = registerBlock("pyromossed_netherrack",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).strength(2.0F, 3.0F).sound(SoundType.NYLIUM)));

    public static final RegistryObject<Block> PYROMOSS_SPROUTS = registerBlock("pyromoss_sprouts",
            () -> new TallGrassBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_CARPET).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ)){
                protected boolean mayPlaceOn(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
                    return BlockUtils.flamingGrovePlantable(blockState);
                }
            });
//
    public static final RegistryObject<Block> SIZZLING_VINE = registerBlock("sizzling_vine",
            () -> new SizzlingVineBlock(BlockBehaviour.Properties.copy(Blocks.TWISTING_VINES).noCollission().instabreak().sound(SoundType.CAVE_VINES).emissiveRendering(BlockRegistry::always)));

    public static final RegistryObject<Block> FIREBRIAR = registerBlock("firebriar",
            () -> new FirebriarBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).strength(0,0).sound(SoundType.HARD_CROP).noCollission()));

    public static final RegistryObject<Block> BLAZING_POPPY = registerBlock("blazing_poppy",
            () -> new FlowerBlock(() -> MobEffectRegistry.FIERY_AEGIS.get(), 1, BlockBehaviour.Properties.copy(Blocks.POPPY).strength(0,0).sound(SoundType.HARD_CROP).noCollission()){
                @Override
                protected boolean mayPlaceOn(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
                    return BlockUtils.flamingGrovePlantable(blockState);
                }
            });

    public static final RegistryObject<Block> NETHER_LILY = registerBlock("nether_lily",
            () -> new FlowerBlock(() -> MobEffectRegistry.MELTDOWN.get(), 1, BlockBehaviour.Properties.copy(Blocks.ORANGE_TULIP).strength(0,0).sound(SoundType.HARD_CROP).noCollission().lightLevel(i -> 7).emissiveRendering(BlockRegistry::always)){
                @Override
                protected boolean mayPlaceOn(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
                    return BlockUtils.flamingGrovePlantable(blockState);
                }
            });

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> output = BLOCKS.register(name, block);
        registerBlockItem(name, output);
        return output;
    }
    public static <T extends Block> void registerBlockItem(String name, Supplier<T> block){
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
    private static boolean always(BlockState p_50775_, BlockGetter p_50776_, BlockPos p_50777_) {
        return true;
    }
}
