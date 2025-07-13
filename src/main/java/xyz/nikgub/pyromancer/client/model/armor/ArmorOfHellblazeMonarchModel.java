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
package xyz.nikgub.pyromancer.client.model.armor;// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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

import java.util.Collections;
import java.util.Map;

public class ArmorOfHellblazeMonarchModel<T extends LivingEntity> extends EntityModel<T>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("modid", "armorofhellblazemonarchmodel"), "main");
    private final ModelPart arm_left;
    private final ModelPart arm_right;
    private final ModelPart chestplate;
    private final ModelPart helmet;
    private final ModelPart leg_left;
    private final ModelPart leg_right;
    private final ModelPart boot_right;
    private final ModelPart boot_left;

    public ArmorOfHellblazeMonarchModel (ModelPart root)
    {
        this.arm_left = root.getChild("arm_left");
        this.arm_right = root.getChild("arm_right");
        this.chestplate = root.getChild("chestplate");
        this.helmet = root.getChild("helmet");
        this.leg_left = root.getChild("leg_left");
        this.leg_right = root.getChild("leg_right");
        this.boot_right = root.getChild("boot_right");
        this.boot_left = root.getChild("boot_left");
    }

    public static LayerDefinition createBodyLayer ()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition arm_left = partdefinition.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(20, 0).mirror().addBox(-1.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offset(6.0F, 0.0F, 0.0F));

        PartDefinition shoulder_r2_r1 = arm_left.addOrReplaceChild("shoulder_r2_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.7F)).mirror(false)
            .texOffs(0, 10).mirror().addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(1.3F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition arm_right = partdefinition.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

        PartDefinition shoulder_r3_r1 = arm_right.addOrReplaceChild("shoulder_r3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.7F))
            .texOffs(0, 10).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(-1.3F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition chestplate = partdefinition.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.45F))
            .texOffs(0, 20).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.8F))
            .texOffs(20, 14).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.9F))
            .texOffs(20, 14).addBox(-4.0F, 7.0F, -2.0F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition helmet = partdefinition.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(0, 52).addBox(-4.0F, -11.0F, 2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.45F))
            .texOffs(0, 52).mirror().addBox(2.0F, -11.0F, 2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.45F)).mirror(false)
            .texOffs(0, 36).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.4F))
            .texOffs(32, 36).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.7F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition horn_r1_r1 = helmet.addOrReplaceChild("horn_r1_r1", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false)
            .texOffs(0, 52).addBox(-7.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(3.0F, -13.0F, -3.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition horn_r2_r1 = helmet.addOrReplaceChild("horn_r2_r1", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.45F)).mirror(false)
            .texOffs(0, 52).addBox(-7.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(3.0F, -9.0F, -3.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition leg_left = partdefinition.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(48, 12).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F))
            .texOffs(20, 0).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition leg_right = partdefinition.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(48, 12).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false)
            .texOffs(20, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition boot_right = partdefinition.addOrReplaceChild("boot_right", CubeListBuilder.create().texOffs(4, 11).addBox(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.8F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition boot_left = partdefinition.addOrReplaceChild("boot_left", CubeListBuilder.create().texOffs(4, 11).mirror().addBox(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.8F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static HumanoidModel<? extends LivingEntity> getHumanoidModel (EquipmentSlot slot)
    {
        ArmorOfHellblazeMonarchModel<LivingEntity> testModel = new ArmorOfHellblazeMonarchModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ArmorOfHellblazeMonarchModel.LAYER_LOCATION));
        return new HumanoidModel<>(new ModelPart(
            Collections.emptyList(),
            Map.of(
                "left_leg", slot.equals(EquipmentSlot.FEET) ? testModel.boot_left :
                    slot.equals(EquipmentSlot.LEGS) ? testModel.leg_left : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "right_leg", slot.equals(EquipmentSlot.FEET) ? testModel.boot_right :
                    slot.equals(EquipmentSlot.LEGS) ? testModel.leg_right : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "head", slot.equals(EquipmentSlot.HEAD) ? testModel.helmet : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "body", slot.equals(EquipmentSlot.CHEST) ? testModel.chestplate : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "left_arm", slot.equals(EquipmentSlot.CHEST) ? testModel.arm_left : new ModelPart(Collections.emptyList(), Collections.emptyMap()),

                "right_arm", slot.equals(EquipmentSlot.CHEST) ? testModel.arm_right : new ModelPart(Collections.emptyList(), Collections.emptyMap())
            )
        ));
    }

    @Override
    public void setupAnim (T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {

    }

    @Override
    public void renderToBuffer (PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        arm_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        arm_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        chestplate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        helmet.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        boot_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        boot_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
