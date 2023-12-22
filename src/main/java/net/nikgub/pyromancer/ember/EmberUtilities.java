package net.nikgub.pyromancer.ember;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.nikgub.pyromancer.events.EmberEvent;
import net.nikgub.pyromancer.registries.custom.EmberRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class EmberUtilities {
    public static void emberCooldown(Player player, int time, String tag){
        AtomicReference<IItemHandler> itemHandReference = new AtomicReference<>();
        player.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(itemHandReference::set);
        if (itemHandReference.get() != null) {
            for (int i = 0; i < itemHandReference.get().getSlots(); i++) {
                ItemStack itemStack = itemHandReference.get().getStackInSlot(i).copy();
                if (itemStack.getOrCreateTag().getString(EmberRegistry.TAG_NAME).equals(tag)) {
                    player.getCooldowns().addCooldown(itemStack.getItem(), time);
                }
            }
        }
    }
    @NotNull
    public static EmberEvent getEmberEvent(Player player, Ember ember, ItemStack itemStack, int tick)
    {
        EmberEvent event = new EmberEvent(player, ember, itemStack, tick);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }
    public static boolean isUniquelyDenied(ItemStack itemStack)
    {
        return (itemStack.getItem().getClass().isAnnotationPresent(UniqueEmberBehaviour.class)) && itemStack.getItem().getClass().getAnnotation(UniqueEmberBehaviour.class).allow() == UniqueEmberBehaviour.AllowanceModifier.DENY;
    }
    public static boolean isUniquelyDenied(Item item)
    {
        return (item.getClass().isAnnotationPresent(UniqueEmberBehaviour.class)) && item.getClass().getAnnotation(UniqueEmberBehaviour.class).allow() == UniqueEmberBehaviour.AllowanceModifier.DENY;
    }
    public static boolean isUniquelyAllowed(ItemStack itemStack)
    {
        return (itemStack.getItem().getClass().isAnnotationPresent(UniqueEmberBehaviour.class)) && itemStack.getItem().getClass().getAnnotation(UniqueEmberBehaviour.class).allow() == UniqueEmberBehaviour.AllowanceModifier.ALLOW
                && !isUniquelyDenied(itemStack);
    }
    public static boolean isUniquelyAllowed(Item item)
    {
        return (item.getClass().isAnnotationPresent(UniqueEmberBehaviour.class)) && item.getClass().getAnnotation(UniqueEmberBehaviour.class).allow() == UniqueEmberBehaviour.AllowanceModifier.ALLOW
                && !isUniquelyDenied(item);
    }
}
