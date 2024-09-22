package xyz.nikgub.pyromancer.registry;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.mob_effect.*;

public class MobEffectRegistry
{
    public static DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PyromancerMod.MOD_ID);
    public static final RegistryObject<FrostburnMobEffect> FROSTBURN = MOB_EFFECTS.register("frostburn", FrostburnMobEffect::new);
    public static final RegistryObject<OiledMobEffect> OILED = MOB_EFFECTS.register("oiled",
            OiledMobEffect::new);
    public static final RegistryObject<InfusionMobEffect> OIL_INFUSION = MOB_EFFECTS.register("oil_infusion",
            () -> new InfusionMobEffect(MobEffectCategory.BENEFICIAL, 0, new InfusionMobEffect.Colors(0f, 0.05f, 0.2f, 1.2f),
                    (MobEffectRegistry::oilInfusion)));
    public static final RegistryObject<InfusionMobEffect> FIERY_INFUSION = MOB_EFFECTS.register("fiery_infusion",
            () -> new InfusionMobEffect(MobEffectCategory.BENEFICIAL, 0, new InfusionMobEffect.Colors(1f, 0.3f, 0f, 1f),
                    ((target, directCause, damageSource, damageAmount) ->
                    {
                        target.setSecondsOnFire(3);
                        return 0F;
                    })));
    public static final RegistryObject<InfusionMobEffect> ICE_INFUSION = MOB_EFFECTS.register("ice_infusion",
            () -> new InfusionMobEffect(MobEffectCategory.BENEFICIAL, 0, new InfusionMobEffect.Colors(0f, 0.4f, 1f, 1f),
                    ((target, directCause, damageSource, damageAmount) ->
                    {
                        if (target.canFreeze())
                        {
                            target.setTicksFrozen(target.getTicksFrozen() + 40);
                        }
                        return 0F;
                    })));
    public static RegistryObject<MeltdownEffect> MELTDOWN = MOB_EFFECTS.register("meltdown", MeltdownEffect::new);
    public static RegistryObject<FieryAegisEffect> FIERY_AEGIS = MOB_EFFECTS.register("fiery_aegis", FieryAegisEffect::new);
    public static RegistryObject<SolarCollisionEffect> SOLAR_COLLISION = MOB_EFFECTS.register("solar_collision", SolarCollisionEffect::new);
    public static RegistryObject<MeteoricStrikeEffect> METEORIC_STRIKE = MOB_EFFECTS.register("meteoric_strike", MeteoricStrikeEffect::new);

    private static float creeperInfusion (LivingEntity target, LivingEntity directCause, DamageSource damageSource, float damageAmount)
    {
        GeneralUtils.shortenEffect(directCause, CREEPER_INFUSION.get(), 200);
        target.level().explode(damageSource.getEntity(), target.getX(), target.getY(), target.getZ(), 0.1f * damageAmount, Level.ExplosionInteraction.NONE);
        return 0.0F;
    }    public static final RegistryObject<InfusionMobEffect> CREEPER_INFUSION = MOB_EFFECTS.register("creeper_infusion",
            () -> new InfusionMobEffect(MobEffectCategory.BENEFICIAL, 0, new InfusionMobEffect.Colors(0.25f, 1f, 0.0f, 1f),
                    (MobEffectRegistry::creeperInfusion)));

    private static float midasInfusion (LivingEntity target, LivingEntity directCause, DamageSource damageSource, float damageAmount)
    {
        GeneralUtils.shortenEffect(directCause, MIDAS_INFUSION.get(), 200);
        directCause.level().addFreshEntity(
                new ExperienceOrb(directCause.level(), target.getX(), target.getY(), target.getZ(), (int) damageAmount * 2)
        );
        return 0.0F;
    }

    private static float oilInfusion (LivingEntity target, LivingEntity directCause, DamageSource damageSource, float damageAmount)
    {
        target.removeEffect(OILED.get());
        target.addEffect(new MobEffectInstance(OILED.get(), 100, 0, false, true));
        return 0.0F;
    }    public static final RegistryObject<InfusionMobEffect> MIDAS_INFUSION = MOB_EFFECTS.register("midas_infusion",
            () -> new InfusionMobEffect(MobEffectCategory.BENEFICIAL, 0, new InfusionMobEffect.Colors(1f, 1f, 0f, 1f),
                    (MobEffectRegistry::midasInfusion)));






}
