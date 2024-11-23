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
import xyz.nikgub.pyromancer.registry.MobEffectRegistry;

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
