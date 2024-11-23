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

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.common.entity.projectile.BombsackProjectile;

import java.util.function.Supplier;

public class BombsackItem extends Item
{

    private final Supplier<EntityType<? extends BombsackProjectile>> typeSupplier;

    public BombsackItem (Properties properties, Supplier<EntityType<? extends BombsackProjectile>> typeSupplier)
    {
        super(properties);
        this.typeSupplier = typeSupplier;
    }

    public @NotNull InteractionResultHolder<ItemStack> use (Level level, Player player, @NotNull InteractionHand interactionHand)
    {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide)
        {
            BombsackProjectile bombsack = typeSupplier.get().create(level);
            if (bombsack == null) return InteractionResultHolder.fail(itemstack);
            bombsack.setItem(itemstack);
            bombsack.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            bombsack.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 0.8f, 0.1f);
            level.addFreshEntity(bombsack);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild)
        {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
