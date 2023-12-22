package net.nikgub.pyromancer.animations;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

public class AnimationList {
    public static EmberAnimation SOULFLAME_IGNITION = new EmberAnimation(
            (model, entity, arm) -> {
                    int tick;
                    if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
                    else return;
                    if(tick <= 0) return;
                    ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
                    armPart.xRot = (float) Math.PI - Mth.sin( (float) Math.PI * tick * 9 / 180) * -1 * Mth.cos( (float) Math.PI * tick * 9 / 180);
                    armPart.zRot = -1 * Mth.cos( (float) Math.PI * tick * 9 / 180) / 5;
                },
            poseStack -> {

            },
            10,
            40);
}
