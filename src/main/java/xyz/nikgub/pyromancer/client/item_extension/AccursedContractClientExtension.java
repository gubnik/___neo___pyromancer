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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class AccursedContractClientExtension implements IClientItemExtensions
{
    public static HumanoidModel.ArmPose MAIN = HumanoidModel.ArmPose.create("accursed_contract_item", false, (model, entity, arm) ->
    {
        int tick;
        if (entity instanceof Player player) tick = player.getUseItemRemainingTicks();
        else return;
        if (tick <= 0) return;
        ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
        armPart.xRot = Mth.HALF_PI + Mth.PI - Mth.sin(Mth.PI * tick * 9 / 180) * -0.75f * Mth.cos(Mth.PI * tick * 9 / 180);
        armPart.zRot = -0.75f * Mth.cos(Mth.PI * tick * 9 / 180) / 5;
        //armPart.z = -12f;
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
