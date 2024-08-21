package xyz.nikgub.pyromancer.common.entity.attack_effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.registries.AttributeRegistry;
import xyz.nikgub.pyromancer.registries.DamageSourceRegistry;

import java.util.Optional;

public class FlamingGuillotineEntity extends AttackEffectEntity
{
    public AnimationState fallAnimationState = new AnimationState();

    public FlamingGuillotineEntity(EntityType<? extends AttackEffectEntity> entityType, Level level)
	{
        super(entityType, level);
        this.lifetime = 9;
        this.fallAnimationState.start(0);
    }

    @Override
    public void tick()
    {
        if (this.tickCount <= lifetime) return;
        this.remove(RemovalReason.DISCARDED);
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 30, 0.5, 2, 0.5, 0.3);
        Optional<Player> ownerOpt = this.getOwner();
        if (ownerOpt.isEmpty()) return;
        Player owner = ownerOpt.get();
        for(LivingEntity entity : EntityUtils.entityCollector(this.position(), 3 * Mth.sqrt(this.getSize()), this.level()))
        {
            if (entity == owner) continue;
            entity.hurt(DamageSourceRegistry.blazingJournal(this, owner), ((float) owner.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get())) * this.getSize());
        }
    }

    @Override
    public void addToLevelForPlayerAt(Level level, Player player, Vec3 pos)
	{
        this.setPlayerUuid(player.getUUID());
        this.moveTo(pos);
        this.setYRot(player.getYRot());
        player.level().addFreshEntity(this);
    }
}
