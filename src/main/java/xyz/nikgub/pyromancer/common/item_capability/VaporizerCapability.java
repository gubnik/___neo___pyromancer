package xyz.nikgub.pyromancer.common.item_capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.data.ItemTagDatagen;

import javax.annotation.Nonnull;

public class VaporizerCapability implements ICapabilitySerializable<CompoundTag>
{
    public static final int MAX_SLOTS = 3;

    private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createItemHandler);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability (@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.inventory.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT ()
    {
        return getItemHandler().serializeNBT();
    }

    private ItemStackHandler createItemHandler ()
    {
        return new ItemStackHandler(MAX_SLOTS)
        {

            @Override
            public boolean isItemValid (int slot, @Nonnull ItemStack stack)
            {
                return stack.is(ItemTagDatagen.VAPORIZER_AMMO);
            }

            @Override
            public void setSize (int size)
            {
            }
        };
    }

    private ItemStackHandler getItemHandler ()
    {
        return inventory.orElseThrow(RuntimeException::new);
    }

    @Override
    public void deserializeNBT (CompoundTag nbt)
    {
        getItemHandler().deserializeNBT(nbt);
    }
}