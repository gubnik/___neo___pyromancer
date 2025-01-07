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
package xyz.nikgub.pyromancer.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.Collections;
import java.util.Map;

public class PyromancerArmorModel<T extends LivingEntity> extends EntityModel<T>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(PyromancerMod.MOD_ID, "pyromancer_armor"), "main");
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    public final ModelPart rightBoot;
    public final ModelPart leftBoot;
    public final ModelPart leggings;

    public PyromancerArmorModel (ModelPart root)
    {
        this.head = root.getChild("helmet");
        this.body = root.getChild("chestplate");
        this.rightLeg = root.getChild("leg_right");
        this.leftLeg = root.getChild("leg_left");
        this.rightBoot = root.getChild("boot_right");
        this.leftBoot = root.getChild("boot_left");
        this.rightArm = root.getChild("arm_right");
        this.leftArm = root.getChild("arm_left");
        this.leggings = root.getChild("leggings");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer ()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition helmet = partdefinition.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition horn_r1 = helmet.addOrReplaceChild("horn_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -1.3F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(-2.0F, -8.0F, -3.0F, 0.2618F, 0.0873F, 0.0F));

        PartDefinition horn_r2 = helmet.addOrReplaceChild("horn_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -6.5F, -1.9F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(2.0F, -8.0F, -3.3F, -0.0436F, -0.0873F, 0.0F));

        PartDefinition horn_r3 = helmet.addOrReplaceChild("horn_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.5F, -1.9F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(-2.0F, -8.0F, -3.3F, -0.0436F, 0.0873F, 0.0F));

        PartDefinition horn_r4 = helmet.addOrReplaceChild("horn_r4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(2.0F, -8.0F, -3.3F, 0.2618F, -0.0873F, 0.0F));

        PartDefinition horn_r5 = helmet.addOrReplaceChild("horn_r5", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(2.0F, -8.0F, 3.3F, -0.2618F, 0.0873F, 0.0F));

        PartDefinition horn_r6 = helmet.addOrReplaceChild("horn_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(-2.0F, -8.0F, 3.3F, -0.2618F, -0.0873F, 0.0F));

        PartDefinition chestplate = partdefinition.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition arm_left = partdefinition.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(48, 54).mirror().addBox(-1.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offset(5.0F, 0.0F, 0.0F));

        PartDefinition shoulder_r1 = arm_left.addOrReplaceChild("shoulder_r1", CubeListBuilder.create().texOffs(44, 0).mirror().addBox(-2.2F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition arm_right = partdefinition.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(48, 54).addBox(-3.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(-5.0F, 0.0F, 0.0F));

        PartDefinition shoulder_r2 = arm_right.addOrReplaceChild("shoulder_r2", CubeListBuilder.create().texOffs(44, 0).addBox(-2.8F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition leggings = partdefinition.addOrReplaceChild("leggings", CubeListBuilder.create().texOffs(18, 17).addBox(-4.5F, 9.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.4F))
            .texOffs(32, 7).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.5F))
            .texOffs(34, 34).addBox(-2.0F, 8.0F, -3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bombsack_r1 = leggings.addOrReplaceChild("bombsack_r1", CubeListBuilder.create().texOffs(43, 21).addBox(-2.8F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.4F))
            .texOffs(10, 23).addBox(-2.0F, -3.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-4.0F, 13.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition leg_left = partdefinition.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition leg_right = partdefinition.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition boot_right = partdefinition.addOrReplaceChild("boot_right", CubeListBuilder.create().texOffs(28, 41).addBox(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition boot_left = partdefinition.addOrReplaceChild("boot_left", CubeListBuilder.create().texOffs(28, 41).mirror().addBox(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static HumanoidModel<? extends LivingEntity> getHumanoidModel (EquipmentSlot slot)
    {
        PyromancerArmorModel<LivingEntity> testModel = new PyromancerArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(PyromancerArmorModel.LAYER_LOCATION));
        return new HumanoidModel<>(new ModelPart(
            Collections.emptyList(),
            Map.of(
                "left_leg", slot.equals(EquipmentSlot.FEET) ? testModel.leftBoot :
                    slot.equals(EquipmentSlot.LEGS) ? testModel.leftLeg : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "right_leg", slot.equals(EquipmentSlot.FEET) ? testModel.rightBoot :
                    slot.equals(EquipmentSlot.LEGS) ? testModel.rightLeg : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "head", slot.equals(EquipmentSlot.HEAD) ? testModel.head : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "body", slot.equals(EquipmentSlot.CHEST) ? testModel.body :
                    slot.equals(EquipmentSlot.LEGS) ? testModel.leggings : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "left_arm", slot.equals(EquipmentSlot.CHEST) ? testModel.leftArm : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "right_arm", slot.equals(EquipmentSlot.CHEST) ? testModel.rightArm : new ModelPart(Collections.emptyList(), Collections.emptyMap())
            )
        ));
    }

    @Override
    public void setupAnim (@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void renderToBuffer (@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightBoot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftBoot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
