package xyz.nikgub.pyromancer.client.item_extension;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
import xyz.nikgub.pyromancer.common.item.MusketItem;

public class MusketClientExtension implements IClientItemExtensions
{
    public static HumanoidModel.ArmPose MAIN = HumanoidModel.ArmPose.create("musket_loading", true, (model, entity, arm) ->
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

    public static HumanoidModel.ArmPose LOADED = HumanoidModel.ArmPose.create("musket_loaded", true, ((model, entity, arm) ->
    {
        ModelPart rightArm = model.rightArm;
        ModelPart leftArm = model.leftArm;
        boolean is_right_arm = arm == HumanoidArm.RIGHT;
        ModelPart modelpart = is_right_arm ? rightArm : leftArm;
        ModelPart modelpart1 = is_right_arm ? leftArm : rightArm;
        modelpart.yRot = (entity.isUsingItem()) ? 0 : is_right_arm ? -0.8F : 0.8F;
        modelpart.xRot = (entity.isUsingItem()) ? -Mth.PI / 2f : -0.97079635F;
        modelpart1.xRot = modelpart.xRot;
        modelpart1.yRot = Mth.PI / 4f;
    }));

    @Override
    public HumanoidModel.ArmPose getArmPose (LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack)
    {
        if (MusketItem.isLoaded(itemStack)) return LOADED;
        return MAIN;
    }

    @Override
    public boolean applyForgeHandTransform (PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess)
    {

        int ticks = player.getUseItemRemainingTicks();
        if (ticks <= 0 || player.getUseItem() != itemInHand) return false;
        if (MusketItem.isLoaded(itemInHand))
        {
            this.applyLoadedHandTransforms(poseStack, arm, ((72000 - ticks) / 20f + partialTick / 20f));
        } else
        {
            this.applyLoadingHandTransforms(poseStack, arm, swingProcess);
        }
        return true;
    }

    private void applyLoadingHandTransforms (PoseStack poseStack, HumanoidArm arm, float v)
    {
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.rotateAround(Axis.ZP.rotationDegrees(120 + (i == 1 ? 0 : 180)), (float) i * 0.56F, -0.52F + v * -0.6F, -0.72F);
        poseStack.rotateAround(Axis.YP.rotationDegrees(90 * i), (float) i * 0.56F, -0.52F + v * -0.6F, -0.72F);
        poseStack.translate((float) i * 0.56F, -0.52F + v * -0.6F, -0.72F);
    }

    private void applyLoadedHandTransforms (PoseStack poseStack, HumanoidArm arm, float v)
    {
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate((float) i * (0.5F + Mth.clamp(v, 0F, 1F) * -0.5F), -0.52F, -0.72F);
    }
}
