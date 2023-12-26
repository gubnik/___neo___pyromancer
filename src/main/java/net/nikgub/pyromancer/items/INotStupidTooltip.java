package net.nikgub.pyromancer.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public interface INotStupidTooltip {
    Map<Attribute, Pair<UUID, ChatFormatting>> specialColoredUUID();
    Function<Player, Double> getAdditionalPlayerBonus();
}
