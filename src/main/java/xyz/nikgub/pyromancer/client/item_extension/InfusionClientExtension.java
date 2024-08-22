package xyz.nikgub.pyromancer.client.item_extension;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class InfusionClientExtension implements IClientItemExtensions
{
    public static HumanoidModel.ArmPose MAIN = HumanoidModel.ArmPose.create("infusion_item", true, (model, entity, arm) ->
    {
        int tick;
        if (entity instanceof Player player) tick = player.getUseItemRemainingTicks();
        else return;
        if (tick <= 0) return;
        ModelPart other = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
        ModelPart main = (arm.equals(HumanoidArm.RIGHT)) ? model.leftArm : model.rightArm;
        main.yRot = -0.8F;
        main.zRot = 0.5F;
        main.xRot = -0.97079635F;
        other.xRot = main.xRot;
        other.zRot = 1F;
        float f = (float) CrossbowItem.getChargeDuration(player.getUseItem());
        float f1 = Mth.clamp((float) player.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;
        other.xRot = Mth.lerp(-f2, other.xRot, (-(float) Math.PI / 2F));
    });

    @Override
    public HumanoidModel.ArmPose getArmPose (LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack)
    {
        return MAIN;
    }

    @Override
    public boolean applyForgeHandTransform (PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess)
    {
        if (player.getUseItemRemainingTicks() > 0)
        {
            this.applyItemArmTransform(poseStack, arm, swingProcess);
            return true;
        }
        return false;
    }

    private void applyItemArmTransform (PoseStack poseStack, HumanoidArm arm, float v)
    {
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate((float) i * 0.56F, -0.52F + v * -0.6F, -0.72F);
    }
}
