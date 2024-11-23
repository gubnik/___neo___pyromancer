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
package xyz.nikgub.pyromancer.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.common.item.SpearOfMorozItem;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
    @Inject(method = "tryAddFrost", at = @At("HEAD"), cancellable = true)
    protected void tryAddFrost (CallbackInfo callbackInfo)
    {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;
        ItemStack spear = player.getMainHandItem();
        if (!(spear.getItem() == ItemRegistry.SPEAR_OF_MOROZ.get() && spear.getOrCreateTag().getInt(SpearOfMorozItem.ACTION_TAG) == 1))
            return;
        callbackInfo.cancel();
    }
}
