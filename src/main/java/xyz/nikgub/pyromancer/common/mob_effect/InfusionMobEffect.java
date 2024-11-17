package xyz.nikgub.pyromancer.common.mob_effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class InfusionMobEffect extends MobEffect
{

    public final static String INFUSION_TAG = "___Infusion_Item___";
    public final static String RED_TAG = "___Infusion_Red___";
    public final static String BLUE_TAG = "___Infusion_Blue___";
    public final static String GREEN_TAG = "___Infusion_Green___";
    public final static String ALPHA_TAG = "___Infusion_Alpha___";

    private final Colors itemColors;
    private final InfusionEffect effect;

    public InfusionMobEffect (MobEffectCategory effectCategory, int color, Colors itemColors, InfusionEffect effect)
    {
        super(effectCategory, color);
        this.itemColors = itemColors;
        this.effect = effect;
    }

    public static float tryEffect (LivingEntity target, Entity directCause, DamageSource damageSource, float damageAmount)
    {
        if (!(directCause instanceof LivingEntity source)) return 0;
        Optional<InfusionMobEffect> effectOptional = source.getActiveEffects().stream()
                .map(MobEffectInstance::getEffect)
                .filter(instanceEffect -> instanceEffect instanceof InfusionMobEffect)
                .map(mobEffect -> (InfusionMobEffect) mobEffect).findFirst();
        return effectOptional.map(mobEffect -> mobEffect.getInfusionEffect().effect(target, source, damageSource, damageAmount)).orElse(0F);
    }

    public InfusionEffect getInfusionEffect ()
    {
        return effect;
    }

    public Colors getItemColors ()
    {
        return itemColors;
    }

    @FunctionalInterface
    public interface InfusionEffect
    {
        float effect (LivingEntity target, LivingEntity directCause, DamageSource damageSource, float damageAmount);
    }

    public record Colors(float r, float g, float b, float a)
    {
    }
}
