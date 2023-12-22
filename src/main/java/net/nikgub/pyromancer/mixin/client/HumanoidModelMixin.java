package net.nikgub.pyromancer.mixin.client;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.nikgub.pyromancer.ember.Ember;
import net.nikgub.pyromancer.registries.custom.EmberRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HumanoidModel.class, priority = 42690)
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
