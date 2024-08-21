package xyz.nikgub.pyromancer.common.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.client.animations.DeterminedAnimation;
import xyz.nikgub.incandescent.client.animations.IAnimationPurposeEntity;
import xyz.nikgub.pyromancer.registries.DamageSourceRegistry;

import java.util.List;

public class UnburnedEntity extends Monster implements IFlamingGroveNativeEntity, IAnimationPurposeEntity
{
    public AnimationState ATTACK = new AnimationState();

    public AnimationState KICK = new AnimationState();

    public AnimationState EXPLOSION = new AnimationState();

    public AnimationState EMERGE = new AnimationState();

    public AnimationState IDLE = new AnimationState();

    public static final EntityDataSerializer<DeterminedAnimation.AnimationPurpose> ANIMATION_STATE = EntityDataSerializer.simpleEnum(DeterminedAnimation.AnimationPurpose.class);

    static
    {
        EntityDataSerializers.registerSerializer(ANIMATION_STATE);
    }

    private static final EntityDataAccessor<DeterminedAnimation.AnimationPurpose> DATA_STATE = SynchedEntityData.defineId(UnburnedEntity.class, ANIMATION_STATE);

    private int attackTick = 0;
    private static final int attackTimeOffset = 6;

    private int explosionTick = 0;
    private static final int explosionTickOffset = 8;
    private static final int explosionTickCooldown = 200;
    private boolean timedExplosionReady = false;

    public UnburnedEntity(EntityType<? extends Monster> entityType, Level level)
	{
        super(entityType, level);
        this.entityData.define(DATA_STATE, DeterminedAnimation.AnimationPurpose.IDLE);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> pKey)
    {
        this.animationSyncedDataHandler(pKey);
        super.onSyncedDataUpdated(pKey);
    }

    @Override
    public EntityDataAccessor<DeterminedAnimation.AnimationPurpose> getAnimationStateDataAccessor()
	{
        return DATA_STATE;
    }

    @Override
    public void tick()
    {
        this.setInvulnerable(this.EXPLOSION.isStarted() || this.EMERGE.isStarted());

        super.tick();

        if (this.getTarget() == null) return;

        if (this.attackTick != 0 && this.tickCount >= this.attackTick + attackTimeOffset)
        {
            this.level().explode(this, DamageSourceRegistry.unburnedExplosion(this), null,
                    this.getX() + this.getLookAngle().x*2,
                    this.getY() + 2,
                    this.getZ() + this.getLookAngle().z*2,
                    0.8f, true, Level.ExplosionInteraction.NONE);
            this.attackTick = 0;
        }
        if (this.attackTick != 0 && this.tickCount >= this.attackTick + 12)
        {
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.IDLE);
        }
        if (this.tickCount >= this.explosionTick + explosionTickCooldown)
        {
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.STOMP);
            this.explosionTick = this.tickCount;
            this.timedExplosionReady = true;
        }
        if (this.tickCount >= this.explosionTick + explosionTickOffset && this.timedExplosionReady)
        {
            this.explosionAttack();
            this.explosionTick += explosionTickCooldown - explosionTickOffset;
            this.timedExplosionReady = false;
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.IDLE);
        }
    }

    private void explosionAttack()
    {
        this.level().explode(this, DamageSourceRegistry.unburnedExplosion(this), null,
                this.getX() + this.getLookAngle().x*2,
                this.getY() + 2,
                this.getZ() + this.getLookAngle().z*2,
                3f, true, Level.ExplosionInteraction.NONE);
    }

    public boolean doHurtTarget(@NotNull Entity target)
    {
        if (target instanceof LivingEntity entity && entity.isBlocking())
        {
            if (entity instanceof Player player) player.getCooldowns().addCooldown(Items.SHIELD, 20);
            entity.stopUsingItem();
            entity.setDeltaMovement(this.getLookAngle().x(), 1, this.getLookAngle().z());
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.SPECIAL_HURT);
            return super.doHurtTarget(entity);
        }
        this.runAnimationOf(DeterminedAnimation.AnimationPurpose.MAIN_ATTACK);
        this.attackTick = this.tickCount;
        return super.doHurtTarget(target);
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float amount)
    {
        if (amount > 10f)
        {
            this.stopAllAnimations();
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.SPECIAL_HURT);
            return super.hurt(damageSource, 10F + (float) Math.log(10 - amount));
        }
        return super.hurt(damageSource, amount);
    }

    public static AttributeSupplier setAttributes()
    {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 350)
                .add(Attributes.MOVEMENT_SPEED, 0.45f)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.ATTACK_SPEED, 1)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1f)
                .build();
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2f, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public @NotNull List<DeterminedAnimation> getAllAnimations()
	{
        return List.of(
                new DeterminedAnimation(this.ATTACK, DeterminedAnimation.AnimationPurpose.MAIN_ATTACK, (byte) 70, 0),
                new DeterminedAnimation(this.EXPLOSION, DeterminedAnimation.AnimationPurpose.STOMP, (byte) 71, 2),
                new DeterminedAnimation(this.KICK, DeterminedAnimation.AnimationPurpose.SPECIAL_HURT, (byte) 72, 1),
                new DeterminedAnimation(this.EMERGE, DeterminedAnimation.AnimationPurpose.SPAWN, (byte) 73, 0),
                new DeterminedAnimation(this.IDLE, DeterminedAnimation.AnimationPurpose.IDLE, (byte) 0, 0)
        );
    }
}
