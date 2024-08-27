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
