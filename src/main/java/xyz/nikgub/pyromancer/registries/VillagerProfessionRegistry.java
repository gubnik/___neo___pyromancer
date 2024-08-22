package xyz.nikgub.pyromancer.registries;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.Set;

public class VillagerProfessionRegistry
{
    public static final DeferredRegister<PoiType> POIS = DeferredRegister.create(ForgeRegistries.POI_TYPES, PyromancerMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, PyromancerMod.MOD_ID);

    public static final RegistryObject<PoiType> DEMONOLOGIST_POI = POIS.register("demonologist_poi",
            () -> new PoiType(getBlockStates(Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL), 1, 1));

    public static final RegistryObject<VillagerProfession> DEMONOLOGIST = PROFESSIONS.register("demonologist",
            () -> new VillagerProfession("demonologist",
                    (poiTypeHolder -> poiTypeHolder.get() == DEMONOLOGIST_POI.get()),
                    (poiTypeHolder -> poiTypeHolder.get() == DEMONOLOGIST_POI.get()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_BUTCHER));

    public static Set<BlockState> getBlockStates (Block... blocks)
    {
        ImmutableSet.Builder<BlockState> builder = ImmutableSet.builder();
        for (Block block : blocks) builder.addAll(ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()));
        return builder.build();
    }
}
