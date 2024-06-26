package xyz.nikgub.pyromancer.common.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.common.entities.attack_effects.AttackEffectEntity;
import xyz.nikgub.pyromancer.registries.AttributeRegistry;
import xyz.nikgub.pyromancer.registries.DamageSourceRegistry;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class UnburnedSpiritEntity extends AttackEffectEntity {

    public AnimationState ATTACK = new AnimationState();

    public AnimationState KICK = new AnimationState();

    public final AnimationState activeAnimation;

    public UnburnedSpiritEntity(EntityType<? extends AttackEffectEntity> entityType, Level level) {
        super(entityType, level);
        AnimationState [] list = new AnimationState[]{ATTACK, KICK};
        this.activeAnimation = list [ThreadLocalRandom.current().nextInt(list.length)];
        this.lifetime = 10;
        this.activeAnimation.start(0);
    }

    @Override
    public void tick()
    {
        if(this.tickCount > lifetime){
            this.remove(RemovalReason.DISCARDED);
            if(this.level() instanceof ServerLevel serverLevel)
            {
                serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 30, 1.5, 4, 1.5, 0.3);
                Optional<Player> ownerOpt = this.getOwner();
                if (ownerOpt.isEmpty()) return;
                Player owner = ownerOpt.get();
                for(LivingEntity entity : EntityUtils.entityCollector(this.position(), 3 * Mth.sqrt(this.getSize()), this.level()))
                {
                    if (entity == owner) continue;
                    entity.hurt(DamageSourceRegistry.blazingJournal(this, owner), (10f + (float) owner.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get())) * this.getSize());
                }
            }
        }
    }

    @Override
    public void addToLevelForPlayerAt(Level level, Player player, Vec3 pos) {
        Vec3 center = player.position();
        List<UnburnedSpiritEntity> collected = level.getEntitiesOfClass(UnburnedSpiritEntity.class, (new AABB(center, center)).inflate(16), (e) -> true).stream().sorted(Comparator.comparingDouble((entityFound) -> entityFound.distanceToSqr(center))).toList();
        for (UnburnedSpiritEntity spirit : collected)
        {
            if (spirit.getOwner().isPresent() && spirit.getOwner().get() == player)
                spirit.discard();
        }
        this.setPlayerUuid(player.getUUID());
        this.moveTo(pos);
        this.setYRot(player.getYRot());
        player.level().addFreshEntity(this);
    }
}
