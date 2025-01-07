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
package xyz.nikgub.pyromancer.common.ember;


import xyz.nikgub.incandescent.common.util.GeneralUtils;

import java.util.function.Function;

public class EmberType
{
    private static final int TICK_LIMIT = 40;
    public static final EmberType FLAME = new EmberType("fire", GeneralUtils.rgbaToColorInteger(64, 56, 6, 224),
        functionBuilder(GeneralUtils.rgbToColorInteger(160, 50, 50), GeneralUtils.rgbToColorInteger(200, 140, 40)));
    public static final EmberType SOULFLAME = new EmberType("soulflame", GeneralUtils.rgbaToColorInteger(6, 64, 56, 224),
        functionBuilder(GeneralUtils.rgbToColorInteger(100, 100, 100), GeneralUtils.rgbToColorInteger(95, 200, 180)));
    public static final EmberType HELLBLAZE = new EmberType("hellblaze", GeneralUtils.rgbaToColorInteger(64, 32, 24, 224),
        functionBuilder(GeneralUtils.rgbToColorInteger(140, 60, 80), GeneralUtils.rgbToColorInteger(180, 40, 100)));
    private final String name;
    private final int color;
    private final Function<Integer, Integer> colorFunction;

    public EmberType (String name, int color, Function<Integer, Integer> colorFunction)
    {
        this.name = name;
        this.color = color;
        this.colorFunction = colorFunction;
    }

    private static Function<Integer, Integer> functionBuilder (int first, int second)
    {
        final int redFirst = first / 65536, greenFirst = (first % 65536) / 256, blueFirst = first % 256;
        final int redSecond = second / 65536, greenSecond = (second % 65536) / 256, blueSecond = second % 256;
        return (tick) ->
        {
            final int cT = Math.abs(TICK_LIMIT - tick % (TICK_LIMIT * 2));
            return GeneralUtils.rgbToColorInteger(
                redFirst + ((redSecond - redFirst) / TICK_LIMIT) * cT,
                greenFirst + ((greenSecond - greenFirst) / TICK_LIMIT) * cT,
                blueFirst + ((blueSecond - blueFirst) / TICK_LIMIT) * cT
            );
        };
    }

    public String getName ()
    {
        return name;
    }

    public int getColor ()
    {
        return color;
    }

    public Function<Integer, Integer> getTextColorFunction ()
    {
        return colorFunction;
    }
}
