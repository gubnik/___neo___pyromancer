package net.nikgub.pyromancer.entities.unburned;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.level.LevelReader;
import net.nikgub.pyromancer.PyromancerMod;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Unburned extends Monster {
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    /**
     * This state is used for a spawn animation.
     * Only done once in a constructor
     */
    public AnimationState emergeAnimationState = new AnimationState();
    /**
     * This state is used for a regular attack.
     * Performed each time this mob attacks
     */
    public AnimationState attackAnimationState = new AnimationState();
    /**
     * This state is used for a special attack.
     * Attack is performed when the mob is attacked too much (>40 damage dealt in 3 seconds)
     */
    public AnimationState kickAnimationState = new AnimationState();
    /**
     * This state is used for a special attack.
     * Attack is performed on a 6 seconds timer (120 ticks).
     */
    public AnimationState explosionAnimationState = new AnimationState();
    /**
     * This state is used for a special attack.
     * Attack is performed when unable to reach target
     */
    public AnimationState jumpingAnimationState = new AnimationState();
    public Unburned(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.emergeAnimationState.start(0);
    }
    public void handleEntityEvent(byte bt){
        switch ((int) bt)
        {
            case (61):
                this.stopAllAnimations();
                this.attackAnimationState.start(this.tickCount);
            case (62):
                this.stopAllAnimations();
                this.kickAnimationState.start(this.tickCount);
            case (63):
                this.stopAllAnimations();
                this.explosionAnimationState.start(this.tickCount);
            default:
                super.handleEntityEvent(bt);
        }
    }
    public void stopAllAnimations()
    {
        this.explosionAnimationState.stop();
        this.kickAnimationState.stop();
        this.attackAnimationState.stop();
    }
    public void tick(){
        super.tick();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }
    public boolean doHurtTarget(@NotNull Entity entity) {
        return super.doHurtTarget(entity);
    }
    public boolean hurt(@NotNull DamageSource source, float amount){
        if(!this.emergeAnimationState.isStarted()
        && !this.kickAnimationState.isStarted()
        && !this.explosionAnimationState.isStarted()) return false;
        if(amount > 10f && source.getDirectEntity() != null)
        {
            this.whenHurtTooMuch();
            amount = 10f;
        }
        return super.hurt(source, amount);
    }
    public void whenHurtTooMuch()
    {
        PyromancerMod.LOGGER.warn("My balls are in severe pain");
        this.level().broadcastEntityEvent(this, (byte) 62);
    }
    public boolean removeWhenFarAway(double p_219457_) {
        return false;
    }
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }
    public boolean checkSpawnObstruction(@NotNull LevelReader levelReader) {
        return super.checkSpawnObstruction(levelReader) && levelReader.noCollision(this, this.getType().getDimensions().makeBoundingBox(this.position()));
    }
    public static AttributeSupplier setAttributes(){
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500)
                .add(Attributes.MOVEMENT_SPEED, 0.35f)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.ATTACK_SPEED, 1)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1f)
                .build();
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new UnburnedAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }
    private static class UnburnedAttackGoal extends MeleeAttackGoal{
        public UnburnedAttackGoal(Unburned unburned){
            super(unburned, 1.2, true);
        }
        public boolean canUse(){
            if(!(this.mob instanceof Unburned unburned) || (!unburned.attackAnimationState.isStarted()
                    && !unburned.explosionAnimationState.isStarted() && !unburned.jumpingAnimationState.isStarted()
                    && !unburned.kickAnimationState.isStarted())){
                return super.canUse();
            } else {return false;}
        }
    }
}
