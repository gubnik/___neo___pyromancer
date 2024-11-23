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
package xyz.nikgub.pyromancer.common.entity.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;

public class SizzlingHandFireball extends Fireball implements ItemSupplier
{
    public final int maxLifetime;

    public final float damage;

    public SizzlingHandFireball (EntityType<? extends SizzlingHandFireball> fireball, Level level)
    {
        super(fireball, level);
        this.damage = 4f;
        this.maxLifetime = 20;
    }

    public SizzlingHandFireball (EntityType<? extends SizzlingHandFireball> fireball, Level level, float damage, int maxLifetime)
    {
        super(fireball, level);
        this.damage = damage;
        this.maxLifetime = maxLifetime;
    }

    @Override
    public @NotNull ItemStack getItem ()
    {
        return new ItemStack(Items.FIRE_CHARGE);
    }

    protected void onHitEntity (@NotNull EntityHitResult entityHitResult)
    {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        if (!this.level().isClientSide && !(entity instanceof SizzlingHandFireball))
        {
            entity.setSecondsOnFire(5);
            if (owner == null) return;
            if (owner != entity) this.collisionEffect(entity.level());
            if (!entity.hurt(DamageSourceRegistry.sizzlingHand(this, owner), damage))
            {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks());
            } else if (owner instanceof LivingEntity)
            {
                this.doEnchantDamageEffects((LivingEntity) owner, entity);
            }
        }
    }

    @Override
    protected void onHitBlock (@NotNull BlockHitResult result)
    {
        collisionEffect(this.level());
    }

    @Override
    public void tick ()
    {
        if (this.tickCount > this.maxLifetime && !this.level().isClientSide)
        {
            collisionEffect(this.level());
            this.remove(RemovalReason.DISCARDED);
        }
        super.tick();
    }

    @Override
    protected @NotNull ParticleOptions getTrailParticle ()
    {
        return ParticleTypes.SMOKE;
    }

    @Override
    protected float getInertia ()
    {
        return 1f;
    }

    @Override
    protected void onHit (@NotNull HitResult hitResult)
    {
        super.onHit(hitResult);
        if (!this.level().isClientSide)
        {
            this.discard();
        }
    }

    @Override
    public boolean hurt (@NotNull DamageSource damageSource, float p_36840_)
    {
        return false;
    }

    public void collisionEffect (Level level)
    {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendParticles(ParticleTypes.FLAME, x, y, z, 20, 0.25, 0.25, 0.25, 0.2);
            Vec3 center = new Vec3(x, y, z);
            for (Entity entityiterator : EntityUtils.entityCollector(center, 1 * Math.sqrt(this.damage), this.level()))
            {
                if (!(this.getOwner() == entityiterator) && this.getOwner() != null)
                {
                    entityiterator.hurt(DamageSourceRegistry.sizzlingHand(this, this.getOwner()), this.damage);
                }
            }
        }
    }
}
