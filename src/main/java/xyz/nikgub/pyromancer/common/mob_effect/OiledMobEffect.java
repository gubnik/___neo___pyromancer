package xyz.nikgub.pyromancer.common.mob_effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.registries.MobEffectRegistry;

public class OiledMobEffect extends MobEffect
{
    public OiledMobEffect ()
    {
        super(MobEffectCategory.HARMFUL, -16777152);
    }

    public static float tryIgnition (LivingEntity target, DamageSource damageSource)
    {
        MobEffectInstance oiledInstance = target.getEffect(MobEffectRegistry.OILED.get());
        if (oiledInstance != null && (target.isOnFire() || damageSource.is(DamageTypeTags.IS_FIRE)) && !damageSource.is(DamageTypes.ON_FIRE) && !damageSource.is(DamageTypes.IN_FIRE))
        {
            target.setSecondsOnFire(target.getRemainingFireTicks() / 10 + 5 * (oiledInstance.getAmplifier() + 1));
            target.removeEffect(MobEffectRegistry.OILED.get());
            target.playSound(SoundEvents.BLAZE_SHOOT);
            GeneralUtils.coverInParticles(target, ParticleTypes.FLAME, 0.25D);
            return target.getRemainingFireTicks() / 200f;
        }
        return 0;
    }
}
