package xyz.nikgub.pyromancer.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import xyz.nikgub.pyromancer.registry.BlockRegistry;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * Expected behaviour:
 * - wander around (no water)
 * - gets hit ->
 * ? pyroent is nearby -> run and alert
 * ! just run
 */
public class PyracornEntity extends Monster
{

    public PyracornEntity (EntityType<? extends Monster> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    public static boolean spawnPredicate (EntityType<?> entityType, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom)
    {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL && !pLevel.getBlockState(pPos.below()).is(BlockTags.LEAVES) && !pLevel.getBlockState(pPos.below()).is(BlockRegistry.PYROWOOD_LOG.get());
    }

    public static AttributeSupplier setAttributes ()
    {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .build();
    }

    @Override
    protected void registerGoals ()
    {
        this.goalSelector.addGoal(0, new RunToBigEntsGoal(1.5, 1, 16));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    public class RunToBigEntsGoal extends Goal
    {
        private final PyracornEntity pyracorn;
        private final Predicate<Mob> followPredicate;
        private final double speedModifier;
        private final PathNavigation navigation;
        private final float stopDistance;
        private final float areaSize;
        @Nullable
        private PyroentEntity mobToFollow;
        private int timeToRecalcPath;
        private float oldWaterCost;
        private LivingEntity attacker;

        public RunToBigEntsGoal (double pSpeedModifier, float pStopDistance, float pAreaSize)
        {
            this.pyracorn = PyracornEntity.this;
            this.followPredicate = (p_25278_) ->
            {
                return p_25278_ != null && pyracorn.getClass() != p_25278_.getClass();
            };
            this.speedModifier = pSpeedModifier;
            this.navigation = pyracorn.getNavigation();
            this.stopDistance = pStopDistance;
            this.areaSize = pAreaSize;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        protected boolean shouldPanic ()
        {
            this.attacker = this.pyracorn.getLastHurtByMob();
            return this.attacker != null || this.pyracorn.isFreezing() || this.pyracorn.isOnFire();
        }

        public boolean canUse ()
        {
            if (!this.shouldPanic())
            {
                return false;
            }
            List<PyroentEntity> list = this.pyracorn.level().getEntitiesOfClass(PyroentEntity.class, this.pyracorn.getBoundingBox().inflate(this.areaSize), this.followPredicate);
            if (!list.isEmpty())
            {
                for (PyroentEntity pyroent : list)
                {
                    if (!pyroent.isInvisible())
                    {
                        this.mobToFollow = pyroent;
                        return true;
                    }
                }
            }

            return false;
        }

        public boolean canContinueToUse ()
        {
            return this.mobToFollow != null && !this.navigation.isDone() && this.pyracorn.distanceToSqr(this.mobToFollow) > (double) (this.stopDistance * this.stopDistance);
        }

        public void start ()
        {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.pyracorn.getPathfindingMalus(BlockPathTypes.WATER);
            this.pyracorn.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        }

        public void stop ()
        {
            if (this.attacker != null && this.mobToFollow != null)
            {
                this.mobToFollow.alert(this.attacker);
            }
            this.attacker = null;
            this.mobToFollow = null;
            this.navigation.stop();
            this.pyracorn.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
        }

        public void tick ()
        {
            if (this.mobToFollow != null && !this.pyracorn.isLeashed())
            {
                this.pyracorn.getLookControl().setLookAt(this.mobToFollow, 10.0F, (float) this.pyracorn.getMaxHeadXRot());

                if (--this.timeToRecalcPath > 0) return;

                this.timeToRecalcPath = this.adjustedTickDelay(10);
                double d0 = this.pyracorn.getX() - this.mobToFollow.getX();
                double d1 = this.pyracorn.getY() - this.mobToFollow.getY();
                double d2 = this.pyracorn.getZ() - this.mobToFollow.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (!(d3 <= (double) (this.stopDistance * this.stopDistance)))
                {
                    this.navigation.moveTo(this.mobToFollow, this.speedModifier);
                } else
                {
                    this.navigation.stop();
                    LookControl lookcontrol = this.mobToFollow.getLookControl();
                    if (d3 <= (double) this.stopDistance || lookcontrol.getWantedX() == this.pyracorn.getX() && lookcontrol.getWantedY() == this.pyracorn.getY() && lookcontrol.getWantedZ() == this.pyracorn.getZ())
                    {
                        double d4 = this.mobToFollow.getX() - this.pyracorn.getX();
                        double d5 = this.mobToFollow.getZ() - this.pyracorn.getZ();
                        this.navigation.moveTo(this.pyracorn.getX() - d4, this.pyracorn.getY(), this.pyracorn.getZ() - d5, this.speedModifier);
                    }
                }
            }
        }
    }
}
