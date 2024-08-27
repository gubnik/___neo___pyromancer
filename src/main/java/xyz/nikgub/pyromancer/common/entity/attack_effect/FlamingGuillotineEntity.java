package xyz.nikgub.pyromancer.common.entity.attack_effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import xyz.nikgub.incandescent.Incandescent;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;

import java.util.Optional;

public class FlamingGuillotineEntity extends AttackEffectEntity
{
    public AnimationState fallAnimationState = new AnimationState();

    private float damage = 5;

    private boolean isDirect = false;

    public FlamingGuillotineEntity (EntityType<? extends AttackEffectEntity> entityType, Level level)
    {
        super(entityType, level);
        this.lifetime = 10;
        this.fallAnimationState.start(0);
    }

    public static FlamingGuillotineEntity createWithDamage (EntityType<? extends FlamingGuillotineEntity> entityType, Level level, float damage, boolean isDirect)
    {
        FlamingGuillotineEntity entity = new FlamingGuillotineEntity(entityType, level);
        entity.damage = damage;
        entity.isDirect = isDirect;
        return entity;
    }

    @Override
    public void tick ()
    {
        if (this.tickCount <= lifetime) return;
        this.remove(RemovalReason.DISCARDED);
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 30, 0.5, 2, 0.5, 0.3);
        Optional<Player> ownerOpt = this.getOwner();
        if (ownerOpt.isEmpty()) return;
        Player owner = ownerOpt.get();
        for (LivingEntity entity : EntityUtils.entityCollector(this.position(), 3 * Mth.sqrt(this.getSize()), this.level()))
        {
            if (entity == owner) continue;
            DamageSource guillotineDamageType = (isDirect) ? DamageSourceRegistry.guillotine(owner) : DamageSourceRegistry.blazingJournal(this, owner);
            entity.hurt(guillotineDamageType, (damage + (float) owner.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get())) * Mth.sqrt(this.getSize()));
        }
        final int tick = owner.tickCount;
        Incandescent.runShakeFor(1, (localPlayer -> tick + 10 < localPlayer.tickCount));
    }

    @Override
    public void addToLevelForPlayerAt (Level level, Player player, Vec3 pos)
    {
        this.setPlayerUuid(player.getUUID());
        this.moveTo(pos);
        this.setYRot(player.getYRot());
        player.level().addFreshEntity(this);
    }
}
