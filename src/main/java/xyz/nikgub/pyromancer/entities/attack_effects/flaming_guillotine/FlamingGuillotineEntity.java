package xyz.nikgub.pyromancer.entities.attack_effects.flaming_guillotine;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import xyz.nikgub.incandescent.util.EntityUtils;
import xyz.nikgub.pyromancer.registries.vanila.DamageSourceRegistry;
import xyz.nikgub.pyromancer.entities.attack_effects.AttackEffectEntity;
import xyz.nikgub.pyromancer.registries.vanila.AttributeRegistry;

public class FlamingGuillotineEntity extends AttackEffectEntity {
    public AnimationState fallAnimationState = new AnimationState();
    public FlamingGuillotineEntity(EntityType<? extends AttackEffectEntity> entityType, Level level) {
        super(entityType, level);
        this.lifetime = 9;
        this.fallAnimationState.start(0);
    }
    @Override
    public void tick()
    {
        if(this.tickCount > lifetime){
            this.remove(RemovalReason.DISCARDED);
            if(this.level() instanceof ServerLevel serverLevel)
            {
                serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 30, 0.5, 2, 0.5, 0.3);
                Entity owner = (this.getPlayerUuid() != null) ? this.level().getPlayerByUUID(this.getPlayerUuid()) : this;
                for(LivingEntity entity : EntityUtils.entityCollector(this.position(), 3 * Mth.sqrt(this.getSize()), this.level()))
                {
                    entity.hurt(DamageSourceRegistry.blazingJournal(this, owner),
                            ((owner instanceof Player player) ? (float) player.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()) : 4f) * this.getSize());
                }
            }
        }
    }
}
