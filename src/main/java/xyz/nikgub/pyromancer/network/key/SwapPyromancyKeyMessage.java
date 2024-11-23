package xyz.nikgub.pyromancer.network.key;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.common.item.CompendiumOfFlameItem;
import xyz.nikgub.pyromancer.common.item_capability.CompendiumOfFlameCapability;

import java.util.function.Supplier;

public class SwapPyromancyKeyMessage
{
    int type, pressedms;

    public SwapPyromancyKeyMessage (int type, int pressedms)
    {
        this.type = type;
        this.pressedms = pressedms;
    }

    public SwapPyromancyKeyMessage (FriendlyByteBuf buffer)
    {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void toBytes (SwapPyromancyKeyMessage message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handle (SwapPyromancyKeyMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), message.type, message.pressedms));
        context.setPacketHandled(true);
    }

    @SuppressWarnings("unused")
    public static void pressAction (@Nullable Player entity, int type, int pressedms)
    {
        if (entity == null) return;
        if (entity.getMainHandItem().getItem() instanceof CompendiumOfFlameItem compendiumOfFlameItem)
        {
            ItemStack compendium = entity.getMainHandItem();
            CompoundTag nbt = compendium.getOrCreateTag();
            nbt.putInt(CompendiumOfFlameItem.ACTIVE_SLOT_TAG, (nbt.getInt(CompendiumOfFlameItem.ACTIVE_SLOT_TAG) >= CompendiumOfFlameCapability.MAX_ITEMS) ? 1 : nbt.getInt(CompendiumOfFlameItem.ACTIVE_SLOT_TAG) + 1);
            if (compendiumOfFlameItem.getCurrentlyActiveItem(compendium).isEmpty())
                compendiumOfFlameItem.fixIndexing(compendium);
        }
    }
}
