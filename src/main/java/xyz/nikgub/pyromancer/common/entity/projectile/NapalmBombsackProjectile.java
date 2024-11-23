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
package xyz.nikgub.pyromancer.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

public class NapalmBombsackProjectile extends BombsackProjectile
{

    public NapalmBombsackProjectile (EntityType<? extends ThrowableItemProjectile> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    protected @NotNull Item getDefaultItem ()
    {
        return ItemRegistry.SCATTERSHOT_BOMBSACK.get();
    }

    @Override
    public void collisionEffect ()
    {
        this.level().explode(this, DamageSourceRegistry.bombsack(this, this.getOwner() != null ? this.getOwner() : this), null, this.getX(), this.getY(), this.getZ(), 1.2f, true, Level.ExplosionInteraction.NONE);
        final int lim = 4;
        for (int dx = -lim; lim > dx; dx++)
        {
            for (int dy = -lim; dy < lim; dy++)
            {
                for (int dz = -lim; dz < lim; dz++)
                {
                    if (random.nextInt(0, 12) != 0) continue;
                    BlockPos blockPos = BlockPos.containing(this.getX() + dx, this.getY() + dy, this.getZ() + dz);
                    BlockState blockState = level().getBlockState(blockPos);
                    if (!(blockState.isAir() || blockState.canBeReplaced() && blockState.isFlammable(level(), blockPos, Direction.UP)))
                        continue;
                    this.level().setBlock(blockPos, Blocks.FIRE.defaultBlockState(), 1 | 2);
                }
            }
        }
    }
}
