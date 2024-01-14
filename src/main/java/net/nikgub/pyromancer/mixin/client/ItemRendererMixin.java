package net.nikgub.pyromancer.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.items.BlazingJournalItem;
import net.nikgub.pyromancer.items.CompendiumOfFlameItem;
import net.nikgub.pyromancer.items.quills.QuillItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
@SuppressWarnings("unused")
public abstract class ItemRendererMixin {
    @Shadow
    public abstract void renderModelLists(BakedModel p_115190_, ItemStack p_115191_, int p_115192_, int p_115193_, PoseStack p_115194_, VertexConsumer p_115195_);
    @Shadow
    public abstract BakedModel getModel(ItemStack p_174265_, @Nullable Level p_174266_, @Nullable LivingEntity p_174267_, int p_174268_);
    @Shadow
    private ItemColors itemColors;
    @SuppressWarnings("unstable")
    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    public void renderMixinTail(ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel, CallbackInfo callbackInfo) {
        if(!(itemStack.getItem() instanceof BlazingJournalItem blazingJournalItem) || !(blazingJournalItem.getItemFromItem(itemStack, 0).getItem() instanceof QuillItem)) return;
        ItemStack quill = blazingJournalItem.getItemFromItem(itemStack, 0);
        bakedModel = this.getModel(quill, null, null, (b ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).ordinal());
        bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(poseStack, bakedModel, displayContext, b);
        VertexConsumer vertex;
        if(itemStack.getItem() instanceof CompendiumOfFlameItem) {
            switch (displayContext)
            {
                case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND ->
                {
                    poseStack.translate(-1D, 0.35 + 0.03D * Mth.sin(Mth.PI / 180 * (PyromancerMod.clientTick % 90) * 4), -0.5);
                    poseStack.rotateAround(Axis.ZP.rotationDegrees(-30), 0, 0, 0);
                }
                default -> poseStack.translate(-0.5D, -0.4D, -0.5D);
            }
        }
        else poseStack.translate(-0.5001D, -0.5001D, -0.5101D);
        poseStack.scale(1f, 1f, 1.02f);
        for (RenderType renderType : bakedModel.getRenderTypes(quill, true)) {
            vertex = multiBufferSource.getBuffer(renderType);
            this.renderModelLists(bakedModel, quill, i, j, poseStack, vertex);
        }
        callbackInfo.cancel();
    }
}
