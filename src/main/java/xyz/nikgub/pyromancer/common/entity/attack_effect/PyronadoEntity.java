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
package xyz.nikgub.pyromancer.common.entity.attack_effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;

import java.util.UUID;

public class PyronadoEntity extends AttackEffectEntity
{
    public float sizeCoefficient = 0f;

    public PyronadoEntity (EntityType<? extends AttackEffectEntity> entityType, Level level)
    {
        super(entityType, level);
        this.lifetime = 60;
    }

    @Override
    public void tick ()
    {
        super.tick();
        this.sizeCoefficient = Mth.clamp((float) this.tickCount / 20f, 0f, 2f);
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        final float c = this.sizeCoefficient;
        final double R = 4 * c;
        final double X = this.getX();
        final double Y = this.getY();
        final double Z = this.getZ();
        final double sinK = R * Math.sin(Math.toRadians(this.tickCount * 18));
        final double cosK = R * Math.cos(Math.toRadians(this.tickCount * 18));
        serverLevel.sendParticles(ParticleTypes.FLAME, X + sinK, Y + this.tickCount * 0.1, Z + cosK, (int) (1 + 5 * c), 0.1, 0.1, 0.1, 0);
        serverLevel.sendParticles(ParticleTypes.FLAME, X - sinK, Y + this.tickCount * 0.1, Z - cosK, (int) (1 + 5 * c), 0.1, 0.1, 0.1, 0);
        if (this.tickCount % 5 != 0) return;
        UUID ownerUUID = this.getPlayerUuid();
        if (ownerUUID == null) return;
        Player owner = this.level().getPlayerByUUID(ownerUUID);
        if (owner == null) return;
        double dx, dy, dz;
        Vec3 direction;
        double cy = Y + this.tickCount * 0.1;
        for (LivingEntity entity : EntityUtils.entityCollector(new Vec3(X, cy, Z), 2 + 2 * this.sizeCoefficient, owner.level()))
        {
            if (!entity.equals(owner))
            {
                entity.hurt(DamageSourceRegistry.pyronado(this, owner), (float) owner.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()));
                dx = X - entity.getX();
                dy = cy - entity.getY();
                dz = Z - entity.getZ();
                direction = new Vec3(dx, dy, dz).normalize();
                entity.setDeltaMovement(direction);
            }
        }
    }

    @Override
    public void addToLevelForPlayerAt (Level level, Player player, Vec3 pos)
    {
        this.setPlayerUuid(player.getUUID());
        this.setSize(1);
        this.setPos(pos);
        level.addFreshEntity(this);
    }
}
