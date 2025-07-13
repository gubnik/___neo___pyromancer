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
import xyz.nikgub.pyromancer.client.animation.PyroentAnimations;
import xyz.nikgub.pyromancer.common.entity.PyroentEntity;

public class PyroentModel<T extends PyroentEntity> extends HierarchicalModel<T>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PyromancerMod.MOD_ID, "pyroent"), "main");
    public final ModelPart root;
    public final ModelPart LegRight;
    public final ModelPart LegLeft;
    public final ModelPart Body;
    public final ModelPart Head;
    public final ModelPart ArmRight;
    public final ModelPart ArmLeft;

    public PyroentModel (ModelPart root)
    {
        this.root = root;
        this.LegRight = root.getChild("LegRight");
        this.LegLeft = root.getChild("LegLeft");
        this.Body = root.getChild("Body");
        this.Head = root.getChild("Head");
        this.ArmRight = root.getChild("ArmRight");
        this.ArmLeft = root.getChild("ArmLeft");
    }

    public static LayerDefinition createBodyLayer ()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition LegRight = partdefinition.addOrReplaceChild("LegRight", CubeListBuilder.create().texOffs(40, 89).mirror().addBox(-5.0F, 19.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(0, 100).mirror().addBox(-4.0F, 5.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(72, 89).addBox(-5.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -5.0F, 0.0F));

        PartDefinition bone2 = LegRight.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 29.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition LegLeft = partdefinition.addOrReplaceChild("LegLeft", CubeListBuilder.create().texOffs(40, 89).addBox(-3.0F, 19.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(0, 100).addBox(-2.0F, 5.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(72, 89).mirror().addBox(-3.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -5.0F, 0.0F));

        PartDefinition bone = LegLeft.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, 29.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(64, 26).addBox(-8.0F, 22.0F, -5.0F, 16.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0).addBox(-10.0F, 0.0F, -6.0F, 20.0F, 16.0F, 12.0F, new CubeDeformation(0.0F))
            .texOffs(64, 44).addBox(-7.0F, 16.0F, -3.0F, 14.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -34.0F, 0.0F));

        PartDefinition bone13 = Body.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(64, 0).addBox(-6.0F, -62.0F, -8.0F, 10.0F, 11.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 58.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition bone12 = Body.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(49, 63).addBox(-4.0F, -62.0F, -8.0F, 10.0F, 11.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 58.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition bone7 = Body.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 58.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition bone8 = Body.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 58.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition bone9 = bone8.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 78).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -36.0F, -5.0F));

        PartDefinition horn_right = Head.addOrReplaceChild("horn_right", CubeListBuilder.create().texOffs(54, 28).addBox(0.0F, -62.0F, -4.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 53.0F, 4.0F, 0.0873F, 0.0F, -0.0873F));

        PartDefinition leaf_r1 = horn_right.addOrReplaceChild("leaf_r1", CubeListBuilder.create().texOffs(67, 0).addBox(-2.5F, -5.25F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(52, 0).addBox(0.5F, -6.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -60.25F, -2.5F, 0.0F, 0.0F, 0.2618F));

        PartDefinition horn_left = Head.addOrReplaceChild("horn_left", CubeListBuilder.create().texOffs(22, 28).addBox(-3.0F, -63.0F, -4.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 53.0F, 4.0F, 0.0873F, 0.0F, 0.0873F));

        PartDefinition horn_r1 = horn_left.addOrReplaceChild("horn_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -8.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.25F, -60.5F, -2.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition ArmRight = partdefinition.addOrReplaceChild("ArmRight", CubeListBuilder.create().texOffs(32, 28).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 40.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -26.0F, 0.0F));

        PartDefinition ArmLeft = partdefinition.addOrReplaceChild("ArmLeft", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 40.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -26.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer (@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        LegRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LegLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        ArmRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        ArmLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public @NotNull ModelPart root ()
    {
        return root;
    }

    @Override
    public void setupAnim (@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.walkingAnimation(limbSwing, limbSwingAmount);
        this.idleAnimation(ageInTicks);
        // animations vvv
        this.animate(entity.ATTACK, PyroentAnimations.ATTACK, ageInTicks);
        // animations ^^^
        this.Head.yRot = (float) Math.toRadians(netHeadYaw);
        this.Head.xRot = (float) Math.toRadians(headPitch);
    }

    public void walkingAnimation (float limbSwing, float limbWSwingAmount)
    {
        float f = Math.min(0.4F, 4F * limbWSwingAmount);
        float f1 = limbSwing * 0.7F;
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Math.min(0.25F, f);
        this.Head.zRot += 0.2F * f3 * f;
        this.Head.xRot += 1.1F * Mth.cos(f1 + ((float) Math.PI / 2F)) * f4;
        this.LegLeft.xRot = 0.8F * f2 * f;
        this.LegRight.xRot = 0.8F * Mth.cos(f1 + (float) Math.PI) * f;
        this.ArmLeft.xRot = -(0.5F * f2 * f);
        this.ArmLeft.zRot = 0.0F;
        this.ArmRight.xRot = -(0.5F * f3 * f);
        this.ArmRight.zRot = 0.0F;
        this.resetArmPoses();
    }

    private void resetArmPoses ()
    {
        this.ArmLeft.zRot = (float) Math.toRadians(-0.272F);
        this.ArmLeft.z = 0.0F;
        this.ArmLeft.x = 13.0F;
        this.ArmLeft.y = -26.0F;
        this.ArmRight.zRot = (float) Math.toRadians(0.272F);
        this.ArmRight.z = 0.0F;
        this.ArmRight.x = -13.0F;
        this.ArmRight.y = -26.0F;
    }

    private void idleAnimation (float ticks)
    {
        float f = ticks * 0.1F;
        float f1 = Mth.cos(f);
        float f2 = Mth.sin(f);
        this.Head.zRot += 0.015F * f1;
        this.Head.xRot += 0.015F * f2;
        this.Body.zRot += 0.025F * f2;
        this.Body.xRot += 0.025F * f1;
        this.ArmLeft.zRot += 0.025F * f2;
        this.ArmLeft.xRot += 0.025F * f1;
        this.ArmRight.zRot += 0.025F * f2;
        this.ArmRight.xRot += 0.025F * f1;
        this.resetArmPoses();
    }
}
