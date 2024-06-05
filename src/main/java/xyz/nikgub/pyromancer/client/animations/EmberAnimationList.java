package xyz.nikgub.pyromancer.client.animations;

import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import xyz.nikgub.pyromancer.common.ember.EmberAnimation;

public class EmberAnimationList {
    public static EmberAnimation SOULFLAME_IGNITION = new EmberAnimation(15, 10)
    {
        @Override
        public ThirdPersonAnimation thirdPersonAnimation ()
        {
            return (model, entity, arm) ->
            {
                float tick;
                if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
                else return;
                if(tick <= 0) return;
                //tick += Minecraft.getInstance().getPartialTick();
                ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
                armPart.xRot = (float) Math.PI - Mth.sin( (float) Math.PI * tick * 9 / 180) * -1 * Mth.cos( (float) Math.PI * tick * 9 / 180);
                armPart.zRot = -1 * Mth.cos( (float) Math.PI * tick * 9 / 180) / 5;
            };
        }

        @Override
        public FirstPersonAnimation firstPersonAnimation ()
        {
            return (poseStack, player, arm, itemStack, partialTick, equipProgress, swingProcess) ->
            {
                if (arm != HumanoidArm.RIGHT) return;
                poseStack.rotateAround(Axis.XN.rotationDegrees(partialTick * 5), 0, 0,0);
            };
        }
    };

    public static EmberAnimation PRESERVING_FLAME = new EmberAnimation(80, 40)
    {
        @Override
        public ThirdPersonAnimation thirdPersonAnimation ()
        {
            return (model, entity, arm) ->
            {
                boolean pRightHanded = arm == HumanoidArm.RIGHT;
                ModelPart pRightArm = model.rightArm;
                ModelPart pLeftArm = model.leftArm;
                ModelPart pHead = model.head;
                ModelPart modelpart = pRightHanded ? pRightArm : pLeftArm;
                ModelPart modelpart1 = pRightHanded ? pLeftArm : pRightArm;
                float dry = (float) Math.PI / 4;
                modelpart.yRot = (pRightHanded ? -dry : dry) + pHead.yRot;
                modelpart1.yRot = (pRightHanded ? dry : -dry) + pHead.yRot;
                modelpart.xRot = (-(float)Math.PI / 2F) + pHead.xRot + 0.1F;
                modelpart1.xRot = -1.5F + pHead.xRot;
            };
        }

        @Override
        public FirstPersonAnimation firstPersonAnimation ()
        {
            return (poseStack, player, arm, itemStack, partialTick, equipProgress, swingProcess) ->
            {
                if (arm != HumanoidArm.RIGHT) return;
                poseStack.rotateAround(Axis.XN.rotationDegrees(partialTick * 5), 0, 0,0);
            };
        }
    };
}
