package xyz.nikgub.pyromancer.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.client.animations.DeterminedAnimation;
import xyz.nikgub.incandescent.client.animations.IAnimationPurposeEntity;
import xyz.nikgub.pyromancer.data.BlockTagDatagen;
import xyz.nikgub.pyromancer.registry.BlockRegistry;

import java.util.List;

public class ScorchEntity extends Monster implements IAnimationPurposeEntity
{
    public static final EntityDataSerializer<DeterminedAnimation.AnimationPurpose> ANIMATION_STATE = EntityDataSerializer.simpleEnum(DeterminedAnimation.AnimationPurpose.class);
    private static final EntityDataAccessor<DeterminedAnimation.AnimationPurpose> DATA_STATE = SynchedEntityData.defineId(ScorchEntity.class, ANIMATION_STATE);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(ScorchEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> POLLEN = SynchedEntityData.defineId(ScorchEntity.class, EntityDataSerializers.BOOLEAN);

    static
    {
        EntityDataSerializers.registerSerializer(ANIMATION_STATE);
    }

    public AnimationState ATTACK = new AnimationState();
    public AnimationState IDLE = new AnimationState();

    public ScorchEntity (EntityType<? extends Monster> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
        this.entityData.define(DATA_STATE, DeterminedAnimation.AnimationPurpose.IDLE);
        this.entityData.define(ATTACK_TICK, 0);
        this.entityData.define(POLLEN, false);
    }

    public static AttributeSupplier setAttributes ()
    {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.ATTACK_SPEED, 1)
                .add(Attributes.FOLLOW_RANGE, 8)
                .build();
    }

    public static boolean spawnPredicate (EntityType<?> entityType, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom)
    {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL && !pLevel.getBlockState(pPos.below()).is(BlockTags.LEAVES) && !pLevel.getBlockState(pPos.below()).is(BlockRegistry.PYROWOOD_LOG.get());
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

    public boolean hasPollen ()
    {
        return this.entityData.get(POLLEN);
    }

    public void setPollenState (boolean state)
    {
        this.entityData.set(POLLEN, state);
    }

    @Override
    protected void registerGoals ()
    {
        this.goalSelector.addGoal(0, new ScorchPollinateGoal(1));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2f, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public @NotNull List<DeterminedAnimation> getAllAnimations ()
    {
        return List.of(
                new DeterminedAnimation(ATTACK, DeterminedAnimation.AnimationPurpose.MAIN_ATTACK, (byte) 0, 0),
                new DeterminedAnimation(IDLE, DeterminedAnimation.AnimationPurpose.IDLE, (byte) 0, 0)
        );
    }

    public class ScorchPollinateGoal extends MoveToBlockGoal
    {

        public ScorchPollinateGoal (double pSpeedModifier)
        {
            super(ScorchEntity.this, pSpeedModifier, 16);
        }

        public boolean canUse ()
        {
            return !ScorchEntity.this.hasPollen() && this.findNearestBlock() && ScorchEntity.this.getTarget() == null;
        }

        public boolean canContinueToUse ()
        {
            return !ScorchEntity.this.hasPollen() && super.canContinueToUse() && ScorchEntity.this.getTarget() == null;
        }

        @Override
        protected boolean isValidTarget (@NotNull LevelReader pLevel, @NotNull BlockPos pPos)
        {
            return pLevel.getBlockState(pPos).is(BlockTagDatagen.FLAMING_GROVE_NATIVE);
        }

        @Override
        public void tick ()
        {
            super.tick();
            BlockPos blockpos = ScorchEntity.this.blockPosition();
            if (this.isReachedTarget())
                ScorchEntity.this.setPollenState(true);
        }
    }
}
