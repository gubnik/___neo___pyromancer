package xyz.nikgub.pyromancer.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IExtensibleTooltip {

    String hiddenTooltipTranslationKey ();

    PyromancerConfig.Key showHiddenLinesKey ();

    default String tooltipLineSubscriber ()
    {
        return "desc";
    }

    default void addTooltipLines (@NotNull List<Component> list)
    {
        Optional<ResourceKey<Item>> optKey = ForgeRegistries.ITEMS.getResourceKey((Item) this);
        if (optKey.isEmpty()) return;
        List<Component> fetchedLines = new ArrayList<>();
        String locName = optKey.get().location().getNamespace();
        String locPath = optKey.get().location().getPath();
        int it = 0;
        String lineName = "item." + locName + "." + locPath + "." + tooltipLineSubscriber() + "." + it;
        Component tComponent = Component.translatable(lineName);
        while (!tComponent.getString().equals(lineName))
        {
            fetchedLines.add(tComponent.copy().withStyle(ChatFormatting.GRAY));
            lineName = "item." + locName + "." + locPath + "." + tooltipLineSubscriber() + "." + ++it;
            tComponent = Component.translatable(lineName);

        }
        if (fetchedLines.isEmpty()) return;
        if (Screen.hasAltDown()) list.addAll(fetchedLines);
        else
        {
            String rawText = Component.translatable(hiddenTooltipTranslationKey()).getString();
            list.add(Component.literal(rawText + showHiddenLinesKey().name()).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.BOLD));
        }
    }
}
