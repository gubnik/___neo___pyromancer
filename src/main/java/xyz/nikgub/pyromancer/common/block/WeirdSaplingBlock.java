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
package xyz.nikgub.pyromancer.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registry.BlockRegistry;

public class WeirdSaplingBlock extends BushBlock implements BonemealableBlock
{
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D);

    private final AbstractTreeGrower feature;

    public WeirdSaplingBlock (Properties properties, AbstractTreeGrower grower)
    {
        super(properties);
        this.feature = grower;
    }

    public @NotNull VoxelShape getShape (@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext)
    {
        return SHAPE;
    }

    public boolean mayPlaceOn (@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos)
    {
        return BlockRegistry.Utils.flamingGrovePlantable(blockState);
    }

    @Override
    public boolean isValidBonemealTarget (@NotNull LevelReader p_256559_, @NotNull BlockPos p_50898_, @NotNull BlockState p_50899_, boolean p_50900_)
    {
        return true;
    }

    public boolean isBonemealSuccess (@NotNull Level level, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState)
    {
        return (double) randomSource.nextFloat() < 0.4D;
    }

    public void performBonemeal (@NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState)
    {
        this.feature.growTree(serverLevel, serverLevel.getChunkSource().getGenerator(), blockPos, blockState, randomSource);
    }
}
