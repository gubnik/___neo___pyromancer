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
import xyz.nikgub.pyromancer.common.item.CompendiumOfFlameItem;
import xyz.nikgub.pyromancer.common.item.QuillItem;
import xyz.nikgub.pyromancer.common.item.UsablePyromancyItem;

import javax.annotation.Nonnull;

public class CompendiumOfFlameCapability implements ICapabilitySerializable<CompoundTag>
{
    public static final int MAX_ITEMS = 5;

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
        tag.putInt(CompendiumOfFlameItem.ACTIVE_SLOT_TAG, 1);
        tag.putInt(BlazingJournalItem.BLAZE_TAG_NAME, 0);
        return tag;
    }

    @Override
    public void deserializeNBT (CompoundTag nbt)
    {
        getItemHandler().deserializeNBT(nbt);
    }

    private ItemStackHandler createItemHandler ()
    {
        return new ItemStackHandler(MAX_ITEMS + 1)
        {
            @Override
            public int getSlotLimit (int slot)
            {
                return 1;
            }

            @Override
            public boolean isItemValid (int slot, @Nonnull ItemStack stack)
            {
                return (slot == 0 && stack.getItem() instanceof QuillItem) || (slot != 0 && stack.getItem() instanceof UsablePyromancyItem);
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
}
