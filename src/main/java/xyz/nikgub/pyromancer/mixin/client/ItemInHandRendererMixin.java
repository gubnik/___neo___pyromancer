package xyz.nikgub.pyromancer.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
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
import xyz.nikgub.pyromancer.common.ember.Ember;
import xyz.nikgub.pyromancer.common.item.SpearOfMorozItem;
import xyz.nikgub.pyromancer.common.item.ZweihanderItem;
import xyz.nikgub.pyromancer.common.mob_effect.InfusionMobEffect;
import xyz.nikgub.pyromancer.registries.EnchantmentRegistry;
import xyz.nikgub.pyromancer.registries.ItemRegistry;

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
            if (instance.getEffect() instanceof InfusionMobEffect infusionMobEffect && toRender.getMaxStackSize() == 1 && toRender.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE))
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

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    public void renderArmWithItemMixinHead (AbstractClientPlayer player,
                                            float partialTick,
                                            float pPitch, InteractionHand hand,
                                            float swingProgress, ItemStack itemStack,
                                            float equippedProgress,
                                            PoseStack poseStack,
                                            MultiBufferSource multiBufferSource,
                                            int pCombinedLight,
                                            CallbackInfo callbackInfo)
    {
        boolean flag = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = flag ? player.getMainArm() : player.getMainArm().getOpposite();
        boolean isRight = (arm == HumanoidArm.RIGHT);
        boolean customBehaviour = false;
        poseStack.pushPose();
        if (player.getUsedItemHand() == hand && player.isUsingItem() && player.getUseItemRemainingTicks() > 0)
        {
            Ember ember = Ember.getFromItem(itemStack);
            if (ember != null)
            {
                this.applyItemArmTransform(poseStack, arm, equippedProgress);
                customBehaviour = true;
                ember.getAnimation().getFirstPersonAnimation().run(poseStack, minecraft.player, arm, itemStack, partialTick, equippedProgress, swingProgress);
            }
        }
        if (!customBehaviour) return;
        this.renderItem(minecraft.player, itemStack, isRight ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !isRight, poseStack, multiBufferSource, pCombinedLight);
        poseStack.popPose();
        callbackInfo.cancel();
    }
}
