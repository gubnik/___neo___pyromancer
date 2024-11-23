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

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity
{

    public ItemEntityMixin (EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Shadow
    public abstract ItemStack getItem ();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickMixin (CallbackInfo callbackInfo)
    {
        if (this.getItem().getItem() == ItemRegistry.MEMORY_OF_FIRE.get())
        {
            //this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + 0.55D, this.getZ(), 0.0D, 0.012D, 0.0D);
        }
    }
}
