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
package xyz.nikgub.pyromancer.network.c2s;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.incandescent.autogen_network.IncandescentPacket;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.item.CompendiumOfFlameItem;
import xyz.nikgub.pyromancer.common.item_capability.CompendiumOfFlameCapability;

import java.util.function.Supplier;

@IncandescentPacket(value = PyromancerMod.MOD_ID, direction = NetworkDirection.PLAY_TO_SERVER)
public class SwapPyromancyKeyMessage
{
    @IncandescentPacket.Value
    private Integer type;

    @IncandescentPacket.Value
    private Integer pressedms;

    public static SwapPyromancyKeyMessage create (int type, int pressedms)
    {
        SwapPyromancyKeyMessage instance = new SwapPyromancyKeyMessage();
        instance.type = type;
        instance.pressedms = pressedms;
        return instance;
    }

    @IncandescentPacket.Handler
    public boolean handle (Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), this.type, this.pressedms));
        //context.setPacketHandled(true);
        return true;
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
