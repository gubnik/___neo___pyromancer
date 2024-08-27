package xyz.nikgub.pyromancer.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.block.FirebriarBlock;
import xyz.nikgub.pyromancer.common.block.RimevineBlock;
import xyz.nikgub.pyromancer.common.block.SizzlingVineBlock;
import xyz.nikgub.pyromancer.common.block.WeirdSaplingBlock;
import xyz.nikgub.pyromancer.common.util.BlockUtils;
import xyz.nikgub.pyromancer.data.ConfiguredFeatureDatagen;

import java.util.function.Supplier;

public class BlockRegistry
{
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PyromancerMod.MOD_ID);

    //pyrowood
    public static final RegistryObject<RotatedPillarBlock> PYROWOOD_LOG = registerBlock("pyrowood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_STEM).sound(SoundType.WOOD)));

    public static final RegistryObject<RotatedPillarBlock> STRIPPED_PYROWOOD_LOG = registerBlock("stripped_pyrowood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(PYROWOOD_LOG.get())));

    public static final RegistryObject<Block> PYROWOOD_PLANKS = registerBlock("pyrowood_planks",
            () -> new Block((BlockBehaviour.Properties.copy(PYROWOOD_LOG.get()))));

    public static final RegistryObject<StairBlock> PYROWOOD_STAIRS = registerBlock("pyrowood_stairs",
            () -> new StairBlock(() -> PYROWOOD_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get())));

    public static final RegistryObject<SlabBlock> PYROWOOD_SLAB = registerBlock("pyrowood_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get())));

    public static final RegistryObject<FenceBlock> PYROWOOD_FENCE = registerBlock("pyrowood_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get())));

    public static final RegistryObject<FenceGateBlock> PYROWOOD_FENCE_GATE = registerBlock("pyrowood_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get()), WoodTypesRegistry.PYROWOOD));

    public static final RegistryObject<DoorBlock> PYROWOOD_DOOR = registerBlock("pyrowood_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get()), BlockSetTypeRegistry.PYROWOOD));

    public static final RegistryObject<TrapDoorBlock> PYROWOOD_TRAPDOOR = registerBlock("pyrowood_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(PYROWOOD_PLANKS.get()), BlockSetTypeRegistry.PYROWOOD));

    public static final RegistryObject<Block> PYROWOOD_LEAVES = registerBlock("pyrowood_leaves",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.JUNGLE_LEAVES).lightLevel(s -> 12).emissiveRendering(BlockRegistry::always)));

    public static final RegistryObject<WeirdSaplingBlock> PYROWOOD_SAPLING = registerBlock("pyrowood_sapling",
            () -> new WeirdSaplingBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING), new AbstractTreeGrower()
            {
                @Override
                protected @NotNull ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature (@NotNull RandomSource pRandom, boolean pHasFlowers)
                {
                    return ConfiguredFeatureDatagen.PYROWOOD_NETHER;
                }
            }));

    public static final RegistryObject<Block> AMBER_BLOCK = registerBlock("amber_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_BLOCK).mapColor(DyeColor.ORANGE).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> NATURAL_AMBER = registerBlock("natural_amber",
            () -> new Block(BlockBehaviour.Properties.copy(AMBER_BLOCK.get()).lightLevel(s -> 7).emissiveRendering(BlockRegistry::always)));

    // flaming grove
    public static final RegistryObject<Block> PYROMOSSED_NETHERRACK = registerBlock("pyromossed_netherrack",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).strength(2.0F, 3.0F).sound(SoundType.NYLIUM)));

    public static final RegistryObject<TallGrassBlock> PYROMOSS_SPROUTS = registerBlock("pyromoss_sprouts",
            () -> new TallGrassBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_CARPET).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ))
            {
                protected boolean mayPlaceOn (@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos)
                {
                    return BlockUtils.flamingGrovePlantable(blockState);
                }
            });
    //
    public static final RegistryObject<SizzlingVineBlock> SIZZLING_VINE = registerBlock("sizzling_vine",
            () -> new SizzlingVineBlock(BlockBehaviour.Properties.copy(Blocks.TWISTING_VINES).noCollission().instabreak().sound(SoundType.CAVE_VINES).emissiveRendering(BlockRegistry::always)));

    public static final RegistryObject<FirebriarBlock> FIREBRIAR = registerBlock("firebriar",
            () -> new FirebriarBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).strength(0, 0).sound(SoundType.HARD_CROP).noCollission().instabreak()));

    public static final RegistryObject<FlowerBlock> BLAZING_POPPY = registerBlock("blazing_poppy",
            () -> new FlowerBlock(() -> MobEffectRegistry.FIERY_AEGIS.get(), 1, BlockBehaviour.Properties.copy(Blocks.POPPY).strength(0, 0).sound(SoundType.HARD_CROP).noCollission())
            {
                @Override
                protected boolean mayPlaceOn (@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos)
                {
                    return BlockUtils.flamingGrovePlantable(blockState);
                }
            });

    public static final RegistryObject<FlowerBlock> NETHER_LILY = registerBlock("nether_lily",
            () -> new FlowerBlock(() -> MobEffectRegistry.MELTDOWN.get(), 1, BlockBehaviour.Properties.copy(Blocks.ORANGE_TULIP).strength(0, 0).sound(SoundType.HARD_CROP).noCollission().lightLevel(i -> 7).emissiveRendering(BlockRegistry::always))
            {
                @Override
                protected boolean mayPlaceOn (@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos)
                {
                    return BlockUtils.flamingGrovePlantable(blockState);
                }
            });

    public static final RegistryObject<Block> RIMEBLOOD_CELL = registerBlock("rimeblood_cell", () -> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_BLOCK).mapColor(DyeColor.CYAN).lightLevel((s) -> 8))
    {
        @Override
        public void onProjectileHit (@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockHitResult pHit, @NotNull Projectile pProjectile)
        {
            FallingBlockEntity.fall(pLevel, pHit.getBlockPos(), pState);
        }
    });

    public static final RegistryObject<Block> RIMEBLOOD_BLOCK = registerBlock("rimeblood_block", () -> new Block(BlockBehaviour.Properties.copy(RIMEBLOOD_CELL.get())));

    public static final RegistryObject<RimevineBlock> RIMEVIME = registerBlock("rimevine",
            () -> new RimevineBlock(BlockBehaviour.Properties.copy(Blocks.TWISTING_VINES).noCollission().instabreak().sound(SoundType.CAVE_VINES)));


    public static <T extends Block> RegistryObject<T> registerBlock (String name, Supplier<T> block)
    {
        RegistryObject<T> output = BLOCKS.register(name, block);
        registerBlockItem(name, output);
        return output;
    }

    public static <T extends Block> void registerBlockItem (String name, Supplier<T> block)
    {
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register (IEventBus bus)
    {
        BLOCKS.register(bus);
    }

    private static boolean always (BlockState p_50775_, BlockGetter p_50776_, BlockPos p_50777_)
    {
        return true;
    }
}
