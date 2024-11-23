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
package xyz.nikgub.pyromancer.registry;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import xyz.nikgub.incandescent.common.util.GeneralUtils;

import java.util.Optional;

public class StyleRegistry
{
    public static Style FROST_STYLE = Style.create(
            Optional.of(TextColor.fromRgb(GeneralUtils.rgbToColorInteger(10, 156, 240))),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of("frost"),
            Optional.of(Style.DEFAULT_FONT));

}
