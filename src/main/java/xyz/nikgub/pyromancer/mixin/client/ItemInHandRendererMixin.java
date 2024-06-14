package xyz.nikgub.pyromancer.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.common.ember.Ember;

@SuppressWarnings("unused")
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Final
    @Shadow
    private ItemRenderer itemRenderer;
    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource bufferSource, int i);
    @Shadow
    protected abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float f);

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    public void renderArmWithItemMixinHead(AbstractClientPlayer player,
                                           float partialTick,
                                           float pPitch, InteractionHand hand,
                                           float swingProgress, ItemStack itemStack,
                                           float equippedProgress,
                                           PoseStack poseStack,
                                           MultiBufferSource multiBufferSource,
                                           int pCombinedLight,
                                           CallbackInfo callbackInfo) {
        boolean flag = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = flag ? player.getMainArm() : player.getMainArm().getOpposite();
        boolean isRight = (arm == HumanoidArm.RIGHT);
        boolean customBehaviour = false;
        poseStack.pushPose();
        if(player.getUsedItemHand() == hand && player.isUsingItem() && player.getUseItemRemainingTicks() > 0){
            this.applyItemArmTransform(poseStack, arm, equippedProgress);
            Ember ember = Ember.getFromItem(itemStack);
            if (ember == null)
                customBehaviour = true;
            else
                ember.getAnimation().getFirstPersonAnimation().run(poseStack, minecraft.player, arm, itemStack, partialTick, equippedProgress, swingProgress);
        }
        if (!customBehaviour) return;
        this.renderItem(minecraft.player, itemStack, isRight ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !isRight, poseStack, multiBufferSource, pCombinedLight);
        poseStack.popPose();
        callbackInfo.cancel();
    }
}
