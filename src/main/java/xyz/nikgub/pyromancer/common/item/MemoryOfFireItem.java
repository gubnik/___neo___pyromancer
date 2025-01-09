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

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.item.IGradientNameItem;
import xyz.nikgub.incandescent.common.util.GeneralUtils;

import java.util.List;

public class MemoryOfFireItem extends Item implements IGradientNameItem
{
    public MemoryOfFireItem ()
    {
        super(new Properties().stacksTo(1).fireResistant());
    }


    @Override
    public void appendHoverText (@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        // Alpha
        list.add(Component.translatable("pyromancer.alpha_desc").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED));
        list.add(Component.translatable("pyromancer.bad_craft_warning").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED));
    }

    @Override
    public boolean getGradientCondition (ItemStack itemStack)
    {
        return true;
    }

    @Override
    public Pair<Integer, Integer> getGradientColors (ItemStack itemStack)
    {
        return Pair.of(
            GeneralUtils.rgbToColorInteger(200, 57, 0),
            GeneralUtils.rgbToColorInteger(240, 129, 0)
        );
    }

    @Override
    public int getGradientTickTime (ItemStack itemStack)
    {
        return 60;
    }
}
