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
import com.mojang.math.Axis;
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

public class CourtOfEmbersClientExtension implements IClientItemExtensions
{
    public static HumanoidModel.ArmPose MAIN = HumanoidModel.ArmPose.create("court_of_embers", false, (model, entity, arm) ->
    {
        int tick;
        if (entity instanceof Player player) tick = player.getUseItemRemainingTicks();
        else return;
        if (tick <= 0) return;
        ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
        armPart.xRot = (float) Math.PI - Mth.sin((float) Math.PI * tick * 18 / 180) * -1 * Mth.cos((float) Math.PI * tick * 9 / 180);
        armPart.zRot = -1 * Mth.cos((float) Math.PI * tick * 18 / 180) / 2;
    });

    @Override
    public HumanoidModel.ArmPose getArmPose (LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack)
    {
        return MAIN;
    }

    @Override
    public boolean applyForgeHandTransform (PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess)
    {
        if (player.getUseItemRemainingTicks() > 0 && player.getUseItem() == itemInHand)
        {
            this.applyItemArmTransform(poseStack, arm, equipProcess);
            return true;
        }
        return false;
    }

    private void applyItemArmTransform (PoseStack poseStack, HumanoidArm arm, float v)
    {
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        float f = Mth.clamp(v, 0F, 1F);
        poseStack.rotateAround(Axis.XN.rotation(f), 0, 0, 0);
        poseStack.translate((float) i * 0.5F + f * -0.5F, -f / 3, -0.72F);
    }
}
