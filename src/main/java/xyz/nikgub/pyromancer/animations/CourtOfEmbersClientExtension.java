package xyz.nikgub.pyromancer.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class CourtOfEmbersClientExtension implements IClientItemExtensions {
    public static HumanoidModel.ArmPose MAIN = HumanoidModel.ArmPose.create("court_of_embers", false, (model, entity, arm) -> {
        int tick;
        if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
        else return;
        if(tick <= 0) return;
        ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
        armPart.xRot = (float) Math.PI - Mth.sin( (float) Math.PI * tick * 18 / 180) * -1 * Mth.cos( (float) Math.PI * tick * 9 / 180);
        armPart.zRot = -1 * Mth.cos( (float) Math.PI * tick * 18 / 180) / 2;
    });
    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return MAIN;
    }
    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        if (player.getUseItemRemainingTicks() > 0) {
            this.applyItemArmTransform(poseStack, arm, swingProcess);
            return true;
        }
        return false;
    }
    private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float v) {
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate((float)i * 0.56F, -0.52F + v * -0.6F, -0.72F);
    }
}
