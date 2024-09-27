package xyz.nikgub.pyromancer.common.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.nikgub.incandescent.common.item.IGradientNameItem;
import xyz.nikgub.incandescent.common.util.GeneralUtils;

public class EverburningHeartItem extends Item implements IGradientNameItem
{
    public EverburningHeartItem ()
    {
        super(new Properties().stacksTo(1).fireResistant());
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
