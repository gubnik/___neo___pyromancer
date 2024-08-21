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
import xyz.nikgub.pyromancer.client.animation.FrostcopperGolemAnimations;
import xyz.nikgub.pyromancer.common.entity.FrostcopperGolemEntity;

public class FrostcopperGolemModel<T extends FrostcopperGolemEntity> extends HierarchicalModel<T>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(PyromancerMod.MOD_ID, "frostcopper_golem"), "main");

	private final ModelPart root;

	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public FrostcopperGolemModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createbodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 24).addBox(-4.0F, -8.0F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-5.0F, -6.0F, -4.0F, 10.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -1.0F, -5.0F, 14.0F, 9.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(52, 25).addBox(-4.5F, 0.5F, -6.0F, 9.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 32).addBox(-7.0F, -3.0F, -4.0F, 7.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(48, 58).addBox(-6.0F, 3.0F, -3.0F, 4.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(22, 40).addBox(0.0F, -3.0F, -4.0F, 7.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 65).addBox(2.0F, 3.0F, -3.0F, 4.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(48, 0).addBox(-2.5F, -1.0F, -3.0F, 5.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(22, 54).addBox(-3.0F, 1.0F, -3.5F, 6.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(22, 54).addBox(-3.0F, 6.0F, -3.5F, 6.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 12.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 46).addBox(-2.5F, -1.0F, -3.0F, 5.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(22, 54).addBox(-3.0F, 1.0F, -3.5F, 6.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(22, 54).addBox(-3.0F, 6.0F, -3.5F, 6.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}



	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netheadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.walkingAnimation(limbSwing, limbSwingAmount);
		this.idleAnimation(ageInTicks);
		this.head.yRot = (float) Math.toRadians(netheadYaw);
		this.head.xRot = (float) Math.toRadians(headPitch);
		this.animate(entity.STOMP, FrostcopperGolemAnimations.STOMP, ageInTicks);
		this.animate(entity.ATTACK, FrostcopperGolemAnimations.ATTACK, ageInTicks);
	}

	public void walkingAnimation(float limbSwing, float limbWSwingAmount)
	{
		float f = Math.min(0.4F, 4F * limbWSwingAmount);
		float f1 = limbSwing * 0.7F;
		float f2 = Mth.cos(f1);
		float f3 = Mth.sin(f1);
		float f4 = Math.min(0.25F, f);
		this.head.zRot += 0.2F * f3 * f;
		this.head.xRot += 1.1F * Mth.cos(f1 + ((float)Math.PI / 2F)) * f4;
		this.left_leg.xRot = 0.8F * f2 * f;
		this.right_leg.xRot = 0.8F * Mth.cos(f1 + (float)Math.PI) * f;
		this.left_arm.xRot = -(0.5F * f2 * f);
		this.left_arm.zRot = 0.0F;
		this.right_arm.xRot = -(0.5F * f3 * f);
		this.right_arm.zRot = 0.0F;
		this.resetArmPoses();
	}

	private void resetArmPoses()
	{
		this.left_arm.zRot = (float) Math.toRadians(-0.272F);
		this.left_arm.z = 0.0F;
		this.right_arm.zRot = (float) Math.toRadians(0.272F);
		this.right_arm.z = 0.0F;
	}

	private void idleAnimation(float ticks)
	{
		float f = ticks * 0.1F;
		float f1 = Mth.cos(f);
		float f2 = Mth.sin(f);
		this.head.zRot += 0.015F * f1;
		this.head.xRot += 0.015F * f2;
		this.body.zRot += 0.025F * f2;
		this.body.xRot += 0.025F * f1;
		this.left_arm.zRot += 0.025F * f2;
		this.left_arm.xRot += 0.025F * f1;
		this.right_arm.zRot += 0.025F * f2;
		this.right_arm.xRot += 0.025F * f1;
		this.resetArmPoses();
	}

	@Override
	public void renderToBuffer (@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public @NotNull ModelPart root() {
		return root;
	}
}