package xyz.nikgub.pyromancer.common.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.client.animations.DeterminedAnimation;
import xyz.nikgub.incandescent.client.animations.IAnimationPurposeEntity;

import java.util.List;

public class RimegazerEntity extends Monster implements IAnimationPurposeEntity
{
    public enum AttackState
    {
        ATTACK,
        SPIN,
    }

    public static final EntityDataSerializer<AttackState> ATTACK_STATE_SERIALIZER = EntityDataSerializer.simpleEnum(AttackState.class);
    private static final EntityDataAccessor<DeterminedAnimation.AnimationPurpose> ANIMATION_STATE = SynchedEntityData.defineId(RimegazerEntity.class, DeterminedAnimation.ANIMATION_SERIALIZER);
    private static final EntityDataAccessor<AttackState> ATTACK_STATE = SynchedEntityData.defineId(RimegazerEntity.class, ATTACK_STATE_SERIALIZER);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(RimegazerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SPIN_TICK = SynchedEntityData.defineId(RimegazerEntity.class, EntityDataSerializers.INT);

    static
    {
        EntityDataSerializers.registerSerializer(ATTACK_STATE_SERIALIZER);
    }

    public AnimationState IDLE = new AnimationState();
    public AnimationState ATTACK = new AnimationState();
    public AnimationState SPIN = new AnimationState();

    public RimegazerEntity (EntityType<? extends Monster> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
        this.entityData.define(ANIMATION_STATE, DeterminedAnimation.AnimationPurpose.IDLE);
        this.entityData.define(ATTACK_STATE, AttackState.ATTACK);
        this.entityData.define(ATTACK_TICK, 0);
        this.entityData.define(SPIN_TICK, 0);
    }

    public static AttributeSupplier setAttributes ()
    {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 75)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.ATTACK_SPEED, 1)
                .add(Attributes.FOLLOW_RANGE, 8)
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
        return ANIMATION_STATE;
    }

    public AttackState getAttackState()
    {
        return this.entityData.get(ATTACK_STATE);
    }

    public void setAttackState(AttackState attackState)
    {
        this.entityData.set(ATTACK_STATE, attackState);
    }

    public int getAttackTick ()
    {
        return entityData.get(ATTACK_TICK);
    }

    public void setAttackTick (int value)
    {
        entityData.set(ATTACK_TICK, value);
    }

    public int getSpinTick ()
    {
        return entityData.get(SPIN_TICK);
    }

    public void setSpinTick (int value)
    {
        entityData.set(SPIN_TICK, value);
    }

    @Override
    public boolean doHurtTarget (@NotNull Entity target)
    {
        if (this.getAllAnimations().stream()
                .filter(determinedAnimation -> determinedAnimation.animationPurpose() != DeterminedAnimation.AnimationPurpose.IDLE)
                .anyMatch(determinedAnimation -> determinedAnimation.animationState().isStarted()))
            return false;
        if (this.getAttackState() == AttackState.ATTACK)
        {
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.MAIN_ATTACK);
            this.setAttackTick(this.tickCount);
        }
        else
        {
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.SPECIAL_ATTACK);
            this.setSpinTick(this.tickCount);
        }
        return super.doHurtTarget(target);
    }

    @Override
    protected void registerGoals ()
    {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2.2f, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick ()
    {
        super.tick();

        if (this.isDeadOrDying()) return;

        LivingEntity target = this.getTarget();
        if (this.getAttackState() == AttackState.ATTACK)
        {
            final int attackTick = getAttackTick();

            if (attackTick != 0)
            {
                if (target != null)
                {
                    this.lookAt(target, 90, 90);
                }
                if (this.tickCount >= attackTick + 10)
                {
                    this.setAttackTick(0);
                    this.runAnimationOf(DeterminedAnimation.AnimationPurpose.IDLE);
                    this.setAttackState(AttackState.SPIN);
                } else if (this.tickCount >= attackTick + 8)
                {
                    this.rayAttack(5);
                }
            }
        }
        else
        {
            final int spinTick = getSpinTick();

            if (spinTick != 0)
            {
                if (target != null)
                {
                    this.lookAt(target, 90, 90);
                }
                if (this.tickCount >= spinTick + 34)
                {
                    this.setSpinTick(0);
                    this.runAnimationOf(DeterminedAnimation.AnimationPurpose.IDLE);
                    this.setAttackState(AttackState.ATTACK);
                } else if (this.tickCount >= 8 && this.tickCount % 8 == 7)
                {
                    this.rayAttack(5);
                }
            }
        }

    }

    private void rayAttack (float damage)
    {
        final double x = this.getX();
        final double y = this.getY() + 1;
        final double z = this.getZ();
        final Vec3 angles = this.getLookAngle();
        final int iterations = 20;
        if (!(level() instanceof ServerLevel level)) return;
        for (int i = 0; i < iterations; i++)
        {
            level.sendParticles(ParticleTypes.CRIT, x + angles.x * (0.3 + (double) i / 5), y + angles.y * ((double) i / 5), z + angles.z * (0.3 + (double) i / 5), 2, 0, 0, 0, 0.0f);
        }
    }

    @Override
    public @NotNull List<DeterminedAnimation> getAllAnimations ()
    {
        return List.of(
                new DeterminedAnimation(IDLE, DeterminedAnimation.AnimationPurpose.IDLE),
                new DeterminedAnimation(ATTACK, DeterminedAnimation.AnimationPurpose.MAIN_ATTACK),
                new DeterminedAnimation(SPIN, DeterminedAnimation.AnimationPurpose.SPECIAL_ATTACK)
        );
    }
}
