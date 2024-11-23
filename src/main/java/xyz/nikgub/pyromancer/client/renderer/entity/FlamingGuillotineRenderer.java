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
package xyz.nikgub.pyromancer.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.model.entity.FlamingGuillotineModel;
import xyz.nikgub.pyromancer.common.entity.attack_effect.FlamingGuillotineEntity;

public class FlamingGuillotineRenderer extends EntityRenderer<FlamingGuillotineEntity>
{
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(PyromancerMod.MOD_ID, "textures/entity/flaming_guillotine.png");

    private final FlamingGuillotineModel<FlamingGuillotineEntity> model;

    public FlamingGuillotineRenderer (EntityRendererProvider.Context context)
    {
        super(context);
        this.model = new FlamingGuillotineModel<>(context.bakeLayer(FlamingGuillotineModel.LAYER_LOCATION));
    }

    public void render (@NotNull FlamingGuillotineEntity entity, float v, float v1, @NotNull PoseStack poseStack, MultiBufferSource multiBufferSource, int p_114533_)
    {
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION));
        poseStack.scale(entity.getSize(), -1.0F * entity.getSize(), entity.getSize());
        poseStack.translate(0D, -1D, 0D);
        //this.model.prepareMobModel(entity, f5, f8, p_115310_);
        this.model.setupAnim(entity, 0, 0, entity.tickCount + v1, 0, 0);
        this.model.renderToBuffer(poseStack, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);
        super.render(entity, v, v1, poseStack, multiBufferSource, p_114533_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation (@NotNull FlamingGuillotineEntity p_114482_)
    {
        return TEXTURE_LOCATION;
    }
}
