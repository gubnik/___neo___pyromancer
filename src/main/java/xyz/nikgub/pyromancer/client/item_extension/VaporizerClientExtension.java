/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, Nikolay Gubankov (aka nikgub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package xyz.nikgub.pyromancer.client.item_extension;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class VaporizerClientExtension implements IClientItemExtensions
{

    public static HumanoidModel.ArmPose MAIN = HumanoidModel.ArmPose.create("musket_loaded", true, ((model, entity, arm) ->
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
        return MAIN;
    }

    @Override
    public boolean applyForgeHandTransform (PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess)
    {

        int ticks = player.getUseItemRemainingTicks();
        if (ticks <= 0 || player.getUseItem() != itemInHand) return false;
        this.applyLoadedHandTransforms(poseStack, arm, ((72000 - ticks) / 20f + partialTick / 20f));
        return true;
    }

    private void applyLoadedHandTransforms (PoseStack poseStack, HumanoidArm arm, float v)
    {
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate((float) i * (0.5F + Mth.clamp(v, 0F, 1F) * -0.5F), -0.52F, -0.72F);
    }
}
