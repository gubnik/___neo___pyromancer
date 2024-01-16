package net.nikgub.pyromancer.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.items.capabilities.CompendiumOfFlameCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompendiumOfFlameItem extends BlazingJournalItem {
    public static final String ACTIVE_SLOT_TAG = "___PYROMANCER_COMPENDIUM_ACTIVE_SLOT___";
    public static final String PYROMANCY_CUSTOM_RENDER_TAG = "___PYROMANCER_PYROMANCY_CUSTOM_RENDER___";
    public CompendiumOfFlameItem(Properties properties) {
        super(properties);
    }
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new CompendiumOfFlameCapability();
    }
    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int tick, boolean m)
    {
        CompoundTag tag = itemStack.getOrCreateTag();
        if(tag.getInt(ACTIVE_SLOT_TAG) == 0) tag.putInt(ACTIVE_SLOT_TAG, 1);
        if(tag.getDouble("CustomModelData") != 0) return;
        for (int i = 0; i < CompendiumOfFlameCapability.MAX_ITEMS + 1; i++)
        {
            PyromancerMod.LOGGER.info("Item in slot " + i + " is " + this.getItemFromItem(itemStack, i));
            if(this.getItemFromItem(itemStack, i).getItem() instanceof UsablePyromancyItem) tag.putDouble("CustomModelData", 1);
        }
    }
    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack inSlot, @NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player, @NotNull SlotAccess slotAccess)
    {
        if(!super.overrideOtherStackedOnMe(inSlot, held, slot, clickAction, player, slotAccess))
        {
            if(held.getItem() instanceof  UsablePyromancyItem) return pyromancyBehaviour(inSlot, held, slotAccess);
        }
        return super.overrideOtherStackedOnMe(inSlot, held, slot, clickAction, player, slotAccess);
    }
    public boolean pyromancyBehaviour(ItemStack inSlot, ItemStack held, SlotAccess slotAccess)
    {
        for(int i = 1; i < CompendiumOfFlameCapability.MAX_ITEMS + 1; i++)
        {
            if(!(this.getItemFromItem(inSlot, i).getItem() instanceof UsablePyromancyItem))
            {
                this.setItemInItem(inSlot, held, i);
                slotAccess.set(ItemStack.EMPTY);
                break;
            }
        }
        return true;
    }
}
