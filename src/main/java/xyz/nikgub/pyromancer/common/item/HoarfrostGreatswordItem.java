package xyz.nikgub.pyromancer.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.item.ICustomSwingItem;
import xyz.nikgub.incandescent.common.item.IExtensibleTooltipItem;
import xyz.nikgub.incandescent.common.item.INotStupidTooltipItem;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.registries.AttributeRegistry;
import xyz.nikgub.pyromancer.registries.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registries.EnchantmentRegistry;
import xyz.nikgub.pyromancer.registries.StyleRegistry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

public class HoarfrostGreatswordItem extends Item implements ICustomSwingItem, INotStupidTooltipItem, IExtensibleTooltipItem
{
    public static final Set<ToolAction> ACTIONS = Set.of(ToolActions.SWORD_SWEEP);

    public static final UUID COLD_BUILDUP_UUID = UUID.fromString("c79b16a9-3513-4d35-9a78-5c92882673c5");
    public static final UUID REACH_UUID = UUID.fromString("f8d0df81-d456-4013-a058-40c4a5be1f2d");

    public static final String ACTION_TAG = "__FROSTBORNE_HOARFROST_GREATSWORD_ACTION__";

    public HoarfrostGreatswordItem (Properties properties)
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
        if (player.swingTime > 0) return;
        CompoundTag tag = itemStack.getOrCreateTag();
        if (player.isCrouching() && player.getMainHandItem() == itemStack)
        {
            tag.putInt(ACTION_TAG, 1);
        } else
        {
            tag.putInt(ACTION_TAG, 0);
        }
    }

    @Override
    public boolean hurtEnemy (@NotNull ItemStack itemStack, @NotNull LivingEntity target, @NotNull LivingEntity source)
    {
        target.knockback(0.5, Math.sin(source.getYRot() * ((float) Math.PI / 180F)), -Math.cos(source.getYRot() * ((float) Math.PI / 180F)));
        if (itemStack.getOrCreateTag().getInt(ACTION_TAG) == 0) return false;
        source.setTicksFrozen(source.getTicksFrozen() + 70);
        if (!(source.level() instanceof ServerLevel level)) return false;
        for (Vec3 pos : GeneralUtils.launchRay(source.getEyePosition(), source.getLookAngle(), 40, 0.2))
        {
            level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, 2, 0.02, 0.02, 0.02, 0.0025);
            for (LivingEntity entity : EntityUtils.entityCollector(pos, 0.5, level))
            {
                if (entity == target || entity == source) continue;
                entity.hurt(DamageSourceRegistry.hoarfrostGreatswordPoke(source), (float) source.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.2f);
                entity.knockback(1.0, Math.sin(source.getYRot() * ((float) Math.PI / 180F)), -Math.cos(source.getYRot() * ((float) Math.PI / 180F)));
            }
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
        if (stack.getOrCreateTag().getInt(ACTION_TAG) == 1 && toolAction == ToolActions.SWORD_SWEEP) return false;
        return ACTIONS.contains(toolAction);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers (EquipmentSlot slot, ItemStack itemStack)
    {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        boolean crouchPoke = itemStack.getOrCreateTag().getInt(ACTION_TAG) == 1;
        if (slot == EquipmentSlot.MAINHAND)
        {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", crouchPoke ? 5D : 8D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", crouchPoke ? -2.8D : -3.0D, AttributeModifier.Operation.ADDITION));
            final double coldAmount = ((crouchPoke) ? 12 : 10) + itemStack.getEnchantmentLevel(EnchantmentRegistry.FIERCE_FROST.get());
            builder.put(AttributeRegistry.COLD_BUILDUP.get(), new AttributeModifier(COLD_BUILDUP_UUID, "Weapon modifier", coldAmount, AttributeModifier.Operation.ADDITION));
            //builder.put(AttributeRegistry.COLD_BUILDUP.get(), new AttributeModifier(COLD_BUILDUP_UUID, "Weapon modifier", 10, AttributeModifier.Operation.ADDITION));
            if (crouchPoke)
            {
                builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(REACH_UUID, "Weapon modifier", 1, AttributeModifier.Operation.ADDITION));
            }
        }
        return builder.build();
    }

    @Override
    public Map<Attribute, Pair<UUID, Style>> specialColoredUUID (ItemStack itemStack)
    {
        return Map.of(AttributeRegistry.COLD_BUILDUP.get(), Pair.of(COLD_BUILDUP_UUID, StyleRegistry.FROST_STYLE));
    }

    @Override
    public BiFunction<Player, Attribute, Double> getAdditionalPlayerBonus (ItemStack itemStack)
    {
        return ((player, attribute) -> 0d);
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
            crouchPokeThirdPerson(model, entity);
        } else
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
    }

    @Override
    public void firstPersonTransform (ItemStack itemStack, PoseStack poseStack, float swingProgress, float equippedProgress, boolean isRight)
    {
        if (itemStack.getOrCreateTag().getInt(ACTION_TAG) == 1)
        {
            crouchPokeFirstPerson(poseStack, swingProgress);
        } else
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

    public <T extends LivingEntity> void crouchPokeThirdPerson (HumanoidModel<T> model, T entity)
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

    public void crouchPokeFirstPerson (PoseStack poseStack, float swingProgress)
    {
        final int doRotation = swingProgress > 0.0F ? 1 : 0;
        final float f1 = Mth.sin(Mth.sqrt(swingProgress) * 3.1415927F);
        poseStack.translate(0, 0, -f1 * 5);
        poseStack.mulPose(Axis.XP.rotationDegrees(doRotation * -90.0F));
        final float scaleMod = doRotation == 0 ? 1F : 2.75F;
        poseStack.scale(scaleMod, scaleMod, scaleMod);
    }
}
