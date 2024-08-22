package xyz.nikgub.pyromancer.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class MusketAmmunitionItem extends Item
{
    private final Effect effect;

    public MusketAmmunitionItem (Properties properties, Effect effect)
    {
        super(properties);
        this.effect = effect;
    }

    public static ItemStack fetchStack (@NotNull Entity entity)
    {
        AtomicReference<IItemHandler> aref = new AtomicReference<>();
        entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(aref::set);
        if (aref.get() != null)
        {
            for (int i = 0; i < aref.get().getSlots(); i++)
            {
                ItemStack itemStack = aref.get().getStackInSlot(i);
                if (itemStack.getItem() instanceof MusketAmmunitionItem) return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public @NotNull Effect getEffect ()
    {
        return effect;
    }

    public interface Effect
    {
        float getModifier (ItemStack musket, LivingEntity source, LivingEntity entity);
    }
}
