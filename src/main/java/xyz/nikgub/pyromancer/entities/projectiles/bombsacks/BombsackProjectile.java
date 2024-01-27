package xyz.nikgub.pyromancer.entities.projectiles.bombsacks;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registries.vanila.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registries.vanila.ItemRegistry;

public class BombsackProjectile extends ThrowableItemProjectile {
    public BombsackProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegistry.BOMBSACK.get();
    }
    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItemRaw();
        return (itemstack.isEmpty() ? ParticleTypes.SMOKE : new ItemParticleOption(ParticleTypes.ITEM, itemstack));
    }
    @Override
    public void handleEntityEvent(byte b) {
        if (b == 3) {
            ParticleOptions particleoptions = this.getParticle();
            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }
    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.hurt(DamageSourceRegistry.bombsack(this, this.getOwner() != null ? this.getOwner() : this), 1);
    }
    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
            collisionEffect();
        }
    }
    public void collisionEffect(){
        this.level().explode(this, DamageSourceRegistry.bombsack(this, this.getOwner() != null ? this.getOwner() : this), null, this.getX(), this.getY(), this.getZ(), 1.3f, false, Level.ExplosionInteraction.NONE);
    }
}
