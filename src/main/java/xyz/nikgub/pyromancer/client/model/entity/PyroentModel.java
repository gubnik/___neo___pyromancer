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
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;

public class PyroentModel<T extends Entity> extends HierarchicalModel<T>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(PyromancerMod.MOD_ID, "pyroent"), "main");
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
        PartDefinition LegRight = partdefinition.addOrReplaceChild("LegRight", CubeListBuilder.create().texOffs(40, 89).mirror().addBox(-5.0F, 19.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(0, 100).mirror()
                .addBox(-4.0F, 5.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(72, 89).addBox(-5.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -5.0F, 0.0F));
        PartDefinition bone2 = LegRight.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 29.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition LegLeft = partdefinition.addOrReplaceChild("LegLeft", CubeListBuilder.create().texOffs(40, 89).addBox(-3.0F, 19.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 100)
                .addBox(-2.0F, 5.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(72, 89).mirror().addBox(-3.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -5.0F, 0.0F));
        PartDefinition bone = LegLeft.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, 29.0F, 0.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(64, 26).addBox(-8.0F, -36.0F, -5.0F, 16.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                .addBox(-10.0F, -58.0F, -6.0F, 20.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(64, 44).addBox(-7.0F, -42.0F, -3.0F, 14.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition bone13 = Body.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(64, 0).addBox(-6.0F, -62.0F, -8.0F, 10.0F, 11.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition bone12 = Body.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(49, 63).addBox(-4.0F, -62.0F, -8.0F, 10.0F, 11.0F, 15.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition bone7 = Body.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition bone8 = Body.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition bone9 = bone8.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 78).addBox(-5.0F, -59.0F, -9.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -1.0F));
        PartDefinition horn_right = Head.addOrReplaceChild("horn_right", CubeListBuilder.create().texOffs(54, 28).addBox(0.0F, -62.0F, -4.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, -0.0873F));
        PartDefinition leaf_r1 = horn_right.addOrReplaceChild("leaf_r1",
                CubeListBuilder.create().texOffs(67, 0).addBox(-2.5F, -5.25F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(52, 0).addBox(0.5F, -6.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -60.25F, -2.5F, 0.0F, 0.0F, 0.2618F));
        PartDefinition horn_left = Head.addOrReplaceChild("horn_left", CubeListBuilder.create().texOffs(22, 28).addBox(-3.0F, -63.0F, -4.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0873F));
        PartDefinition horn_r1 = horn_left.addOrReplaceChild("horn_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -8.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.25F, -60.5F, -2.5F, 0.0F, 0.0F, -0.2618F));
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

    public void setupAnim (@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.ArmLeft.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.LegLeft.xRot = Mth.cos(limbSwing) * -1.0F * limbSwingAmount;
        this.ArmRight.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.LegRight.xRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
    }
}
