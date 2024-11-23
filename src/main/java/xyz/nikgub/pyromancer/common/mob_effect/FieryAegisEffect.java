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

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FieryAegisEffect extends SmartMobEffect
{
    public FieryAegisEffect ()
    {
        super(MobEffectCategory.BENEFICIAL, 240, 125, 0);
    }

    public float performAttack (float damageAmount, LivingEntity victim, LivingEntity attacker)
    {
        attacker.setSecondsOnFire((int) damageAmount + 1);
        attacker.knockback(damageAmount / 2f, victim.getX() - attacker.getX(), victim.getZ() - attacker.getZ());
        return -0.75f;
    }
}
