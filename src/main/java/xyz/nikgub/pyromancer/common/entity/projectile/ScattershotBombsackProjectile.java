package xyz.nikgub.pyromancer.common.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

public class ScattershotBombsackProjectile extends BombsackProjectile
{

    public ScattershotBombsackProjectile (EntityType<? extends ThrowableItemProjectile> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    protected @NotNull Item getDefaultItem ()
    {
        return ItemRegistry.SCATTERSHOT_BOMBSACK.get();
    }

    @Override
    public void collisionEffect ()
    {
        this.level().explode(this, DamageSourceRegistry.bombsack(this, this.getOwner() != null ? this.getOwner() : this), null, this.getX(), this.getY(), this.getZ(), 1.8f, false, Level.ExplosionInteraction.NONE);
    }
}
