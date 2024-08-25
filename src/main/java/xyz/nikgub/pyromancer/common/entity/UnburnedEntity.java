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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.client.animations.DeterminedAnimation;
import xyz.nikgub.incandescent.client.animations.IAnimationPurposeEntity;

import java.util.List;

public class UnburnedEntity extends Monster implements IAnimationPurposeEntity
{
    public static final EntityDataSerializer<DeterminedAnimation.AnimationPurpose> ANIMATION_STATE = EntityDataSerializer.simpleEnum(DeterminedAnimation.AnimationPurpose.class);
    private static final EntityDataAccessor<DeterminedAnimation.AnimationPurpose> DATA_STATE = SynchedEntityData.defineId(UnburnedEntity.class, ANIMATION_STATE);
    private static final EntityDataAccessor<Integer> BATTLE_TICK = SynchedEntityData.defineId(UnburnedEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(UnburnedEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STOMP_TICK = SynchedEntityData.defineId(UnburnedEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> KICK_TICK = SynchedEntityData.defineId(UnburnedEntity.class, EntityDataSerializers.INT);

    static
    {
        EntityDataSerializers.registerSerializer(ANIMATION_STATE);
    }

    public AnimationState ATTACK = new AnimationState();
    public AnimationState KICK = new AnimationState();
    public AnimationState EXPLOSION = new AnimationState();
    public AnimationState EMERGE = new AnimationState();
    public AnimationState IDLE = new AnimationState();

    public UnburnedEntity (EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);
        this.entityData.define(DATA_STATE, DeterminedAnimation.AnimationPurpose.IDLE);
        this.entityData.define(BATTLE_TICK, 0);
        this.entityData.define(ATTACK_TICK, 0);
        this.entityData.define(STOMP_TICK, 0);
        this.entityData.define(KICK_TICK, 0);
    }

    public static AttributeSupplier setAttributes ()
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
    public void onSyncedDataUpdated (@NotNull EntityDataAccessor<?> pKey)
    {
        this.animationSyncedDataHandler(pKey);
        super.onSyncedDataUpdated(pKey);
    }

    @Override
    public EntityDataAccessor<DeterminedAnimation.AnimationPurpose> getAnimationStateDataAccessor ()
    {
        return DATA_STATE;
    }

    public int getAttackTick ()
    {
        return entityData.get(ATTACK_TICK);
    }

    public void setAttackTick (int value)
    {
        entityData.set(ATTACK_TICK, value);
    }

    public int getStompTick ()
    {
        return entityData.get(STOMP_TICK);
    }

    public void setStompTick (int value)
    {
        entityData.set(STOMP_TICK, value);
    }

    public int getBattleTick ()
    {
        return entityData.get(BATTLE_TICK);
    }

    public void setBattleTick (int value)
    {
        entityData.set(BATTLE_TICK, value);
    }

    public void incrementBattleTick ()
    {
        setBattleTick(getBattleTick() + 1);
    }

    public void incrementBattleTick (int value)
    {
        setBattleTick(getBattleTick() + value);
    }

    @Override
    public boolean doHurtTarget (@NotNull Entity target)
    {
        if (this.getAllAnimations().stream()
                .filter(determinedAnimation -> determinedAnimation.animationPurpose() != DeterminedAnimation.AnimationPurpose.IDLE)
                .anyMatch(determinedAnimation -> determinedAnimation.animationState().isStarted()))
            return false;
        this.runAnimationOf(DeterminedAnimation.AnimationPurpose.MAIN_ATTACK);
        this.setAttackTick(this.tickCount);
        return super.doHurtTarget(target);
    }

    @Override
    public boolean hurt (@NotNull DamageSource pSource, float pAmount)
    {
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void tick ()
    {
        super.tick();

        if (this.isDeadOrDying()) return;

        LivingEntity target = this.getTarget();
        if (target != null) incrementBattleTick();
        final int battleTick = getBattleTick();
        final int stompTick = getStompTick();
        final int attackTick = getAttackTick();

        if (attackTick != 0)
        {
            if (target != null)
                this.lookAt(target, 90, 90);
            if (this.tickCount >= attackTick + 12)
            {
                this.setAttackTick(0);
                this.runAnimationOf(DeterminedAnimation.AnimationPurpose.IDLE);
            } else if (this.tickCount >= attackTick + 7)
            {
            }
        }
    }

    @Override
    protected void registerGoals ()
    {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2f, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public @NotNull List<DeterminedAnimation> getAllAnimations ()
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
