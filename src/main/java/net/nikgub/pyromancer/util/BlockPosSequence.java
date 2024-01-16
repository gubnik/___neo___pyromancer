package net.nikgub.pyromancer.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockPosSequence {
    private final Type type;
    private final BlockPos initialPosition;
    private final int limit;
    private final List<BlockPos> values;
    public BlockPosSequence(Type type, BlockPos initialPosition, int limit, @Nullable Direction direction)
    {
        this.type = type;
        this.initialPosition = initialPosition;
        this.limit = limit;
        this.values = switch (type)
        {
            case LINE -> generateLine(direction != null ? direction : Direction.UP);
            case LIGHTING -> generateLightning(direction != null ? direction : Direction.UP);
            case CUBE -> generateCube();
            case SPHERE -> generateSphere();
        };
    }
    private List<BlockPos> generateLine(@NotNull Direction direction)
    {
        int x = this.initialPosition.getX(), y = this.initialPosition.getY(), z = this.initialPosition.getZ();
        List<BlockPos> values = new ArrayList<>();
        for(int i = 0; i < this.limit; i++)
        {
            values.add(new BlockPos(x + i * direction.getStepX(), y + i * direction.getStepY(), z + i * direction.getStepZ()));
        }
        return values;
    }
    private List<BlockPos> generateLightning(@NotNull Direction direction)
    {
        int x = this.initialPosition.getX(), y = this.initialPosition.getY(), z = this.initialPosition.getZ();
        List<BlockPos> values = new ArrayList<>();
        for(int i = 0; i < this.limit; i++)
        {
            values.add(new BlockPos(x + i * direction.getStepX(), y + i * direction.getStepY(), z + i * direction.getStepZ()));
        }
        return values;
    }
    private List<BlockPos> generateSphere()
    {
        int x = this.initialPosition.getX(), y = this.initialPosition.getY(), z = this.initialPosition.getZ();
        List<BlockPos> values = new ArrayList<>();
        for(int i = 0; i < this.limit; i++)
        {
            for(int j = 0; j < this.limit; j++)
            {
                for(int k = 0; k < this.limit; k++)
                {
                    if(i*i + j*j + k*k <= this.limit*this.limit) {
                        values.add(new BlockPos(x + i, y + j, z + k));
                        values.add(new BlockPos(x - i, y + j, z + k));
                        values.add(new BlockPos(x + i, y - j, z + k));
                        values.add(new BlockPos(x + i, y + j, z - k));
                        values.add(new BlockPos(x - i, y - j, z + k));
                        values.add(new BlockPos(x - i, y + j, z - k));
                        values.add(new BlockPos(x + i, y - j, z - k));
                        values.add(new BlockPos(x - i, y - j, z - k));
                    }
                }
            }
        }
        return values;
    }
    private List<BlockPos> generateCube()
    {
        int x = this.initialPosition.getX(), y = this.initialPosition.getY(), z = this.initialPosition.getZ();
        List<BlockPos> values = new ArrayList<>();
        for(int i = 0; i < this.limit / 2; i++)
        {
            for(int j = 0; j < this.limit / 2; j++)
            {
                for(int k = 0; k < this.limit / 2; k++)
                {
                    values.add(new BlockPos(x + i, y + j, z + k));
                    values.add(new BlockPos(x - i, y + j, z + k));
                    values.add(new BlockPos(x + i, y - j, z + k));
                    values.add(new BlockPos(x + i, y + j, z - k));
                    values.add(new BlockPos(x - i, y - j, z + k));
                    values.add(new BlockPos(x - i, y + j, z - k));
                    values.add(new BlockPos(x + i, y - j, z - k));
                    values.add(new BlockPos(x - i, y - j, z - k));
                }
            }
        }
        return values;
    }
    public List<BlockPos> getValues() {
        return values;
    }
    public Type getType() {
        return type;
    }
    public BlockPos getInitialPosition() {
        return initialPosition;
    }
    public int getLimit() {
        return limit;
    }
    public enum Type
    {
        LINE,
        LIGHTING,
        SPHERE,
        CUBE
    }
}
