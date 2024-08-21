package xyz.nikgub.pyromancer.client.animation;

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
        public ThirdPersonAnimation getThirdPersonAnimation()
        {
            return (model, entity, arm) ->
            {
                float tick;
                if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
                else return;
                if(tick <= 0) return;
                final float anglePerTick = 12;
                ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
                armPart.yRot = - (float) Mth.HALF_PI + Mth.sin( (float) Math.PI * tick * anglePerTick / 2 * 180);
                armPart.xRot = - (float) Mth.HALF_PI / 2 - Mth.cos( (float) Math.PI * tick * anglePerTick / 180) / 3;
            };
        }

        @Override
        public FirstPersonAnimation getFirstPersonAnimation()
        {
            return (poseStack, player, arm, itemStack, partialTick, equipProgress, swingProcess) ->
            {
                int i = (arm == HumanoidArm.RIGHT) ? 1 : -1;
                poseStack.rotateAround(Axis.XN.rotationDegrees(45 - Mth.sin(partialTick)), (float)i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
            };
        }
    };

    public static EmberAnimation PRESERVING_FLAME = new EmberAnimation(80, 40)
    {
        @Override
        public ThirdPersonAnimation getThirdPersonAnimation()
        {
            return (model, entity, arm) ->
            {
                boolean pRightHanded = arm == HumanoidArm.RIGHT;
                ModelPart pRightArm = model.rightArm;
                ModelPart pLeftArm = model.leftArm;
                ModelPart pHead = model.head;
                ModelPart main = pRightHanded ? pRightArm : pLeftArm;
                ModelPart secondary = pRightHanded ? pLeftArm : pRightArm;
                float dry = Mth.HALF_PI / 2;
                main.yRot = (pRightHanded ? -dry : dry) + pHead.yRot;
                secondary.yRot = (pRightHanded ? dry : -dry) + pHead.yRot;
                main.xRot = (-(float)Mth.HALF_PI) + pHead.xRot + 0.1F;
                secondary.xRot = -1.5F + pHead.xRot;
            };
        }

        @Override
        public FirstPersonAnimation getFirstPersonAnimation()
        {
            return (poseStack, player, arm, itemStack, partialTick, equipProgress, swingProcess) ->
            {
                poseStack.rotateAround(Axis.YN.rotationDegrees(-90), 0, 0,0);
                poseStack.rotateAround(Axis.XN.rotationDegrees(45), 0, 0,0);
            };
        }
    };

    public static EmberAnimation EXECUTIONERS_FIRE = new EmberAnimation(20, 40)
    {
        @Override
        public ThirdPersonAnimation getThirdPersonAnimation()
        {
            return (model, entity, arm) ->
            {
                float tick = entity.getUseItemRemainingTicks();// + Minecraft.getInstance().getPartialTick();
                boolean pRightHanded = arm == HumanoidArm.RIGHT;
                final ModelPart pRightArm = model.rightArm;
                final ModelPart pLeftArm = model.leftArm;
                final ModelPart main = pRightHanded ? pRightArm : pLeftArm;
                main.xRot = - Mth.HALF_PI;
                float dry = 2.5f * (Mth.sqrt(tick / getUseTime()) - 0.5f);
                // float dy = 40 * Mth.sqrt(tick / getUseTime());
                main.yRot = Mth.HALF_PI * dry;
                main.zRot = Mth.HALF_PI;
            };
        }

        @Override
        public FirstPersonAnimation getFirstPersonAnimation()
        {
            return (poseStack, player, arm, itemStack, partialTick, equipProgress, swingProcess) ->
            {
                float tick = player.getUseItemRemainingTicks();
                poseStack.rotateAround(Axis.YN.rotationDegrees(-90), 0, 0,0);
                poseStack.rotateAround(Axis.XN.rotationDegrees(45f - 90f * tick / getUseTime()), 0f, 0f, 0f);
            };
        }
    };
}
