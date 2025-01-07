/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, Nikolay Gubankov (aka nikgub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
