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
package xyz.nikgub.pyromancer.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.item.SpearOfMorozItem;
import xyz.nikgub.pyromancer.common.item.ZweihanderItem;
import xyz.nikgub.pyromancer.common.mob_effect.InfusionMobEffect;
import xyz.nikgub.pyromancer.registry.EnchantmentRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

@SuppressWarnings("unused")
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin
{

    @Shadow
    @Final
    private Minecraft minecraft;
    @Final
    @Shadow
    private ItemRenderer itemRenderer;

    @Shadow
    public abstract void renderItem (LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource bufferSource, int i);

    @Shadow
    protected abstract void applyItemArmTransform (PoseStack poseStack, HumanoidArm arm, float f);

    @Inject(
        method = {"renderItem"},
        at = {@At("HEAD")}
    )
    public void renderItemHead (LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo callbackInfo)
    {
        if (itemStack.isEmpty()) return;
        ItemStack toRender = itemStack.copy();
        if (!toRender.isEmpty() && toRender.getItem() instanceof ZweihanderItem)
        {
            float mod = toRender.getEnchantmentLevel(EnchantmentRegistry.GIANT.get()) * 0.1f + 1f;
            poseStack.scale(mod, mod, mod);
        }
        if (itemStack.getItem() == ItemRegistry.SPEAR_OF_MOROZ.get() && (displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
            && itemStack.getOrCreateTag().getInt(SpearOfMorozItem.ACTION_TAG) == 1)
        {
            poseStack.rotateAround(Axis.XN.rotationDegrees(180), 0, 0, 0.05F);
        }
    }

    @Inject(
        method = {"renderItem"},
        at = {@At("TAIL")}
    )
    public void renderItemTail (LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo callbackInfo)
    {
        if (itemStack.isEmpty()) return;
        if (livingEntity.getTicksUsingItem() > 0 && livingEntity.getUseItem() == livingEntity.getMainHandItem()) return;
        ItemStack toRender = itemStack.copy();
        for (MobEffectInstance instance : livingEntity.getActiveEffects())
        {
            if (instance.getEffect() instanceof InfusionMobEffect infusionMobEffect
                && PyromancerMod.DO_INFUSION_RENDER.get(toRender.getItem())
                && toRender.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE)
            )
            {
                toRender.getOrCreateTag().putBoolean(InfusionMobEffect.INFUSION_TAG, true);
                toRender.getOrCreateTag().putFloat(InfusionMobEffect.RED_TAG, infusionMobEffect.getItemColors().r());
                toRender.getOrCreateTag().putFloat(InfusionMobEffect.GREEN_TAG, infusionMobEffect.getItemColors().g());
                toRender.getOrCreateTag().putFloat(InfusionMobEffect.BLUE_TAG, infusionMobEffect.getItemColors().b());
                toRender.getOrCreateTag().putFloat(InfusionMobEffect.ALPHA_TAG, infusionMobEffect.getItemColors().a());
                this.itemRenderer.renderStatic(livingEntity, toRender, displayContext, b, poseStack, multiBufferSource, livingEntity.level(), i,
                    OverlayTexture.NO_OVERLAY, livingEntity.getId() + displayContext.ordinal());
            }
        }
    }
}
