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
        if (player.getUseItemRemainingTicks() > 0 && player.getUseItem() == itemInHand)
        {
            float remTicks = (float) player.getUseItemRemainingTicks();
            float useTicks = (float) itemInHand.getUseDuration();
            float f = (remTicks) / useTicks;
            this.applyItemArmTransform(poseStack, arm, f);
            return true;
        }
        return false;
    }

    private void applyItemArmTransform (PoseStack poseStack, HumanoidArm arm, float v)
    {
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        float f = Mth.clamp(v, 0F, 1F);
        poseStack.translate((float) -i * 0.56, -f / 3, -0.72F);
    }
}
