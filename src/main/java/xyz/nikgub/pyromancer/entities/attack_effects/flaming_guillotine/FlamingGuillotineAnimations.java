package xyz.nikgub.pyromancer.entities.attack_effects.flaming_guillotine;

import net.minecraft.client.animation.AnimationDefinition;
import xyz.nikgub.pyromancer.animations.from_text.PyranimParser;

public class FlamingGuillotineAnimations {
    public static final AnimationDefinition FALL = new PyranimParser("guillotine.pyranim").create();
            //= AnimationDefinition.Builder.withLength(0.5f)
            //.addAnimation("bone",
            //        new AnimationChannel(AnimationChannel.Targets.POSITION,
            //                new Keyframe(0f, KeyframeAnimations.posVec(0f, 36f, 0f),
            //                        AnimationChannel.Interpolations.CATMULLROM),
            //                new Keyframe(0.35f, KeyframeAnimations.posVec(0f, 42f, 0f),
            //                        AnimationChannel.Interpolations.CATMULLROM),
            //                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, -16f, 0f),
            //                        AnimationChannel.Interpolations.CATMULLROM)))
            //.addAnimation("bone",
            //        new AnimationChannel(AnimationChannel.Targets.ROTATION,
            //                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
            //                        AnimationChannel.Interpolations.CATMULLROM),
            //                new Keyframe(0.35f, KeyframeAnimations.degreeVec(0f, -15f, 0f),
            //                        AnimationChannel.Interpolations.CATMULLROM),
            //                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 360f, 0f),
            //                        AnimationChannel.Interpolations.CATMULLROM))).build();
}
