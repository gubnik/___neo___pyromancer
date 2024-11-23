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
package xyz.nikgub.pyromancer.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import xyz.nikgub.pyromancer.common.ember.Ember;

public class EmberEvent extends PlayerEvent
{
    private final Ember ember;
    private final ItemStack itemStack;
    private final int tick;

    /**
     * This event fires while a player uses an {@link Ember}
     *
     * @param player    inherited constructor param
     * @param ember     ember that was used during the event
     * @param itemStack itemstack on which the ember was applied
     * @param tick      ticks passed since player started using the ember
     **/
    public EmberEvent (Player player, Ember ember, ItemStack itemStack, int tick)
    {
        super(player);
        this.ember = ember;
        this.itemStack = itemStack;
        this.tick = tick;
    }

    public Ember getEmber ()
    {
        return ember;
    }

    public ItemStack getItemStack ()
    {
        return itemStack;
    }

    public int getTick ()
    {
        return tick;
    }
}
