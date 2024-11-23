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
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.entity.ScorchEntity;

public class ScorchModel<T extends ScorchEntity> extends HierarchicalModel<T>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(PyromancerMod.MOD_ID, "scorch"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_wing;
    private final ModelPart left_wing;
    private final ModelPart right_front_leg;
    private final ModelPart left_front_leg;
    private final ModelPart right_middle_leg;
    private final ModelPart left_middle_leg;
    private final ModelPart right_back_leg;
    private final ModelPart left_back_leg;

    public ScorchModel (ModelPart root)
    {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.right_wing = root.getChild("right_wing");
        this.left_wing = root.getChild("left_wing");
        this.right_front_leg = root.getChild("right_front_leg");
        this.left_front_leg = root.getChild("left_front_leg");
        this.right_middle_leg = root.getChild("right_middle_leg");
        this.left_middle_leg = root.getChild("left_middle_leg");
        this.right_back_leg = root.getChild("right_back_leg");
        this.left_back_leg = root.getChild("left_back_leg");
    }

    public static LayerDefinition createBodyLayer ()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(40, 0).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(96, 0).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.3F))
                .texOffs(0, 36).addBox(-1.0F, 4.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, -8.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(59, 6).addBox(-1.0F, 4.0F, -9.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.2618F, 0.0F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(59, 6).addBox(1.0F, 4.0F, -9.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.2618F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 58).addBox(-4.0F, -4.25F, -10.5F, 8.0F, 8.0F, 10.0F, new CubeDeformation(0.3F))
                .texOffs(81, 28).addBox(-4.0F, -3.25F, -0.5F, 8.0F, 7.0F, 12.0F, new CubeDeformation(0.3F))
                .texOffs(28, 28).addBox(-4.0F, -3.25F, -0.5F, 8.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 40).addBox(-4.0F, -4.25F, -10.5F, 8.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.25F, 2.5F));

        PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 20).addBox(-8.0F, 0.0F, 0.0F, 10.0F, 0.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(70, 76).addBox(-8.0F, -0.25F, 0.0F, 10.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 11.0F, -5.0F, 0.0873F, 0.0F, -0.0873F));

        PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 11.0F, -5.0F, 0.0873F, 0.0F, 0.0873F));

        PartDefinition wing_r1 = left_wing.addOrReplaceChild("wing_r1", CubeListBuilder.create().texOffs(69, 97).addBox(-2.0F, -0.25F, 0.0F, 10.0F, 0.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, 0.0F, 0.0F, 10.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0436F, 0.0F));

        PartDefinition right_front_leg = partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, 14.0F, -7.0F));

        PartDefinition leg_r1 = right_front_leg.addOrReplaceChild("leg_r1", CubeListBuilder.create().texOffs(79, 47).addBox(-9.0F, -2.0F, 0.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition left_front_leg = partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.offset(4.0F, 14.0F, -7.0F));

        PartDefinition leg_r2 = left_front_leg.addOrReplaceChild("leg_r2", CubeListBuilder.create().texOffs(59, 14).addBox(0.0F, -2.0F, 0.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition right_middle_leg = partdefinition.addOrReplaceChild("right_middle_leg", CubeListBuilder.create().texOffs(54, 47).addBox(-9.0F, -4.0F, 0.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 14.0F, -1.0F));

        PartDefinition left_middle_leg = partdefinition.addOrReplaceChild("left_middle_leg", CubeListBuilder.create().texOffs(40, 14).addBox(0.0F, -4.0F, 0.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 14.0F, -1.0F));

        PartDefinition right_back_leg = partdefinition.addOrReplaceChild("right_back_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, 14.0F, 5.0F));

        PartDefinition leg_r3 = right_back_leg.addOrReplaceChild("leg_r3", CubeListBuilder.create().texOffs(0, 14).addBox(-9.0F, -2.0F, 0.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition left_back_leg = partdefinition.addOrReplaceChild("left_back_leg", CubeListBuilder.create(), PartPose.offset(4.0F, 14.0F, 5.0F));

        PartDefinition leg_r4 = left_back_leg.addOrReplaceChild("leg_r4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, 0.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim (@NotNull ScorchEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.walkingAnim(limbSwing, limbSwingAmount);
        this.attackAnim(ageInTicks);
        this.head.yRot = (float) Math.toRadians(netHeadYaw);
        this.head.xRot = (float) Math.toRadians(headPitch);
    }

    public void walkingAnim (float limbSwing, float limbSwingAmount)
    {
        this.right_front_leg.yRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
        this.left_back_leg.yRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.right_back_leg.yRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
        this.left_middle_leg.yRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
        this.right_middle_leg.yRot = Mth.cos(limbSwing) * -1.0F * limbSwingAmount;
        this.left_front_leg.yRot = Mth.cos(limbSwing) * -1.0F * limbSwingAmount;
    }

    public void attackAnim (float pAgeInTicks)
    {
        float f = Mth.sin(this.attackTime * (float) Math.PI);
        float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
        this.right_wing.zRot = 0.0F;
        this.left_wing.zRot = 0.0F;
        this.right_wing.yRot = -(0.1F - f * 0.6F);
        this.left_wing.yRot = 0.1F - f * 0.6F;
        this.right_wing.xRot += f * 1.2F - f1 * 0.4F;
        this.left_wing.xRot += f * 1.2F - f1 * 0.4F;
    }

    @Override
    public void renderToBuffer (@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right_front_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left_front_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right_middle_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left_middle_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right_back_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left_back_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public @NotNull ModelPart root ()
    {
        return root;
    }
}
