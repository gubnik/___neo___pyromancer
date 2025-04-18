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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
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
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.incandescent.util.Hypermap;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registry.EnchantmentRegistry;
import xyz.nikgub.pyromancer.registry.StyleRegistry;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SpearOfMorozItem extends Item implements ICustomSwingItem, IBetterAttributeTooltipItem, IExtensibleTooltipItem
{
    public static final Set<ToolAction> ACTIONS = Set.of(ToolActions.SWORD_SWEEP);

    public static final UUID COLD_BUILDUP_UUID = UUID.fromString("c79b16a9-3513-4d35-9a78-5c92882673c5");
    public static final UUID REACH_UUID = UUID.fromString("f8d0df81-d456-4013-a058-40c4a5be1f2d");
    public static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("d74e1d05-f83c-40a9-b61f-ab8d5e13d969");

    public static final String ACTION_TAG = "__SPEAR_OF_MOROZ_ACTION__";

    public SpearOfMorozItem (Properties pProperties)
    {
        super(pProperties.stacksTo(1).defaultDurability(1500));
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
        if (player.swingTime > 0) return;
        CompoundTag tag = itemStack.getOrCreateTag();
        if (player.isSprinting() && !player.isCrouching() && player.getMainHandItem() == itemStack)
        {
            tag.putInt(ACTION_TAG, 1);
            for (LivingEntity target : EntityUtils.entityCollector(player.getEyePosition(), 1.4, level))
            {
                if (target == player) continue;
                target.hurt(DamageSourceRegistry.spearOfMoroz(player), (float) ((int) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.3D) + 1.0F);
                target.addDeltaMovement(player.getDeltaMovement().multiply(1.2, 1.2, 1.2).add(0, 0.2, 0));
                target.addDeltaMovement(player.getLookAngle().multiply(1, 0.5, 1));
            }
            runningAttackEffect(player);
        } else
        {
            tag.putInt(ACTION_TAG, 0);
        }
        //runningAttackEffect(itemStack, player);
    }

    @Override
    public boolean hurtEnemy (@NotNull ItemStack itemStack, @NotNull LivingEntity target, @NotNull LivingEntity source)
    {
        if (itemStack.getOrCreateTag().getInt(ACTION_TAG) == 1)
        {
            target.knockback(1.2, Math.sin(source.getYRot() * ((float) Math.PI / 180F)), -Math.cos(source.getYRot() * ((float) Math.PI / 180F)));
        } else
        {
            target.knockback(0.5, Math.sin(source.getYRot() * ((float) Math.PI / 180F)), -Math.cos(source.getYRot() * ((float) Math.PI / 180F)));
        }
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
        if (toolAction == ToolActions.SWORD_SWEEP)
        {
            return stack.getOrCreateTag().getInt(ACTION_TAG) == 1;
        }
        return ACTIONS.contains(toolAction);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers (EquipmentSlot slot, ItemStack itemStack)
    {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        int mode = itemStack.getOrCreateTag().getInt(ACTION_TAG);
        if (slot == EquipmentSlot.MAINHAND)
        {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (mode == 1) ? 5D : 7D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.7D, AttributeModifier.Operation.ADDITION));
            if (mode == 0)
            {
                builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(REACH_UUID, "Weapon modifier", 1, AttributeModifier.Operation.ADDITION));
            }
            if (mode == 1)
            {
                builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(KNOCKBACK_RESISTANCE_UUID, "Weapon modifier", 1, AttributeModifier.Operation.ADDITION));
            }
            final double coldAmount = ((mode == 1) ? 12 : 8) + itemStack.getEnchantmentLevel(EnchantmentRegistry.FIERCE_FROST.get());
            builder.put(AttributeRegistry.COLD_BUILDUP.get(), new AttributeModifier(COLD_BUILDUP_UUID, "Weapon modifier", coldAmount, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }

    @Override
    public final void appendHoverText (@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        this.gatherTooltipLines(list, "pyromancer.hidden_desc", "desc", PyromancerConfig.descTooltipKey);
        this.gatherTooltipLines(list, "pyromancer.hidden_lore", "lore", PyromancerConfig.loreTooltipKey);
    }

    @Override
    public <T extends LivingEntity> void thirdPersonTransform (ItemStack itemStack, HumanoidModel<T> model, T entity, float ageInTicks)
    {
        if (itemStack.getOrCreateTag().getInt(ACTION_TAG) == 1)
        {
            if (!(model.attackTime <= 0.0F))
            {
                runningAttackActiveThirdPerson(model, entity);
            } else runningAttackPassiveThirdPerson(itemStack, model, entity);
            return;
        }
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

    @Override
    public void firstPersonTransform (ItemStack itemStack, PoseStack poseStack, float swingProgress, float equippedProgress, boolean isRight)
    {
        if (itemStack.getOrCreateTag().getInt(ACTION_TAG) == 1)
        {
            if (swingProgress > 0.0F)
            {
                runningAttackActiveFirstPerson(poseStack, swingProgress, isRight);
            } else
            {
                runningAttackPassiveFirstPerson(poseStack);
            }
            return;
        }
        final int doRotation = swingProgress > 0.0F ? 1 : 0;
        final float f1 = Mth.sin(Mth.sqrt(swingProgress) * 3.1415927F);
        poseStack.translate(0, 0, -f1 * 5);
        poseStack.mulPose(Axis.XP.rotationDegrees(doRotation * -90.0F));
        final float scaleMod = doRotation == 0 ? 1F : 2.75F;
        poseStack.scale(scaleMod, scaleMod, scaleMod);
    }

    private <T extends LivingEntity> void runningAttackPassiveThirdPerson (ItemStack itemStack, HumanoidModel<T> model, T entity)
    {

        HumanoidArm arm = entity.getMainHandItem() == itemStack ? entity.getMainArm() : entity.getMainArm().getOpposite();
        ModelPart modelpart = arm == HumanoidArm.LEFT ? model.leftArm : model.rightArm;
        float f = 1;
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
        modelpart.xRot += 1800;
    }

    private <T extends LivingEntity> void runningAttackActiveThirdPerson (HumanoidModel<T> model, T entity)
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

            model.rightArm.z = Mth.sin(model.body.yRot) * 5.0F;
            model.rightArm.x = -Mth.cos(model.body.yRot) * 5.0F;
            model.leftArm.z = -Mth.sin(model.body.yRot) * 5.0F;
            model.leftArm.x = Mth.cos(model.body.yRot) * 5.0F;
            model.rightArm.yRot += model.body.yRot;
            model.leftArm.yRot += model.body.yRot;
            model.leftArm.xRot += model.body.yRot;
            f = 1.0F - model.attackTime;
            f *= f;
            f *= f;
            //f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(model.attackTime * (float) Math.PI) * -(model.head.xRot - 0.7F) * 0.75F;
            modelpart.xRot -= f1 * 1.2F + f2;
            modelpart.yRot += model.body.yRot * 2.0F;
            modelpart.zRot += Mth.sin(model.attackTime * (float) Math.PI) * -0.4F;
        }
    }

    public void runningAttackPassiveFirstPerson (PoseStack poseStack)
    {
        final int doRotation = 1;
        final float f1 = 1;
        poseStack.translate(0, 0, -f1 * 1);
        poseStack.mulPose(Axis.XP.rotationDegrees(doRotation * -90.0F));
        final float scaleMod = 2.25F;
        poseStack.scale(scaleMod, scaleMod, scaleMod);
    }

    public void runningAttackActiveFirstPerson (PoseStack poseStack, float swingProgress, boolean isRight)
    {
        final int doRotation = swingProgress > 0.0F ? 1 : 0;
        HumanoidArm arm = isRight ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
        final int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        final float f = Mth.sin(swingProgress * swingProgress * 3.1415927F);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) i * (45.0F + f * -20.0F)));
        final float f1 = Mth.sin(Mth.sqrt(swingProgress) * 3.1415927F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(i * -45 * doRotation + (float) i * f1 * -45.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees((float) i * -45.0F));
        poseStack.translate(0, 2.25F * doRotation, 0);
        final float scaleMod = doRotation == 0 ? 1F : 2.75F;
        poseStack.scale(scaleMod, scaleMod, scaleMod);
    }

    protected void runningAttackEffect (Player player)
    {
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;
        player.setTicksFrozen(player.getTicksFrozen() + 3);
        final double px = player.getX();
        final double py = player.getY();
        final double pz = player.getZ();
        final double multiplier = 1.4D;
        final float hRad = player.getYRot() * Mth.PI / 180F;
        for (int i = 0; i < 15; i++)
        {
            float rad = 24 * i * Mth.PI / 180F;
            serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, px - Mth.sin(hRad + rad) * multiplier, py, pz + Mth.cos(hRad + rad) * multiplier, 1, 0.0D, 0.0D, 0.0D, 0.03D);
        }
    }

    @Override
    public Hypermap<Attribute, UUID, Style> getDefaultAttributesStyles (ItemStack itemStack)
    {
        return Hypermap.of(
            Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_UUID, this.defaultStyle(itemStack),
            Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_UUID, this.defaultStyle(itemStack),
            AttributeRegistry.COLD_BUILDUP.get(), COLD_BUILDUP_UUID, StyleRegistry.FROST_STYLE,
            ForgeMod.ENTITY_REACH.get(), REACH_UUID, this.defaultStyle(itemStack)
        );
    }

    @Override
    public double getAdditionalPlayerBonus (final ItemStack itemStack, final Player player, final Attribute attribute)
    {
        return 0;
    }
}
