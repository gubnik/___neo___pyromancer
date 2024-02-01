package xyz.nikgub.pyromancer.animations;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.player.Player;

import java.util.Stack;

/**
 * <h1>How does it work?</h1>
 * <p>When you launch an animation via a method, AnimationInstance containing info about launched animation will be pushed onto a stack.</p>
 * <p>Then player's model will try to consume the animation on top of the stack for its player, popping it if successful.
 * Thus, stack acts more like a fool-proof impulse signal, where animations for different players will have next to no delay in playing</p>
 * <p>Due to the tick-based nature of animation handling there is no situation when this fails, unless specifically meant to be failed (launched for fake player, launched with null animation etc.)</p>
 * @author nikgub_
 */
@SuppressWarnings("unused")
public class PlayerAnimationManager {

    private static final Stack<AnimationInstance> runningAnimations = new Stack<>();

    public static boolean isIdle()
    {
        return runningAnimations.empty();
    }

    public static void launchAnimation(Player player, AnimationDefinition animationDefinition, boolean override)
    {
        PlayerAnimationManager.runningAnimations.push(new AnimationInstance(player, animationDefinition, override));
    }

    public static AnimationInstance consumeAnimationFor(Player player)
    {
        if(isIdle()) return null;
        if(!(runningAnimations.peek().player.equals(player))) return null;
        else return runningAnimations.pop();
    }

    public record AnimationInstance(Player player, AnimationDefinition animation, boolean override)
    {
    }
}
