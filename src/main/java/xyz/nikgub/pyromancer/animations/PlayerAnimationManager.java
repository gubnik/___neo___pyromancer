package xyz.nikgub.pyromancer.animations;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>How does it work?</h2>
 * <p>When you launch an animation via a method, AnimationInstance containing info about launched animation will be put into a hash map.</p>
 * <p>Then player's model will try to consume the animation for its player, making it null if successful.</p>
 * @author nikgub_
 */
@SuppressWarnings("unused")
public class PlayerAnimationManager {

    private static final Map<Player, AnimationInstance> runningAnimations = new HashMap<>();

    public static void launchAnimation(Player player, AnimationDefinition animationDefinition, boolean override)
    {
        PlayerAnimationManager.runningAnimations.put(player, new AnimationInstance(animationDefinition, override));
    }

    public static AnimationInstance consumeAnimationFor(Player player)
    {
        AnimationInstance instance = runningAnimations.get(player);
        runningAnimations.put(player, null);
        return instance;
    }

    public record AnimationInstance(AnimationDefinition animation, boolean override)
    {
    }
}
