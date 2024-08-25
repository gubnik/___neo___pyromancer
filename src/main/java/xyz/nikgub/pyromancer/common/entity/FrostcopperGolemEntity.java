package xyz.nikgub.pyromancer.common.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.client.animations.DeterminedAnimation;
import xyz.nikgub.incandescent.client.animations.IAnimationPurposeEntity;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.registries.AttributeRegistry;
import xyz.nikgub.pyromancer.registries.DamageSourceRegistry;

import java.util.List;

public class FrostcopperGolemEntity extends Monster implements IAnimationPurposeEntity
{
    public static final EntityDataSerializer<DeterminedAnimation.AnimationPurpose> FROSTCOPPER_GOLEM_STATE = EntityDataSerializer.simpleEnum(DeterminedAnimation.AnimationPurpose.class);
    private static final EntityDataAccessor<DeterminedAnimation.AnimationPurpose> DATA_STATE = SynchedEntityData.defineId(FrostcopperGolemEntity.class, FROSTCOPPER_GOLEM_STATE);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(FrostcopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STOMP_TICK = SynchedEntityData.defineId(FrostcopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STOMP_BUILDUP_COUNTER = SynchedEntityData.defineId(FrostcopperGolemEntity.class, EntityDataSerializers.INT);

    static
    {
        EntityDataSerializers.registerSerializer(FROSTCOPPER_GOLEM_STATE);
    }

    public AnimationState IDLE = new AnimationState();
    public AnimationState ATTACK = new AnimationState();
    public AnimationState STOMP = new AnimationState();

    public FrostcopperGolemEntity (EntityType<? extends Monster> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
        this.entityData.define(DATA_STATE, DeterminedAnimation.AnimationPurpose.IDLE);
        this.entityData.define(ATTACK_TICK, 0);
        this.entityData.define(STOMP_TICK, 0);
        this.entityData.define(STOMP_BUILDUP_COUNTER, 0);
    }

    public static AttributeSupplier setAttributes ()
    {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ATTACK_SPEED, 1)
                .add(Attributes.ATTACK_KNOCKBACK, 0.4)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25f)
                .add(AttributeRegistry.COLD_BUILDUP.get(), 16)
                .build();
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

    @Override
    public EntityDataAccessor<DeterminedAnimation.AnimationPurpose> getAnimationStateDataAccessor ()
    {
        return DATA_STATE;
    }

    public int getStompBuildupCounter ()
    {
        return entityData.get(STOMP_BUILDUP_COUNTER);
    }

    public void setStompBuildupCounter (int value)
    {
        entityData.set(STOMP_BUILDUP_COUNTER, value);
    }

    public void incrementStompBuildupCounter ()
    {
        setStompBuildupCounter(getStompBuildupCounter() + 1);
    }

    public void incrementStompBuildupCounter (int value)
    {
        setStompBuildupCounter(getStompBuildupCounter() + value);
    }

    @Override
    public DeterminedAnimation.AnimationPurpose getState ()
    {
        return this.getEntityData().get(DATA_STATE);
    }

    @Override
    public void setState (DeterminedAnimation.AnimationPurpose state)
    {
        this.getEntityData().set(DATA_STATE, state);
    }

    @Override
    public void onSyncedDataUpdated (@NotNull EntityDataAccessor<?> pKey)
    {
        this.animationSyncedDataHandler(pKey);
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
        //if (pSource.getEntity() instanceof LivingEntity livingEntity && livingEntity.position().distanceTo(this.position()) > 7)
        //{
        //    if (this.stompTick == 0) this.runAnimationOf(DeterminedAnimation.AnimationPurpose.STOMP);
        //    stompAttack(pAmount);
        //    this.stompTick = tickCount;
        //    return false;
        //}
        boolean retVal = super.hurt(pSource, pAmount);
        if (retVal) this.incrementStompBuildupCounter((int) pAmount * 4);
        return retVal && getStompTick() == 0;
    }

    private void stompAttack (float pAmount)
    {
        final int areaSize = ((int) (pAmount / 5) + 2);
        final Vec3 initPos = this.getOnPos().getCenter();
        final Level level = this.level();
        if (!(level instanceof ServerLevel serverLevel)) return;
        for (LivingEntity target : EntityUtils.entityCollector(initPos, areaSize, serverLevel))
        {
            if (!target.canFreeze()) continue;
            if (!target.onGround()) continue;
            target.hurt(DamageSourceRegistry.frostcopperGolemStomp(this), pAmount);
            //target.addDeltaMovement(this.position().subtract(target.position()));
        }
        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, initPos.x, initPos.y + 1, initPos.z, areaSize * areaSize * areaSize * 20, areaSize, 2, areaSize, 0.5D);
    }

    @Override
    public void tick ()
    {
        super.tick();
        if (this.isDeadOrDying()) return;
        LivingEntity target = this.getTarget();
        final int stompTick = getStompTick();
        final int attackTick = getAttackTick();
        if (target != null)
        {
            this.incrementStompBuildupCounter();
        }
        if (target != null && stompTick == 0 && this.getStompBuildupCounter() % 200 >= 155)
        {
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.STOMP);
            this.setStompTick(tickCount);
        }

        if (attackTick != 0 && this.tickCount >= attackTick + 10)
        {
            this.setAttackTick(0);
            this.runAnimationOf(DeterminedAnimation.AnimationPurpose.IDLE);
        }
        if (stompTick != 0)
        {
            if (this.tickCount >= stompTick + 15)
            {
                this.setStompTick(0);
                this.runAnimationOf(DeterminedAnimation.AnimationPurpose.IDLE);
            } else if (this.tickCount >= stompTick + 13)
            {
                stompAttack(5);
            }
        }
    }

    @Override
    public boolean canFreeze ()
    {
        return false;
    }

    @Override
    public @NotNull List<DeterminedAnimation> getAllAnimations ()
    {
        return List.of(
                new DeterminedAnimation(ATTACK, DeterminedAnimation.AnimationPurpose.MAIN_ATTACK, (byte) 73, 0),
                new DeterminedAnimation(STOMP, DeterminedAnimation.AnimationPurpose.STOMP, (byte) 74, 10),
                new DeterminedAnimation(IDLE, DeterminedAnimation.AnimationPurpose.IDLE, (byte) 75, 0)
        );
    }
}