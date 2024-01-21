package xyz.nikgub.pyromancer.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import xyz.nikgub.pyromancer.mixin.ItemStackMixin;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Interface for altering attribute tooltip's color <p>
 * Used in {@link ItemStackMixin}
 */
public interface INotStupidTooltipItem {
    private Item self()
    {
        return (Item) this;
    }
    /**
     * Determines format transformations of attribute's tooltip <p>
     * New format overrides the default one
     * @return      Map of attributes and pairs of attribute's UUID and its new format
     */
    Map<Attribute, Pair<UUID, ChatFormatting>> specialColoredUUID();

    /**
     * Determines additional bonus in attribute's value <p>
     * This value should be gathered from player and be solely visual
     * @return      Function that consumes player and returns double value
     */
    BiFunction<Player, Attribute, Double> getAdditionalPlayerBonus();
}
