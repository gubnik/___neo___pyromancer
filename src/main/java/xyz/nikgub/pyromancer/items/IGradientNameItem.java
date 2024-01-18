package xyz.nikgub.pyromancer.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.nikgub.pyromancer.util.GeneralUtils;
import xyz.nikgub.pyromancer.mixin.ItemStackMixin;

import java.util.function.Function;

/**
 * Interface for {@link ItemStackMixin}'s getHoverNameMixinHead()
 */
public interface IGradientNameItem {
    /**
     * Method to cast into Item
     * @return                  This but Item
     */
    @SuppressWarnings("unused")
    default Item self()
    {
        return (Item) this;
    }
    /**
     * Method that provides an additional condition to display gradient name
     * @param itemStack         ItemStack of this item
     * @return                  boolean
     */
    boolean getGradientCondition(ItemStack itemStack);
    /**
     * Method that provides a pair of colors to switch between
     * @return                  Pair of integer RGB color values
     */
    Pair<Integer, Integer> getGradientColors();

    /**
     * Method that provides time in ticks in which full color change happens
     * @return                  Integer time in ticks
     */
    int getGradientTickTime();

    /**
     * Method that provides a function that defines how does the color change depending on tick
     * @return                  Function that consumes an integer tick and returns an integer color code
     */
    default Function<Integer, Integer> getGradientFunction()
    {
        final int redFirst = getGradientColors().getFirst() / 65536, greenFirst = (getGradientColors().getFirst() % 65536) / 256, blueFirst = getGradientColors().getFirst() % 256;
        final int redSecond = getGradientColors().getSecond() / 65536, greenSecond = (getGradientColors().getSecond() % 65536) / 256, blueSecond = getGradientColors().getSecond() % 256;
        return (tick)->
        {
            final int cT = Math.abs(getGradientTickTime() - tick % (getGradientTickTime() * 2));
            return GeneralUtils.rgbToColorInteger(
                    redFirst + ((redSecond - redFirst) / getGradientTickTime()) * cT,
                    greenFirst + ((greenSecond - greenFirst) / getGradientTickTime()) * cT,
                    blueFirst + ((blueSecond - blueFirst) / getGradientTickTime()) * cT
            );
        };
    }
}
