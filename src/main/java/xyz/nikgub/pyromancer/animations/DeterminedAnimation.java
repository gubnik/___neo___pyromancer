package xyz.nikgub.pyromancer.animations;

import net.minecraft.world.entity.AnimationState;

/**
 * Record used by {@link xyz.nikgub.pyromancer.entities.ISafeAnimatedEntity}
 * @param animationState            AnimationState of an entity
 * @param animationPurpose          Type of animation, must be declared properly for generalized behaviour
 * @param signal                    Byte value of a signal used by Entity.handleEntityEvent(byte)
 * @param localPriority             Animations with higher priority are prioritized, also acts as an id.<p>
 *                                  It's recommended to not have duplicate priorities to avoid possible uncertainties
 */
public record DeterminedAnimation(AnimationState animationState, AnimationPurpose animationPurpose, byte signal, int localPriority) {

    public enum AnimationPurpose
    {
        MAIN_ATTACK,
        SPECIAL_ATTACK,
        HURT,
        SPECIAL_HURT,
        SPAWN,
        DEATH,
        MISC
    }
}
