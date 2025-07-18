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
package xyz.nikgub.pyromancer.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.entity.attack_effect.PyronadoEntity;

public class PyronadoModel<T extends PyronadoEntity> extends HierarchicalModel<T>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "pyronado"), "main");
    private final ModelPart root;
    private final ModelPart big;
    private final ModelPart medium;
    private final ModelPart small;

    public PyronadoModel (ModelPart root)
    {
        this.root = root;
        this.big = root.getChild("big");
        this.medium = root.getChild("medium");
        this.small = root.getChild("small");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer ()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition big = partdefinition.addOrReplaceChild("big", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 16.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -7.0F, 0.0F));

        PartDefinition medium = partdefinition.addOrReplaceChild("medium", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(-12.0F, -32.0F, -13.0F, 24.0F, 16.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition small = partdefinition.addOrReplaceChild("small", CubeListBuilder.create().texOffs(0, 16).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 80);
    }

    @Override
    public @NotNull ModelPart root ()
    {
        return this.root;
    }

    @Override
    public void setupAnim (@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        ModelPart[] arr = {this.big, this.medium, this.small};
        for (ModelPart part : arr) part.yRot += ageInTicks * (0.01f * entity.sizeCoefficient);
    }

    @Override
    public void renderToBuffer (@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        big.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        medium.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        small.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
