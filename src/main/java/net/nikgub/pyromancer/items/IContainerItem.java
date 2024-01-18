package net.nikgub.pyromancer.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface for mod items that have associated inventory
 */
public interface IContainerItem {
    default Item self()
    {
        return (Item) this;
    }
    @SuppressWarnings("unused")
    ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt);

    /**
     * Method that gets a copy of itemstack from a slot of an itemstack
     * Made not-static to enforce readability
     * @param target        ItemStack with an associated item handling capability
     * @param id            ID of a slot
     * @return              ItemStack from a slot
     */
    default ItemStack getItemFromItem(ItemStack target, int id)
    {
        AtomicReference<ItemStack> AR = new AtomicReference<>(ItemStack.EMPTY);
        target.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> AR.set(capability.getStackInSlot(id).copy()));
        return AR.get();
    }
    /**
     * Method that sets a copy of itemstack to a slot of an itemstack
     * Made not-static to enforce readability
     * @param target        ItemStack with an associated item handling capability
     * @param value         ItemStack that will have its copy placed in a slot
     * @param id            ID of a slot
     */
    default void setItemInItem(ItemStack target, ItemStack value, int id)
    {
        target.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
            if(capability instanceof IItemHandlerModifiable handlerModifiable){
                handlerModifiable.setStackInSlot(id, value);
            }
        });
    }
}
