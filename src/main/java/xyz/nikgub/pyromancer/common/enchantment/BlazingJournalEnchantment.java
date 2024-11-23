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
package xyz.nikgub.pyromancer.common.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.common.item.BlazingJournalItem;
import xyz.nikgub.pyromancer.registry.EnchantmentRegistry;

public abstract class BlazingJournalEnchantment extends Enchantment
{
    public BlazingJournalEnchantment ()
    {
        super(Rarity.UNCOMMON, EnchantmentRegistry.BLAZING_JOURNAL, new EquipmentSlot[]{});
    }

    @Override
    public @NotNull Component getFullname (int pLevel)
    {
        Component component = super.getFullname(pLevel);
        Style style = component.getStyle();
        return component.copy().withStyle(style).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD);
    }

    public abstract Class<? extends TieredItem> getWeaponClass ();

    public abstract void getAttack (Player player, Entity target);

    public abstract boolean getCondition (Player player, Entity target);

    public boolean globalCondition (Player player)
    {
        return BlazingJournalItem.getBlaze(player) > 0 && player.getAttackStrengthScale(0) > 0.7;
    }
}
