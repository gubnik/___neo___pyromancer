package net.nikgub.pyromancer.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public interface IContainerItem {
    private Item self()
    {
        return (Item) this;
    }
    ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt);
    default ItemStack getItemFromItem(ItemStack target, int id)
    {
        AtomicReference<ItemStack> AR = new AtomicReference<>(ItemStack.EMPTY);
        target.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> AR.set(capability.getStackInSlot(id).copy()));
        return AR.get();
    }
    default void setItemInItem(ItemStack target, ItemStack value, int id)
    {
        target.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
            if(capability instanceof IItemHandlerModifiable handlerModifiable){
                handlerModifiable.setStackInSlot(id, value);
            }
        });
    }
}
