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
import xyz.nikgub.pyromancer.common.entity.RimegazerEntity;

public class RimegazerModel<T extends RimegazerEntity> extends HierarchicalModel<T>
{

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(PyromancerMod.MOD_ID, "rimegazer"), "main");

    private final ModelPart root;
    private final ModelPart eye;
    private final ModelPart vortex1;
    private final ModelPart vortex2;
    private final ModelPart tentacles;
    private final ModelPart bottom_left;
    private final ModelPart bottom_right;
    private final ModelPart middle_left;
    private final ModelPart upper_left;
    private final ModelPart middle_right;
    private final ModelPart upper_right;

    public RimegazerModel (ModelPart root)
    {
        this.root = root;
        this.eye = root.getChild("eye");
        this.vortex1 = root.getChild("vortex1");
        this.vortex2 = root.getChild("vortex2");
        this.tentacles = root.getChild("tentacles");
        this.bottom_left = tentacles.getChild("bottom_left");
        this.bottom_right = tentacles.getChild("bottom_right");
        this.middle_left = tentacles.getChild("middle_left");
        this.upper_left = tentacles.getChild("upper_left");
        this.middle_right = tentacles.getChild("middle_right");
        this.upper_right = tentacles.getChild("upper_right");
    }

    public static LayerDefinition createBodyLayer ()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition eye = partdefinition.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 2.0F));

        PartDefinition vortex1 = partdefinition.addOrReplaceChild("vortex1", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 3.0F));

        PartDefinition vortex2 = partdefinition.addOrReplaceChild("vortex2", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 4.0F));

        PartDefinition tentacles = partdefinition.addOrReplaceChild("tentacles", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition bottom_left = tentacles.addOrReplaceChild("bottom_left", CubeListBuilder.create().texOffs(24, 16).addBox(0.0F, -3.0F, 0.0F, 14.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, -0.1426F, -0.1544F, 0.8862F));

        PartDefinition tip_r1 = bottom_left.addOrReplaceChild("tip_r1", CubeListBuilder.create().texOffs(32, 28).addBox(0.0F, -2.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.0F, 1.309F, 0.0F));

        PartDefinition bottom_right = tentacles.addOrReplaceChild("bottom_right", CubeListBuilder.create().texOffs(32, 0).addBox(-14.0F, -3.0F, 0.0F, 14.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, -0.1426F, 0.1544F, -0.8862F));

        PartDefinition tip_r2 = bottom_right.addOrReplaceChild("tip_r2", CubeListBuilder.create().texOffs(0, 44).addBox(-10.0F, -2.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.0F, -1.309F, 0.0F));

        PartDefinition middle_left = tentacles.addOrReplaceChild("middle_left", CubeListBuilder.create().texOffs(24, 32).addBox(0.0F, -3.0F, 0.0F, 14.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition tip_r3 = middle_left.addOrReplaceChild("tip_r3", CubeListBuilder.create().texOffs(0, 48).addBox(0.0F, -2.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.0F, 1.309F, 0.0F));

        PartDefinition upper_left = tentacles.addOrReplaceChild("upper_left", CubeListBuilder.create().texOffs(32, 6).addBox(0.0F, -3.0F, 0.0F, 14.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.1426F, -0.1544F, -0.8862F));

        PartDefinition tip_r4 = upper_left.addOrReplaceChild("tip_r4", CubeListBuilder.create().texOffs(20, 44).addBox(0.0F, -2.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.0F, 1.309F, 0.0F));

        PartDefinition middle_right = tentacles.addOrReplaceChild("middle_right", CubeListBuilder.create().texOffs(24, 38).addBox(-14.0F, -3.0F, 0.0F, 14.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.0F, 0.1745F, 0.0F));

        PartDefinition tip_r5 = middle_right.addOrReplaceChild("tip_r5", CubeListBuilder.create().texOffs(32, 12).addBox(-10.0F, -2.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.0F, -1.309F, 0.0F));

        PartDefinition upper_right = tentacles.addOrReplaceChild("upper_right", CubeListBuilder.create().texOffs(32, 22).addBox(-14.0F, -3.0F, 0.0F, 14.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.1426F, 0.1544F, 0.8862F));

        PartDefinition middle_r1 = upper_right.addOrReplaceChild("middle_r1", CubeListBuilder.create().texOffs(40, 44).addBox(-10.0F, -2.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.0F, -1.309F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public @NotNull ModelPart root ()
    {
        return root;
    }

    @Override
    public void setupAnim (T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        //this.animate(entity.ATTACK, RimegazerAnimations.ATTACK, ageInTicks);
        //this.animate(entity.SPIN, RimegazerAnimations.SPIN, ageInTicks);
    }

    @Override
    public void renderToBuffer (PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        eye.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        vortex1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        vortex2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        tentacles.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}