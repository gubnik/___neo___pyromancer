package xyz.nikgub.pyromancer.common.mob_effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.registries.MobEffectRegistry;

public class FrostburnMobEffect extends MobEffect
{
    public FrostburnMobEffect()
    {
        super(MobEffectCategory.HARMFUL, GeneralUtils.rgbToColorInteger(10, 156, 240));
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int pAmplifier)
    {
        if (livingEntity.isOnFire())
        {
            livingEntity.getActiveEffectsMap().put(MobEffectRegistry.FROSTBURN.get(), new MobEffectInstance(MobEffectRegistry.FROSTBURN.get(), 0, 0, true, false));
            return;
        }
        GeneralUtils.coverInParticles(livingEntity, ParticleTypes.SNOWFLAKE, 0.015);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier)
    {
        return true;
    }
}
