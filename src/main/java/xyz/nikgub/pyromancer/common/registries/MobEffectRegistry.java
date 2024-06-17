package xyz.nikgub.pyromancer.common.registries;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.mob_effects.FieryAegisEffect;
import xyz.nikgub.pyromancer.common.mob_effects.MeltdownEffect;

public class MobEffectRegistry {
    public static DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PyromancerMod.MOD_ID);
    public static RegistryObject<MeltdownEffect> MELTDOWN = MOB_EFFECTS.register("meltdown", MeltdownEffect::new);
    public static RegistryObject<FieryAegisEffect> FIERY_AEGIS = MOB_EFFECTS.register("fiery_aegis", FieryAegisEffect::new);
}
