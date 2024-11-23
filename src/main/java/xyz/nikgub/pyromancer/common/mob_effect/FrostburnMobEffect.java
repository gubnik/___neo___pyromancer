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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.registry.MobEffectRegistry;

public class FrostburnMobEffect extends MobEffect
{
    public FrostburnMobEffect ()
    {
        super(MobEffectCategory.HARMFUL, GeneralUtils.rgbToColorInteger(10, 156, 240));
    }

    @Override
    public void applyEffectTick (@NotNull LivingEntity livingEntity, int pAmplifier)
    {
        if (livingEntity.isOnFire())
        {
            livingEntity.getActiveEffectsMap().put(MobEffectRegistry.FROSTBURN.get(), new MobEffectInstance(MobEffectRegistry.FROSTBURN.get(), 0, 0, true, false));
            return;
        }
        GeneralUtils.coverInParticles(livingEntity, ParticleTypes.SNOWFLAKE, 0.015);
    }

    @Override
    public boolean isDurationEffectTick (int pDuration, int pAmplifier)
    {
        return true;
    }
}
