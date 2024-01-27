package xyz.nikgub.pyromancer.mixin.client;

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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.ember.Ember;
import xyz.nikgub.pyromancer.registries.custom.EmberRegistry;

@SuppressWarnings("unused")
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow
    private ItemRenderer itemRenderer;
    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource bufferSource, int i);
    @Shadow
    public abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float f);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    public void renderArmWithItemMixinHead(AbstractClientPlayer player, float v, float v1, InteractionHand hand, float v2, ItemStack itemStack, float v3, PoseStack poseStack, MultiBufferSource multiBufferSource, int i1, CallbackInfo callbackInfo) {
        if(player.getUsedItemHand().equals(hand) && player.getUseItemRemainingTicks() > 0){
            Ember ember = EmberRegistry.getFromItem(itemStack);
            if(ember == null) return;
            if(Ember.emberItemStackPredicate(itemStack)) {
                boolean flag = hand == InteractionHand.MAIN_HAND;
                HumanoidArm arm = flag ? player.getMainArm() : player.getMainArm().getOpposite();
                ember.getAnimation().firstPersonAnimation().accept(poseStack);
                this.applyItemArmTransform(poseStack, hand.equals(InteractionHand.MAIN_HAND) ? HumanoidArm.RIGHT : HumanoidArm.LEFT, v3);
                boolean flag1 = player.getMainArm() == HumanoidArm.RIGHT;
                this.renderItem(player, itemStack, flag1 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                        !hand.equals(InteractionHand.MAIN_HAND),
                        poseStack,
                        multiBufferSource,
                        i1);
                callbackInfo.cancel();
            }
        }
        //poseStack.popPose();
    }
}
