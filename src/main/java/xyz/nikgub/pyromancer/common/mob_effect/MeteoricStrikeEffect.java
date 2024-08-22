package xyz.nikgub.pyromancer.common.mob_effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.registries.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registries.MobEffectRegistry;

public class MeteoricStrikeEffect extends SmartMobEffect
{
    public MeteoricStrikeEffect ()
    {
        super(MobEffectCategory.HARMFUL, 180, 70, 0);
    }

    @Override
    public void applyEffectTick (@NotNull LivingEntity livingEntity, int pAmplifier)
    {
        if (!(livingEntity.level() instanceof ServerLevel level)) return;
        GeneralUtils.coverInParticles(livingEntity, ParticleTypes.FLAME, 0.02);
        Vec3 movementVector = livingEntity.getDeltaMovement();
        for (LivingEntity target : EntityUtils.entityCollector(livingEntity.getEyePosition(), livingEntity.getBbWidth() * 2, level))
        {
            if (target == livingEntity) continue;
            target.setDeltaMovement(target.getDeltaMovement().offsetRandom(RandomSource.create(), 1));
            target.setSecondsOnFire(3);
            target.hurt(DamageSourceRegistry.blazingJournal(livingEntity, null), 2f);
        }
        if (movementVector.length() < 0.1D)
            livingEntity.getActiveEffectsMap().put(MobEffectRegistry.METEORIC_STRIKE.get(), new MobEffectInstance(MobEffectRegistry.METEORIC_STRIKE.get(), 0, 0, true, false));
    }

    @Override
    public boolean isDurationEffectTick (int pDuration, int pAmplifier)
    {
        return true;
    }
}
