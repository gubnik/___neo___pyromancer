package xyz.nikgub.pyromancer.common.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.client.animations.DeterminedAnimation;
import xyz.nikgub.incandescent.client.animations.IAnimationPurposeEntity;

import java.util.List;

public class ScorchEntity extends Monster implements IFlamingGroveNativeEntity, IAnimationPurposeEntity
{
    public static final EntityDataSerializer<DeterminedAnimation.AnimationPurpose> ANIMATION_STATE = EntityDataSerializer.simpleEnum(DeterminedAnimation.AnimationPurpose.class);
    private static final EntityDataAccessor<DeterminedAnimation.AnimationPurpose> DATA_STATE = SynchedEntityData.defineId(ScorchEntity.class, ANIMATION_STATE);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(ScorchEntity.class, EntityDataSerializers.INT);

    static
    {
        EntityDataSerializers.registerSerializer(ANIMATION_STATE);
    }

    public AnimationState ATTACK = new AnimationState();
    public AnimationState IDLE = new AnimationState();

    protected ScorchEntity (EntityType<? extends Monster> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    @Override
    public EntityDataAccessor<DeterminedAnimation.AnimationPurpose> getAnimationStateDataAccessor ()
    {
        return DATA_STATE;
    }

    @Override
    public @NotNull List<DeterminedAnimation> getAllAnimations ()
    {
        return List.of(
                new DeterminedAnimation(ATTACK, DeterminedAnimation.AnimationPurpose.MAIN_ATTACK, (byte) 0, 0),
                new DeterminedAnimation(IDLE, DeterminedAnimation.AnimationPurpose.IDLE, (byte) 0, 0)
        );
    }
}
