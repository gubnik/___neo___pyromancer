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
import net.minecraft.world.item.UseAnim;
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
    @Shadow
    private ItemRenderer itemRenderer;
    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource bufferSource, int i);
    @Shadow
    public abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float f);

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    public void renderArmWithItemMixinHead(AbstractClientPlayer player,
                                           float partialTick,
                                           float pPitch, InteractionHand hand,
                                           float pSwingProgress, ItemStack itemStack,
                                           float pEquippedProgress,
                                           PoseStack poseStack,
                                           MultiBufferSource multiBufferSource,
                                           int pCombinedLight,
                                           CallbackInfo callbackInfo) {
        if(player.getUsedItemHand().equals(hand) && player.getUseItemRemainingTicks() > 0 && itemStack.getUseAnimation() == UseAnim.NONE){
            Ember ember = Ember.getFromItem(itemStack);
            if (ember == null) return;
            poseStack.pushPose();
            boolean flag = hand == InteractionHand.MAIN_HAND;
            HumanoidArm arm = flag ? player.getMainArm() : player.getMainArm().getOpposite();
            ember.getAnimation().firstPersonAnimation().run(poseStack, minecraft.player, arm, itemStack, partialTick, pEquippedProgress, pSwingProgress);
            poseStack.popPose();
            callbackInfo.cancel();
        }
    }
}
