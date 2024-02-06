package xyz.nikgub.pyromancer.mixin.client;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.ember.Ember;
import xyz.nikgub.pyromancer.registries.custom.EmberRegistry;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Mixin(value = HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {

    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    public void poseLeftArmMixinHead(T entity, CallbackInfo callbackInfo)
    {
        ItemStack itemStack = entity.getUseItem();
        if(Ember.emberItemStackPredicate(itemStack)) {
            HumanoidModel<T> model = (HumanoidModel<T>) (Object) this;
            if (entity.getUseItemRemainingTicks() > 0 //&& Ember.emberItemStackPredicate(itemStack)
            ) {
                EmberRegistry.getFromItem(itemStack).getAnimation().thirdPersonAnimation().accept(model, entity, HumanoidArm.LEFT);
                callbackInfo.cancel();
            }
        }
    }
    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    public void poseRightArmMixinHead(T entity, CallbackInfo callbackInfo)
    {
        ItemStack itemStack = entity.getUseItem();
        if(Ember.emberItemStackPredicate(itemStack)) {
            HumanoidModel<T> model = (HumanoidModel<T>) (Object) this;
            if (entity.getUseItemRemainingTicks() > 0 //&& Ember.emberItemStackPredicate(itemStack)
            ) {
                EmberRegistry.getFromItem(itemStack).getAnimation().thirdPersonAnimation().accept((HumanoidModel<?>) (Object) this, entity, HumanoidArm.RIGHT);
                callbackInfo.cancel();
            }
        }
    }
}
