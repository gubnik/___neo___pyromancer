package net.nikgub.pyromancer.items.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.nikgub.pyromancer.items.UsablePyromancyItem;
import net.nikgub.pyromancer.items.quills.QuillItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class CompendiumOfFlameCapability implements ICapabilitySerializable<CompoundTag>  {
    public static final int MAX_ITEMS = 5;
    private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createItemHandler);
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.inventory.cast() : LazyOptional.empty();
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = getItemHandler().serializeNBT();
        tag.putInt("active_slot", 0);
        tag.putDouble("CustomModelData", 0);
        return tag;
    }
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getItemHandler().deserializeNBT(nbt);
    }
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(MAX_ITEMS + 1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return (slot == 0 && stack.getItem() instanceof QuillItem) || (slot != 0 && stack.getItem() instanceof UsablePyromancyItem);
            }
            @Override
            public void setSize(int size) {
            }
        };
    }
    private ItemStackHandler getItemHandler() {
        return inventory.orElseThrow(RuntimeException::new);
    }
}

