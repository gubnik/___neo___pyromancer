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

import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.ArrayList;
import java.util.List;

public class WoodTypesRegistry
{
    public static List<WoodType> VALUES = new ArrayList<>();

    public static final WoodType PYROWOOD = makeNewWoodType(new WoodType("pyrowood", BlockSetTypeRegistry.PYROWOOD));

    private static WoodType makeNewWoodType (WoodType woodType)
    {
        WoodType.register(woodType);
        VALUES.add(woodType);
        return woodType;
    }
}
