package xyz.nikgub.pyromancer.entities;

import org.jetbrains.annotations.ApiStatus;

/**
 * Interface to allow safe interference into entity's animations <p>
 * Right now I do not need it, but I might need it in the future
 */
@ApiStatus.Experimental
public interface ISafeAnimatedEntity {
    void stopAllAnimations();
}
