package net.nikgub.pyromancer.ember;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for overriding default mixin behaviour
 * Should be used for ensure proper behaviour with unique weaponry
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface UniqueEmberBehaviour {
    /**
     * @return      Modifier overriding default logic
     */
    AllowanceModifier allow();

    /**
     * @return      Additive modifier applied to the cost of one ember usage
     */
    int costModifier() default 1;
    enum AllowanceModifier
    {
        /**
         * Overriding default behaviour
         */
        DENY,
        ALLOW,
        NONE
    }
}
