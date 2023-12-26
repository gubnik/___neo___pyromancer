package net.nikgub.pyromancer.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.nikgub.pyromancer.ember.Ember;
import net.nikgub.pyromancer.registries.custom.EmberRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow
    private ItemRenderer itemRenderer;
    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource bufferSource, int i);
    @Shadow
    public abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float f);

    /**
     * Injection that patches up first person view of itemstack with ember
     * Refer to {@link net.nikgub.pyromancer.animations.EmberAnimation} for additional info
     */
    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    public void renderArmWithItemMixinHead(AbstractClientPlayer player, float v, float v1, InteractionHand hand, float v2, ItemStack itemStack, float v3, PoseStack poseStack, MultiBufferSource multiBufferSource, int i1,
                                        CallbackInfo callbackInfo) {
        if(player.getUsedItemHand().equals(hand) && player.getUseItemRemainingTicks() > 0){
            Ember ember = EmberRegistry.getFromItem(itemStack);
            if(ember == null) return;
            if(Ember.emberItemStackPredicate(itemStack)) {
                this.applyItemArmTransform(poseStack, hand.equals(InteractionHand.MAIN_HAND) ? HumanoidArm.RIGHT : HumanoidArm.LEFT, v3);
                ember.getAnimation().firstPersonAnimation().accept(poseStack);
                this.renderItem(player, itemStack, hand.equals(InteractionHand.MAIN_HAND) ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                        false,
                        poseStack,
                        multiBufferSource,
                        i1);
                callbackInfo.cancel();
            }
        }
        //poseStack.popPose();
    }
}
