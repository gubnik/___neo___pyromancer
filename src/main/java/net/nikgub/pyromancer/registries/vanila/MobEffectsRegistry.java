package net.nikgub.pyromancer.registries.vanila;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.mob_effects.MeltdownEffect;

public class MobEffectsRegistry {
    public static DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PyromancerMod.MOD_ID);
    public static RegistryObject<MobEffect> MELTDOWN = MOB_EFFECTS.register("meltdown", MeltdownEffect::new);
}
