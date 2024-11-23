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
package xyz.nikgub.pyromancer.mixin.client;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.common.ember.Ember;

@SuppressWarnings("unused")
@Mixin(value = HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel
{

    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    public void poseLeftArmMixinHead (T entity, CallbackInfo callbackInfo)
    {
        ItemStack itemStack = entity.getUseItem();
        Ember ember = Ember.getFromItem(itemStack);
        if (ember != null)
        {
            HumanoidModel<T> model = (HumanoidModel<T>) (Object) this;
            if (entity.getUseItemRemainingTicks() > 0 //&& Ember.emberItemStackPredicate(itemStack)
            )
            {
                ember.getAnimation().getThirdPersonAnimation().run(model, entity, HumanoidArm.LEFT);
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    public void poseRightArmMixinHead (T entity, CallbackInfo callbackInfo)
    {
        ItemStack itemStack = entity.getUseItem();
        Ember ember = Ember.getFromItem(itemStack);
        if (ember != null)
        {
            HumanoidModel<T> model = (HumanoidModel<T>) (Object) this;
            if (entity.getUseItemRemainingTicks() > 0)
            {
                ember.getAnimation().getThirdPersonAnimation().run(model, entity, HumanoidArm.RIGHT);
                callbackInfo.cancel();
            }
        }
    }
}
