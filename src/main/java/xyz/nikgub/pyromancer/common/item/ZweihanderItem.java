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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.item_interfaces.IBetterAttributeTooltipItem;
import xyz.nikgub.incandescent.common.item_interfaces.ICustomSwingItem;
import xyz.nikgub.incandescent.common.item_interfaces.IExtensibleTooltipItem;
import xyz.nikgub.incandescent.util.Hypermap;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.registry.EnchantmentRegistry;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ZweihanderItem extends Item implements IBetterAttributeTooltipItem, ICustomSwingItem, IExtensibleTooltipItem
{

    public static final Set<ToolAction> ACTIONS = Set.of(ToolActions.SWORD_SWEEP);
    private static final UUID REACH_UUID = UUID.fromString("ba808878-e417-4af3-9987-630bbcbd120f");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("ee28a837-0676-47a1-9e2a-8de73c49eb8c");
    private static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("a11982cc-39c5-4b6f-8bff-303a7c75deda");

    public static final String ACTION_TAG = "__ZWEIHANDER_ACTION__";

    public ZweihanderItem (Properties properties)
    {
        super(properties.stacksTo(1).defaultDurability(1500));
    }

    @Override
    public boolean isDamageable (ItemStack itemStack)
    {
        return true;
    }

    @Override
    public boolean isEnchantable (@NotNull ItemStack itemStack)
    {
        return true;
    }

    @Override
    public int getEnchantmentValue (ItemStack itemStack)
    {
        return 15;
    }

    @Override
    public void inventoryTick (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int tick, boolean b)
    {
        if (!(entity instanceof Player player)) return;
        if (player.getMainHandItem() != itemStack)
        {
            CompoundTag tag = itemStack.getOrCreateTag();
            if (tag.getBoolean("ProperSwing")) tag.putBoolean("ProperSwing", false);
            return;

        }
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.getBoolean("ProperSwing"))
        {
            if (player.getAttackStrengthScale(0) == 1)
            {
                tag.putBoolean("ProperSwing", false);
            }
        }
        if (player.swingTime > 0) return;
        if (player.isCrouching() && player.getMainHandItem() == itemStack)
        {
            tag.putInt(ACTION_TAG, 1);

        }
        else if (player.isSprinting() && player.getMainHandItem() == itemStack)
        {
            tag.putInt(ACTION_TAG, 2);
        }
        else
        {
            tag.putInt(ACTION_TAG, 0);
        }
    }

    @Override
    public boolean hurtEnemy (@NotNull ItemStack itemStack, @NotNull LivingEntity target, @NotNull LivingEntity source)
    {
        double mod = Math.abs(target.getDeltaMovement().length() + source.getDeltaMovement().length()) * itemStack.getEnchantmentLevel(EnchantmentRegistry.WEIGHT.get()) * 0.35f;
        target.knockback(0.5 + mod, Math.sin(source.getYRot() * ((float) Math.PI / 180F)), -Math.cos(source.getYRot() * ((float) Math.PI / 180F)));
        return false;
    }

    @Override
    public boolean canAttackBlock (@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player)
    {
        return !player.isCreative();
    }

    @Override
    public boolean canPerformAction (ItemStack stack, ToolAction toolAction)
    {
        return ACTIONS.contains(toolAction);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers (EquipmentSlot slot, ItemStack itemStack)
    {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        if (slot == EquipmentSlot.MAINHAND)
        {
            CompoundTag tag = itemStack.getOrCreateTag();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 9D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.2D, AttributeModifier.Operation.ADDITION));
            float baseReach = switch (tag.getInt(ACTION_TAG))
            {
                case 1 -> 2;
                case 2 -> 0.5f;
                default -> 1;
            };
            builder.put(ForgeMod.ENTITY_REACH.get(),
                new AttributeModifier(REACH_UUID, "Weapon modifier", baseReach + itemStack.getEnchantmentLevel(EnchantmentRegistry.GIANT.get()), AttributeModifier.Operation.ADDITION));
            if (tag.getBoolean("ProperSwing"))
            {
                builder.put(Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(KNOCKBACK_RESISTANCE_UUID, "Weapon modifier", 0.6 + itemStack.getEnchantmentLevel(EnchantmentRegistry.POISE.get()) * 0.1, AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.ARMOR_TOUGHNESS,
                    new AttributeModifier(TOUGHNESS_UUID, "Weapon modifier", (1 + itemStack.getEnchantmentLevel(EnchantmentRegistry.POISE.get())) * 5, AttributeModifier.Operation.ADDITION));
            }
        }
        return builder.build();
    }

    @Override
    public Hypermap<Attribute, UUID, Style> getDefaultAttributesStyles (ItemStack itemStack)
    {
        return Hypermap.of(
            Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_UUID, this.defaultStyle(itemStack),
            Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_UUID, this.defaultStyle(itemStack),
            ForgeMod.ENTITY_REACH.get(), REACH_UUID, this.defaultStyle(itemStack)
        );
    }

    @Override
    public void appendHoverText (@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        this.gatherTooltipLines(list, "pyromancer.hidden_desc", "desc", PyromancerConfig.descTooltipKey);
        this.gatherTooltipLines(list, "pyromancer.hidden_lore", "lore", PyromancerConfig.loreTooltipKey);
    }

    @Override
    public <T extends LivingEntity> void thirdPersonTransform (@NotNull ItemStack itemStack, HumanoidModel<T> model, T entity, float ageInTicks)
    {
        CompoundTag tag = itemStack.getOrCreateTag();
        switch (tag.getInt(ACTION_TAG))
        {
            case 0 -> defaultSwingThirdPerson(itemStack, model, entity, ageInTicks);
            case 1 -> crouchPokeThirdPerson(itemStack, model, entity, ageInTicks);
            case 2 -> wideSwingThirdPerson(itemStack, model, entity, ageInTicks);
            //case 3 -> verticalStrikeThirdPerson(itemStack, model, entity, ageInTicks);
        }
    }

    private <T extends LivingEntity> void defaultSwingThirdPerson (ItemStack itemStack, @NotNull HumanoidModel<T> model, T entity, float ageInTicks)
    {
        if (!(model.attackTime <= 0.0F)) {
            HumanoidArm arm = entity.swingingArm == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();
            ModelPart modelpart = arm == HumanoidArm.LEFT ? model.leftArm : model.rightArm;
            float f = model.attackTime;
            model.body.yRot = Mth.sin(Mth.sqrt(f) * 6.2831855F) * 0.2F;
            ModelPart var10000;
            if (arm == HumanoidArm.LEFT) {
                var10000 = model.body;
                var10000.yRot *= -1.0F;
            }

            model.rightArm.z = Mth.sin(model.body.yRot) * 5.0F;
            model.rightArm.x = -Mth.cos(model.body.yRot) * 5.0F;
            model.leftArm.z = -Mth.sin(model.body.yRot) * 5.0F;
            model.leftArm.x = Mth.cos(model.body.yRot) * 5.0F;
            var10000 = model.rightArm;
            var10000.yRot += model.body.yRot;
            var10000 = model.leftArm;
            var10000.yRot += model.body.yRot;
            var10000 = model.leftArm;
            var10000.xRot += model.body.yRot;
            f = 1.0F - model.attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * 3.1415927F);
            float f2 = Mth.sin(model.attackTime * 3.1415927F) * -(model.head.xRot - 0.7F) * 1.2F;
            modelpart.xRot -= f1 * 1.2F + f2;
            modelpart.yRot += model.body.yRot * 2.0F;
            modelpart.zRot += Mth.sin(model.attackTime * 3.1415927F) * -0.4F;
        }
    }

    private <T extends LivingEntity> void crouchPokeThirdPerson (ItemStack itemStack, @NotNull HumanoidModel<T> model, T entity, float ageInTicks)
    {
        if (!(model.attackTime <= 0.0F))
        {
            HumanoidArm arm = entity.swingingArm == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();
            ModelPart modelpart = arm == HumanoidArm.LEFT ? model.leftArm : model.rightArm;
            float f = model.attackTime;
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
            modelpart.z -= f1 * 12 * Mth.cos(model.head.xRot);
            modelpart.y += f1 * 12 * Mth.sin(model.head.xRot);
            modelpart.xRot += model.head.xRot;
        }
    }

    private <T extends LivingEntity> void wideSwingThirdPerson (ItemStack itemStack, @NotNull HumanoidModel<T> model, T entity, float ageInTicks)
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
            modelpart.xRot += Mth.PI / 16;
            modelpart.xRot -= f1 * 2.25F;
            modelpart.yRot += model.head.xRot;
            modelpart.zRot = -Mth.HALF_PI * i;
            modelpart.zRot -= Mth.sin(model.attackTime * (float) Math.PI) * -0.4F;
        }
    }

    @Override
    public void firstPersonTransform (@NotNull ItemStack itemStack, PoseStack poseStack, float swingProgress, float equippedProgress, boolean isRight)
    {
        CompoundTag tag = itemStack.getOrCreateTag();
        switch (tag.getInt(ACTION_TAG))
        {
            case 0 -> defaultSwingFirstPerson(itemStack, poseStack, swingProgress, equippedProgress, isRight);
            case 1 -> crouchPokeFirstPerson(itemStack, poseStack, swingProgress, equippedProgress, isRight);
            case 2 -> wideSwingFirstPerson(itemStack, poseStack, swingProgress, equippedProgress, isRight);
            //case 3 -> verticalStrikeFirstPerson(itemStack, poseStack, swingProgress, equippedProgress, isRight);
        }
    }

    private void defaultSwingFirstPerson (ItemStack itemStack, @NotNull PoseStack poseStack, float swingProgress, float equippedProgress, boolean isRight)
    {
        HumanoidArm arm = isRight ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        float f = Mth.sin(swingProgress * swingProgress * 3.1415927F);
        poseStack.mulPose(Axis.YP.rotationDegrees((float)i * (45.0F + f * -20.0F)));
        float f1 = Mth.sin(Mth.sqrt(swingProgress) * 3.1415927F);
        poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * f1 * -20.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees((float)i * -45.0F));
    }

    private void crouchPokeFirstPerson (ItemStack itemStack, @NotNull PoseStack poseStack, float swingProgress, float equippedProgress, boolean isRight)
    {
        final int doRotation = swingProgress > 0.0F ? 1 : 0;
        final float f1 = Mth.sin(Mth.sqrt(swingProgress) * 3.1415927F);
        poseStack.translate(0, 0, -f1 * 5);
        poseStack.mulPose(Axis.XP.rotationDegrees(doRotation * -90.0F));
        final float scaleMod = doRotation == 0 ? 1F : 2.75F;
        poseStack.scale(scaleMod, scaleMod, scaleMod);
    }

    private void wideSwingFirstPerson (ItemStack itemStack, @NotNull PoseStack poseStack, float swingProgress, float equippedProgress, boolean isRight)
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
