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
import xyz.nikgub.pyromancer.common.item.BlazingJournalItem;
import xyz.nikgub.pyromancer.common.item.QuillItem;

import javax.annotation.Nonnull;

public class BlazingJournalCapability implements ICapabilitySerializable<CompoundTag>
{
    private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createItemHandler);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability (@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.inventory.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT ()
    {
        CompoundTag tag = getItemHandler().serializeNBT();
        tag.putDouble("CustomModelData", 0);
        tag.putInt(BlazingJournalItem.BLAZE_TAG_NAME, 0);
        return tag;
    }

    /**
     * @return Inventory of {@link BlazingJournalItem} with the only slot for quills
     */
    private ItemStackHandler createItemHandler ()
    {
        return new ItemStackHandler(1)
        {
            @Override
            public int getSlotLimit (int slot)
            {
                return 1;
            }

            @Override
            public boolean isItemValid (int slot, @Nonnull ItemStack stack)
            {
                return stack.getItem() instanceof QuillItem;
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
