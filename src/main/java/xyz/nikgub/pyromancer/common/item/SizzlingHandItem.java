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
package xyz.nikgub.pyromancer.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.client.item_extension.SizzlingHandClientExtension;
import xyz.nikgub.pyromancer.common.entity.projectile.SizzlingHandFireball;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;
import xyz.nikgub.pyromancer.registry.EntityTypeRegistry;

import java.util.function.Consumer;

public class SizzlingHandItem extends UsablePyromancyItem
{
    public SizzlingHandItem (Properties properties)
    {
        super(properties);
    }

    @Override
    public float getDefaultPyromancyDamage ()
    {
        return 4;
    }

    @Override
    public int getDefaultBlazeCost ()
    {
        return 1;
    }

    @Override
    public void initializeClient (@NotNull Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new SizzlingHandClientExtension());
    }

    @Override
    public @NotNull UseAnim getUseAnimation (@NotNull ItemStack itemStack)
    {
        return UseAnim.CUSTOM;
    }

    @Override
    public @NotNull ItemStack finishUsingItem (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity)
    {
        this.releaseUsing(itemStack, level, entity, this.getUseDuration(itemStack));
        return itemStack;
    }

    @Override
    public int getUseDuration (@NotNull ItemStack itemStack)
    {
        return 10;
    }

    @Override
    public void compendiumTransforms (PoseStack poseStack, ItemDisplayContext displayContext)
    {
        if (displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
            poseStack.scale(1.33f, 1.33f, 1.33f);
        if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
            poseStack.rotateAround(Axis.YP.rotationDegrees(-90), 0.5f, 0.4f, 0.5f);
        else poseStack.rotateAround(Axis.YP.rotationDegrees(90), 0.5f, 0.4f, 0.5f);
    }

    @Override
    public void onUseTick (@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        SizzlingHandFireball fireball = new SizzlingHandFireball(EntityTypeRegistry.SIZZLING_HAND_FIREBALL.get(), entity.level(), (float) entity.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()), 10);
        fireball.setOwner(entity);
        fireball.setPos(entity.getEyePosition());
        fireball.setDeltaMovement(entity.getLookAngle().multiply(2d, 2d, 2d));
        if (entity.level().addFreshEntity(fireball) && entity instanceof Player player)
        {
            BlazingJournalItem.changeBlaze(player, -(int) player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()));
        }
    }

    @Override
    public void releaseUsing (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity, int tick)
    {
        if (!(entity instanceof Player player)) return;
        player.getCooldowns().addCooldown(itemStack.getItem(), 40 + this.getUseDuration(itemStack) - tick);
        SizzlingHandFireball fireball = new SizzlingHandFireball(EntityTypeRegistry.SIZZLING_HAND_FIREBALL.get(), entity.level(), (float) entity.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()), 10);
        fireball.collisionEffect(level);
        entity.stopUsingItem();
    }
}
