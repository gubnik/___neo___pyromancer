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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registry.MobEffectRegistry;

public class SolarCollisionEffect extends SmartMobEffect
{
    public SolarCollisionEffect ()
    {
        super(MobEffectCategory.NEUTRAL, 180, 70, 0);
    }

    @Override
    public void applyEffectTick (@NotNull LivingEntity livingEntity, int pAmplifier)
    {
        if (!(livingEntity.level() instanceof ServerLevel level)) return;
        EntityUtils.coverInParticles(livingEntity, ParticleTypes.FLAME, 0.02);
        livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.FIERY_AEGIS.get(), 5, 0, true, false));
        Vec3 movementVector = livingEntity.getDeltaMovement();
        for (LivingEntity target : EntityUtils.entityCollector(livingEntity.getEyePosition(), 2.25, level))
        {
            if (target == livingEntity) continue;
            target.setDeltaMovement(target.getDeltaMovement().offsetRandom(RandomSource.create(), 1));
            target.setSecondsOnFire(3);
            target.hurt(DamageSourceRegistry.symbolOfSun(livingEntity), (float) livingEntity.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()));
        }
        if (movementVector.length() < 0.1D)
            livingEntity.getActiveEffectsMap().put(MobEffectRegistry.SOLAR_COLLISION.get(), new MobEffectInstance(MobEffectRegistry.SOLAR_COLLISION.get(), 0, 0, true, false));
    }

    @Override
    public boolean isDurationEffectTick (int pDuration, int pAmplifier)
    {
        return true;
    }
}
