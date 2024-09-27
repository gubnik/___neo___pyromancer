package xyz.nikgub.pyromancer.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.item.BlazingJournalItem;
import xyz.nikgub.pyromancer.common.item.CompendiumOfFlameItem;
import xyz.nikgub.pyromancer.common.item.QuillItem;
import xyz.nikgub.pyromancer.common.item.UsablePyromancyItem;
import xyz.nikgub.pyromancer.common.mob_effect.InfusionMobEffect;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ItemRenderer.class)
@SuppressWarnings("unused")
public abstract class ItemRendererMixin
{
    @Unique
    private static BakedModel pyromancerNew$handleCameraTransforms (PoseStack poseStack, BakedModel model, ItemDisplayContext cameraTransformType, boolean applyLeftHandTransform)
    {
        model = model.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        return model;
    }

    @Shadow
    public abstract void renderModelLists (BakedModel p_115190_, ItemStack p_115191_, int p_115192_, int p_115193_, PoseStack p_115194_, VertexConsumer p_115195_);

    @Shadow
    public abstract BakedModel getModel (ItemStack p_174265_, @Nullable Level p_174266_, @Nullable LivingEntity p_174267_, int p_174268_);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderMixinHead (ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel, CallbackInfo callbackInfo)
    {

    }

    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    public void renderMixinTail (ItemStack itemStack, ItemDisplayContext displayContext, boolean b, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel, CallbackInfo callbackInfo)
    {
        if (!(itemStack.getItem() instanceof BlazingJournalItem blazingJournalItem)) return;
        pyromancerNew$quillRenderManager(itemStack, poseStack, displayContext, multiBufferSource, b, i, j);
        if (blazingJournalItem instanceof CompendiumOfFlameItem && !itemStack.getOrCreateTag().getBoolean(CompendiumOfFlameItem.IS_OFFHAND))
            pyromancerNew$pyromancyRenderManager(itemStack, poseStack, displayContext, multiBufferSource, b, i, j);
        callbackInfo.cancel();
    }

    @Unique
    public void pyromancerNew$quillRenderManager (ItemStack itemStack, PoseStack poseStack, ItemDisplayContext displayContext, MultiBufferSource multiBufferSource, boolean b, int i, int j)
    {
        if (!(itemStack.getItem() instanceof BlazingJournalItem blazingJournalItem)) return;
        VertexConsumer vertex;
        ItemStack quill = blazingJournalItem.getItemFromItem(itemStack, 0);
        if (!(quill.getItem() instanceof QuillItem)) quill = new ItemStack(ItemRegistry.BLAZING_QUILL.get());
        BakedModel bakedModelQuill = this.getModel(quill, null, null, (b ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).ordinal());
        bakedModelQuill = pyromancerNew$handleCameraTransforms(poseStack, bakedModelQuill, displayContext, b);
        if (itemStack.getItem() instanceof CompendiumOfFlameItem)
        {
            switch (displayContext)
            {
                case FIRST_PERSON_LEFT_HAND ->
                {
                    poseStack.translate(-0.5D, -0.03D * Math.sin(Math.PI / 180 * (PyromancerMod.clientTick % 90) * 4), -0.5f);
                    poseStack.rotateAround(Axis.ZP.rotationDegrees(45), 0.5f, (float) (0.5 + 0.03f * Math.sin(Math.PI / 180 * (PyromancerMod.clientTick % 90) * 4)), 0);
                }
                case FIRST_PERSON_RIGHT_HAND ->
                {
                    poseStack.translate(-0.5D, -0.03D * Math.sin(Math.PI / 180 * (PyromancerMod.clientTick % 90) * 4), -0.5f);
                    poseStack.rotateAround(Axis.ZP.rotationDegrees(-45), 0.5f, (float) (0.5 + 0.03f * Math.sin(Math.PI / 180 * (PyromancerMod.clientTick % 90) * 4)), 0);
                }
                default ->
                {
                    poseStack.translate(-0.5D, -0.2D, -0.5D);
                    poseStack.rotateAround(Axis.YP.rotationDegrees(-90), 0.5f, 0.4f, 0.5f);
                }
            }
            poseStack.scale(0.75f, 0.75f, 0.75f);
        } else
        {
            poseStack.translate(-0.5001D, -0.5001D, -0.5101D);
            poseStack.scale(1f, 1f, 1.02f);
        }
        if (!(blazingJournalItem.getItemFromItem(itemStack, 0).getItem() instanceof QuillItem)) return;
        for (RenderType renderType : bakedModelQuill.getRenderTypes(quill, true))
        {
            vertex = multiBufferSource.getBuffer(renderType);
            this.renderModelLists(bakedModelQuill, quill, i, j, poseStack, vertex);
        }
    }

    @Unique
    public void pyromancerNew$pyromancyRenderManager (ItemStack itemStack, PoseStack poseStack, ItemDisplayContext displayContext, MultiBufferSource multiBufferSource, boolean b, int i, int j)
    {
        if (!(itemStack.getItem() instanceof CompendiumOfFlameItem compendiumOfFlameItem
                && compendiumOfFlameItem.getItemFromItem(itemStack, itemStack.getOrCreateTag().getInt(CompendiumOfFlameItem.ACTIVE_SLOT_TAG)).getItem() instanceof UsablePyromancyItem usablePyromancyItem)
                || displayContext == ItemDisplayContext.GUI)
            return;
        VertexConsumer vertex;
        ItemStack pyromancy = compendiumOfFlameItem.getItemFromItem(itemStack, itemStack.getOrCreateTag().getInt(CompendiumOfFlameItem.ACTIVE_SLOT_TAG));
        pyromancy.getOrCreateTag().putBoolean(CompendiumOfFlameItem.PYROMANCY_CUSTOM_RENDER_TAG, true);
        BakedModel bakedModelPyromancy = this.getModel(pyromancy, null, null, (b ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).ordinal());
        bakedModelPyromancy = pyromancerNew$handleCameraTransforms(poseStack, bakedModelPyromancy, displayContext, b);
        usablePyromancyItem.compendiumTransforms(poseStack, displayContext);
        for (RenderType renderType : bakedModelPyromancy.getRenderTypes(pyromancy, true))
        {
            vertex = multiBufferSource.getBuffer(renderType);
            this.renderModelLists(bakedModelPyromancy, pyromancy, i, j, poseStack, vertex);
        }
    }

    @Inject(method = "renderQuadList", at = @At("HEAD"), cancellable = true)
    public void renderQuadList (PoseStack poseStack, VertexConsumer vertexConsumer, List<BakedQuad> bakedQuads, ItemStack itemStack, int p_115167_, int p_115168_, CallbackInfo callbackInfo)
    {
        if (itemStack.getItem() instanceof UsablePyromancyItem && itemStack.getOrCreateTag().getBoolean(CompendiumOfFlameItem.PYROMANCY_CUSTOM_RENDER_TAG))
        {
            PoseStack.Pose posestack$pose = poseStack.last();
            for (BakedQuad bakedquad : bakedQuads)
            {
                float f = 1f;
                float f1 = 0.5f;
                float f2 = 0.0f;
                vertexConsumer.putBulkData(posestack$pose, bakedquad, f, f1, f2, 0.5F, 255, p_115168_, false);
            }
            callbackInfo.cancel();
        }
        CompoundTag tag = itemStack.getTag();
        if (tag == null) return;
        if (tag.getBoolean(InfusionMobEffect.INFUSION_TAG))
        {
            PoseStack.Pose posestack$pose = poseStack.last();
            for (BakedQuad bakedquad : bakedQuads)
            {
                vertexConsumer.putBulkData(posestack$pose, bakedquad,
                        tag.getFloat(InfusionMobEffect.RED_TAG),
                        tag.getFloat(InfusionMobEffect.GREEN_TAG),
                        tag.getFloat(InfusionMobEffect.BLUE_TAG),
                        tag.getFloat(InfusionMobEffect.ALPHA_TAG) / 2f
                        , 255, p_115168_, true);
            }
            callbackInfo.cancel();
        }
    }
}
