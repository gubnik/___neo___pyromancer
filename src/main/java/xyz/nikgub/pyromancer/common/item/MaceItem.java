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
package xyz.nikgub.pyromancer.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.item_interfaces.ICustomSwingItem;
import xyz.nikgub.pyromancer.common.enchantment.MaceEnchantment;
import xyz.nikgub.pyromancer.registry.EnchantmentRegistry;

import java.util.Map;

public class MaceItem extends TieredItem implements ICustomSwingItem
{
    public static final float DEFAULT_DAMAGE = 4f;

    public MaceItem (Tier tier, Properties properties)
    {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy (ItemStack pStack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker)
    {
        pStack.hurtAndBreak(1, pAttacker, (p_43296_) ->
        {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers (@NotNull final EquipmentSlot slot, final ItemStack itemStack)
    {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        if (slot == EquipmentSlot.MAINHAND)
        {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                this.getTier().getAttackDamageBonus() + DEFAULT_DAMAGE
                    + itemStack.getEnchantmentLevel(EnchantmentRegistry.CLOSE_QUARTERS.get()) * 0.6f,
                AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8d, AttributeModifier.Operation.ADDITION));
            Map<Enchantment, Integer> itemEnchants = itemStack.getAllEnchantments();
            for (Enchantment enchantment : itemEnchants.keySet().stream().toList())
            {
                if (enchantment instanceof MaceEnchantment maceEnchantment)
                {
                    for (Attribute attribute : maceEnchantment.getAttributes().keySet().stream().toList())
                    {
                        builder.put(attribute, maceEnchantment.getAttributes().get(attribute).apply(itemEnchants.get(enchantment)));
                    }
                }
            }
        }
        return builder.build();
    }

    @Override
    public boolean canAttackBlock (@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player pPlayer)
    {
        return !pPlayer.isCreative();
    }

    @Override
    public <T extends LivingEntity> void thirdPersonTransform (ItemStack itemStack, HumanoidModel<T> model, T entity, float ageInTicks)
    {
        if (!(model.attackTime <= 0.0F))
        {
            HumanoidArm arm = entity.swingingArm == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();
            ModelPart modelpart = arm == HumanoidArm.LEFT ? model.leftArm : model.rightArm;
            float f = model.attackTime;
            int i = arm == HumanoidArm.LEFT ? -1 : 1;
            model.body.yRot = Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
            if (arm == HumanoidArm.LEFT)
            {
                model.body.yRot *= -1.0F;
            }
            float move_multiplier = 5.0F;
            model.rightArm.z = Mth.sin(model.body.yRot) * move_multiplier;
            model.rightArm.x = -Mth.cos(model.body.yRot) * move_multiplier;
            model.leftArm.z = -Mth.sin(model.body.yRot) * move_multiplier;
            model.leftArm.x = Mth.cos(model.body.yRot) * move_multiplier;
            model.rightArm.yRot += model.body.yRot;
            model.leftArm.yRot += model.body.yRot;
            model.leftArm.xRot += model.body.yRot;
            f = 1.0F - model.attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            //float f2 = Mth.sin(model.attackTime * (float)Math.PI) * -(model.head.xRot - 0.7F) * 4F;
            modelpart.xRot += Mth.PI / 16;
            modelpart.xRot -= f1 * 2.25F;
            modelpart.yRot += model.head.xRot;
            modelpart.zRot = -Mth.HALF_PI * i;
            modelpart.zRot -= Mth.sin(model.attackTime * (float) Math.PI) * -0.4F;
        }
    }

    @Override
    public void firstPersonTransform (ItemStack itemStack, PoseStack poseStack, float swingProgress, float equippedProgress, boolean isRight)
    {
        final int doRotation = swingProgress > 0.0F ? 1 : 0;
        HumanoidArm arm = isRight ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
        final int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        final float f = Mth.sin(swingProgress * swingProgress * 3.1415927F);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) i * (45.0F + f * -20.0F)));
        final float f1 = Mth.sin(Mth.sqrt(swingProgress) * 3.1415927F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(i * -90 * doRotation + (float) i * f1 * -20.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees((float) i * -45.0F));
        poseStack.translate(0, 2.75F * doRotation, 0);
        final float scaleMod = doRotation == 0 ? 1F : 2.75F;
        poseStack.scale(scaleMod, scaleMod, scaleMod);
    }
}
