package xyz.nikgub.pyromancer.common.entities;

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

public class UnburnedEntity extends Monster implements IFlamingGroveNativeEntity, IAnimationPurposeEntity {
    /**
     * Responsible for animating main attack
     */
    public AnimationState    ATTACK = new AnimationState();
    /**
     * Responsible for animating counter-shield attack
     */
    public AnimationState      KICK = new AnimationState();
    /**
     * Responsible for animating timed explosion
     */
    public AnimationState EXPLOSION = new AnimationState();
    /**
     * Responsible for animating spawn animation
     */
    public AnimationState    EMERGE = new AnimationState();

    /**
     * When Unburned attacks, it set this field to current tickCount <p>
     * When attackTimeOffset ticks pass i.e. tickCount >= attackTick + attackTimeOffset the mob actually deals damage
     */
    private int attackTick = 0;
    private static final int attackTimeOffset = 6;

    private int explosionTick = 0;
    private static final int explosionTickOffset = 8;
    private static final int explosionTickCooldown = 200;
    private boolean timedExplosionReady = false;

    public UnburnedEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void handleEntityEvent(byte b)
    {
        // <3 the byte event thing, truly genius idea on Mojang's part
        this.safelyHandleAnimations(b);
    }

    @Override
    public void tick()
    {
        // I might rethink this, not sure yet
        this.setInvulnerable(this.EXPLOSION.isStarted() || this.EMERGE.isStarted());

        super.tick();
        if(this.getTarget() == null) return;

        /*
        This part creates an additional explosion after attack when the time is right
        I pray to God it doesn't fail
         */
        if(this.attackTick != 0 && this.tickCount >= this.attackTick + attackTimeOffset)
        {
            this.level().explode(this, DamageSourceRegistry.unburnedExplosion(this), null,
                    this.getX() + this.getLookAngle().x*2,
                    this.getY() + 2,
                    this.getZ() + this.getLookAngle().z*2,
                    0.8f, true, Level.ExplosionInteraction.NONE);
            this.attackTick = 0;
            super.tick();
            return;
        }

        /*
        TODO: find a way to not use timedExplosionReady, I don't like this implementation

        Basically, we have explosionTick, every so often it broadcasts an event
        Then it's being set to tickCount while setting timedExplosionReady to true
        If timedExplosionReady is indeed true then after %appropriate_animation_delay% the actual attack happens
        After the attack, the cycle continues
         */
        if(this.tickCount >= this.explosionTick + explosionTickCooldown)
        {
            this.runAnimationByState(EXPLOSION, true);
            this.explosionTick = this.tickCount;
            this.timedExplosionReady = true;
        }
        if(this.tickCount >= this.explosionTick + explosionTickOffset && this.timedExplosionReady)
        {
            this.explosionAttack();
            this.explosionTick += explosionTickCooldown - explosionTickOffset;
            this.timedExplosionReady = false;
        }
    }

    private void explosionAttack()
    {
        // TODO: Make a proper attack, right now it's a placeholder
        this.level().explode(this, DamageSourceRegistry.unburnedExplosion(this), null,
                this.getX() + this.getLookAngle().x*2,
                this.getY() + 2,
                this.getZ() + this.getLookAngle().z*2,
                3f, true, Level.ExplosionInteraction.NONE);
    }

    public boolean doHurtTarget(@NotNull Entity target)
    {
        /*
        I hate stupid shield so enforce suffering on shield users
        If a player (or a living entity to be precise) blocks, Unburned yeets them into the air + cooldowns the shield
        TODO: make a proper yeeting mechanism, this one doesn't work very well
         */
        if(target instanceof LivingEntity entity && entity.isBlocking())
        {
            if(entity instanceof Player player) player.getCooldowns().addCooldown(Items.SHIELD, 20);
            entity.stopUsingItem();
            entity.setDeltaMovement(this.getLookAngle().x(), 1, this.getLookAngle().z());
            this.runAnimationByState(KICK, true);
            return super.doHurtTarget(entity);
        }
        this.runAnimationByState(ATTACK, false);
        this.attackTick = this.tickCount;
        return super.doHurtTarget(target);
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float amount)
    {
        /* placeholder for the case of me wanting to add specific immunities */
        // if(damageSource.is(DamageTypeTags.IS_FIRE)) return false;

        if(amount > 10f)
        {
            this.stopAllAnimations();
            this.runAnimationByState(KICK, true);
            return super.hurt(damageSource, 10F + (float) Math.log(10 - amount));
        }
        return super.hurt(damageSource, amount);
    }

    // One attribute supplier to rule them all
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
    public @NotNull List<DeterminedAnimation> getAllAnimations() {
        return List.of(
                new DeterminedAnimation(this.ATTACK, DeterminedAnimation.AnimationPurpose.MAIN_ATTACK      , (byte) 70, 0),
                new DeterminedAnimation(this.EXPLOSION, DeterminedAnimation.AnimationPurpose.SPECIAL_ATTACK, (byte) 71, 2),
                new DeterminedAnimation(this.KICK, DeterminedAnimation.AnimationPurpose.SPECIAL_ATTACK     , (byte) 72, 1),
                new DeterminedAnimation(this.EMERGE, DeterminedAnimation.AnimationPurpose.SPAWN            , (byte) 73, 0)
        );
    }
}
