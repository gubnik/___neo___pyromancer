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
package xyz.nikgub.pyromancer.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.incandescent.common.item.IContainerItem;
import xyz.nikgub.pyromancer.client.item_extension.VaporizerClientExtension;
import xyz.nikgub.pyromancer.common.item_capability.VaporizerCapability;
import xyz.nikgub.pyromancer.data.ItemTagDatagen;
import xyz.nikgub.pyromancer.registry.VaporizerAmmoRegistry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class VaporizerItem extends Item implements IContainerItem
{
    public VaporizerItem (Properties pProperties)
    {
        super(pProperties.stacksTo(1));
    }

    public boolean isEmpty (ItemStack itemStack)
    {
        for (int i = 0; i < VaporizerCapability.MAX_SLOTS; i++)
        {
            if (!this.getItemFromItem(itemStack, i).isEmpty()) return false;
        }
        return true;
    }

    public int getAvailableSlot (ItemStack itemStack, ItemStack toPlace)
    {
        for (int i = 0; i < VaporizerCapability.MAX_SLOTS; i++)
        {
            ItemStack inSlot = this.getItemFromItem(itemStack, i);
            if (inSlot.isEmpty() || inSlot.is(toPlace.getItem())) return i;
        }
        return -1;
    }

    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new VaporizerCapability();
    }

    @Override
    public void initializeClient (@NotNull Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new VaporizerClientExtension());
    }

    @Override
    public void onUseTick (@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        int slot = ThreadLocalRandom.current().nextInt(VaporizerCapability.MAX_SLOTS);
        int i = 0;
        ItemStack ammoStack;
        while ((ammoStack = this.getItemFromItem(itemStack, slot)).isEmpty() && i++ < VaporizerCapability.MAX_SLOTS)
        {
            if (++slot >= VaporizerCapability.MAX_SLOTS) slot = 0;
        }
        if (ammoStack.isEmpty())
        {
            return;
        }
        VaporizerAmmoRegistry.getAmmo(ammoStack.getItem()).runConsumeEffect(itemStack, entity);
        if (ThreadLocalRandom.current().nextInt(VaporizerCapability.MAX_SLOTS) == 0)
        {
            ItemStack toPlace = ammoStack.copy();
            toPlace.shrink(1);
            this.setItemInItem(itemStack, toPlace, slot);
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity)
    {
        return itemStack;
    }

    @Override
    public @NotNull UseAnim getUseAnimation (@NotNull ItemStack itemStack)
    {
        return UseAnim.CUSTOM;
    }

    @Override
    public int getUseDuration (@NotNull ItemStack itemStack)
    {
        return 72000;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use (@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!this.isEmpty(itemStack))
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public boolean overrideOtherStackedOnMe (@NotNull ItemStack inSlot, @NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player, @NotNull SlotAccess slotAccess)
    {
        if (held.is(ItemTagDatagen.VAPORIZER_AMMO)) return insertAmmoBehaviour(inSlot, held, slotAccess);
        return super.overrideOtherStackedOnMe(inSlot, held, slot, clickAction, player, slotAccess);
    }

    public boolean insertAmmoBehaviour (ItemStack inSlot, ItemStack held, SlotAccess slotAccess)
    {
        int slot = this.getAvailableSlot(inSlot, held);
        if (slot == -1) return true;
        ItemStack slotStack = this.getItemFromItem(inSlot, slot);
        if (slotStack.isEmpty()) slotStack = ItemStack.EMPTY;
        int upperLimit = 64 - slotStack.getCount();
        while (slotStack.getCount() < upperLimit && !slotAccess.get().isEmpty())
        {
            ItemStack toPlace;
            if (slotStack.isEmpty())
            {
                toPlace = new ItemStack(held.getItem(), 1);
            } else
            {
                toPlace = slotStack.copy();
                toPlace.grow(1);
            }
            this.setItemInItem(inSlot, toPlace, slot);
            slotAccess.get().shrink(1);
            slotStack = this.getItemFromItem(inSlot, slot);
        }
        return true;
    }

    public static abstract class Ammo
    {
        private final Item consumedItem;

        public Ammo (Item consumedItem)
        {
            this.consumedItem = consumedItem;
        }

        public abstract void runConsumeEffect (ItemStack itemStack, LivingEntity entity);

        public Item getConsumedItem ()
        {
            return consumedItem;
        }
    }
}
