package xyz.nikgub.pyromancer.client.model.entity;// Made with Blockbench 4.7.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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
import xyz.nikgub.pyromancer.client.animation.UnburnedAnimations;
import xyz.nikgub.pyromancer.common.entity.UnburnedEntity;

public class UnburnedModel<T extends UnburnedEntity> extends HierarchicalModel<T>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(PyromancerMod.MOD_ID, "unburned"), "main");
    private final ModelPart root;
    private final ModelPart LegRight;
    private final ModelPart LegLeft;
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart ArmRight;
    private final ModelPart ArmLeft;

    public UnburnedModel (ModelPart root)
    {
        this.root = root;
        this.LegRight = root.getChild("LegRight");
        this.LegLeft = root.getChild("LegLeft");
        this.Body = root.getChild("Body");
        this.Head = root.getChild("Head");
        this.ArmRight = root.getChild("ArmRight");
        this.ArmLeft = root.getChild("ArmLeft");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer ()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition LegRight = partdefinition.addOrReplaceChild("LegRight", CubeListBuilder.create().texOffs(30, 114).addBox(-5.0F, 28.0F, 0.0F, 10.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(120, 234).addBox(-5.0F, 8.0F, 0.0F, 10.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 104).addBox(-5.0F, 31.0F, -5.0F, 10.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(164, 134).addBox(-4.0F, 24.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(166, 194).addBox(-4.0F, 8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(164, 210).addBox(-5.0F, 4.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(170, 158).addBox(-3.0F, 12.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -12.0F, 0.0F));

        PartDefinition LegLeft = partdefinition.addOrReplaceChild("LegLeft", CubeListBuilder.create().texOffs(166, 178).addBox(-4.0F, 8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 114).addBox(-5.0F, 28.0F, 0.0F, 10.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(118, 245).addBox(-5.0F, 8.0F, 0.0F, 10.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(30, 104).addBox(-5.0F, 31.0F, -5.0F, 10.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(166, 149).addBox(-4.0F, 24.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(164, 224).addBox(-5.0F, 4.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(170, 158).addBox(-3.0F, 12.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -12.0F, 0.0F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(148, 238).addBox(-11.0F, -2.0F, -6.0F, 22.0F, 6.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(204, 218).addBox(-9.0F, -17.0F, -4.0F, 18.0F, 15.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(196, 121).addBox(-9.0F, -31.1F, -6.0F, 18.0F, 15.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(218, 194).addBox(-11.0F, -22.1F, -6.0F, 7.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(218, 170).addBox(4.0F, -22.1F, -6.0F, 7.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(216, 148).addBox(-4.0F, -22.1F, -6.0F, 8.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition BreastRight = Body.addOrReplaceChild("BreastRight", CubeListBuilder.create().texOffs(0, 31).addBox(-8.0F, -7.5F, -8.0F, 16.0F, 15.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.2096F, -22.97F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition ShoulderRight = Body.addOrReplaceChild("ShoulderRight", CubeListBuilder.create().texOffs(0, 83).addBox(-8.0F, -4.0F, -6.5F, 16.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.9543F, -28.1022F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition ShoulderLeft = Body.addOrReplaceChild("ShoulderLeft", CubeListBuilder.create().texOffs(0, 62).addBox(-8.0F, -4.0F, -6.5F, 16.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.9543F, -28.1022F, -1.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition BreastLeft = Body.addOrReplaceChild("BreastLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -7.5F, -8.0F, 16.0F, 15.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.2096F, -22.97F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(216, 0).addBox(-5.0F, -12.0F, -5.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -42.0F, -5.0F));

        PartDefinition HornBackRight = Head.addOrReplaceChild("HornBackRight", CubeListBuilder.create().texOffs(232, 56).addBox(-1.5F, -7.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0808F, -10.2993F, 4.0677F, -0.0873F, 0.0F, -0.0873F));

        PartDefinition HornBackLeft = Head.addOrReplaceChild("HornBackLeft", CubeListBuilder.create().texOffs(244, 56).addBox(-1.5F, -7.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0808F, -10.2993F, 4.0677F, -0.0873F, 0.0F, 0.0873F));

        PartDefinition HornRight = Head.addOrReplaceChild("HornRight", CubeListBuilder.create().texOffs(244, 56).addBox(-1.5F, -8.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0048F, -8.4307F, -4.0297F, 0.0873F, 0.0F, -0.0873F));

        PartDefinition HornLeft = Head.addOrReplaceChild("HornLeft", CubeListBuilder.create().texOffs(232, 56).addBox(-1.5F, -8.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0048F, -8.4307F, -4.0297F, 0.0873F, 0.0F, 0.0873F));

        PartDefinition ArmRight = partdefinition.addOrReplaceChild("ArmRight", CubeListBuilder.create().texOffs(220, 67).addBox(-4.0F, 3.9093F, -5.0F, 8.0F, 44.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(96, 50).addBox(-5.0F, 4.9093F, -6.0F, 10.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.0F, -42.0F, 0.0F, 0.0F, 0.0F, 0.0047F));

        PartDefinition ArmLeft = partdefinition.addOrReplaceChild("ArmLeft", CubeListBuilder.create().texOffs(52, 50).addBox(-5.0F, 4.9093F, -6.0F, 10.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(184, 67).addBox(-4.0F, 3.9093F, -5.0F, 8.0F, 44.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, -42.0F, 0.0F, 0.0F, 0.0F, -0.0047F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    public @NotNull ModelPart root ()
    {
        return this.root;
    }

    @Override
    public void setupAnim (@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.walkingAnimation(limbSwing, limbSwingAmount);
        this.idleAnimation(ageInTicks);
        // animations vvv
        this.animate(entity.ATTACK, UnburnedAnimations.ATTACK_MAIN, ageInTicks);
        this.animate(entity.KICK, UnburnedAnimations.ATTACK_KICK, ageInTicks);
        this.animate(entity.EXPLOSION, UnburnedAnimations.ATTACK_EXPLOSION, ageInTicks);
        this.animate(entity.EMERGE, UnburnedAnimations.EMERGE, ageInTicks);
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
        this.ArmLeft.x = 18.0F;
        this.ArmLeft.y = -42.0F;
        this.ArmRight.zRot = (float) Math.toRadians(0.272F);
        this.ArmRight.z = 0.0F;
        this.ArmRight.x = -18.0F;
        this.ArmRight.y = -42.0F;
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
}