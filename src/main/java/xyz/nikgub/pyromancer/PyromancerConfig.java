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
package xyz.nikgub.pyromancer;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import xyz.nikgub.incandescent.Incandescent;

@Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PyromancerConfig
{

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Integer> BLAZING_JOURNAL_MAX_CAPACITY = BUILDER
        .comment("Defines max amount of blaze one Blazing Journal can hold")
        .defineInRange("blazing_journal_max_capacity", 512, 0, 8192);

    public static final ForgeConfigSpec.ConfigValue<Integer> BLAZE_VALUE = BUILDER
        .comment("Defines max amount of blaze one Blazing Journal can hold")
        .defineInRange("blaze_value", 8, 0, 128);

    public static final ForgeConfigSpec.ConfigValue<Integer> FLAMING_GROVE_RATE = BUILDER
        .comment("Defines how often is Flaming Grove biome generated")
        .defineInRange("flaming_grove_rate", 5, 0, 10);

    private static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_CONTRACT_CREDITS = BUILDER
        .comment("Defines the amount of credits the director gets when activating Accursed Contract")
        .define("default_contract_credits", 30);

    private static final ForgeConfigSpec.ConfigValue<Float> DEFAULT_MUSKET_DAMAGE = BUILDER
        .comment("Defines the default damage of a single musket shot")
        .define("default_musket_damage", 16f);

    private static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_MUSKET_RANGE = BUILDER
        .comment("Defines the default number of iterations in a single musket shot, 1 iteration = 0.2 blocks")
        .define("default_musket_range", 100);

    private static final ForgeConfigSpec.ConfigValue<Float> MUSKET_DAMAGE_CAP = BUILDER
        .comment("Defines the maximum amount of damage a single musket shot can deal")
        .define("musket_damage_cap", 50f);

    private static final ForgeConfigSpec.EnumValue<Incandescent.Key> DESC_TOOLTIP_KEY = BUILDER
        .comment("Defines the key that reveals item's hidden description")
        .defineEnum("desc_tooltip_key", Incandescent.Key.SHIFT);

    private static final ForgeConfigSpec.EnumValue<Incandescent.Key> LORE_TOOLTIP_KEY = BUILDER
        .comment("Defines the key that reveals item's hidden lore description")
        .defineEnum("lore_tooltip_key", Incandescent.Key.ALT);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int blazingJournalMaxCapacity;
    public static int blazeValue;
    public static int flamingGroveRate;
    public static int defaultContractCredits;
    public static float defaultMusketDamage;
    public static int defaultMusketRange;
    public static float musketDamageCap;
    public static Incandescent.Key descTooltipKey;
    public static Incandescent.Key loreTooltipKey;

    @SubscribeEvent
    static void onLoad (final ModConfigEvent event)
    {
        blazingJournalMaxCapacity = BLAZING_JOURNAL_MAX_CAPACITY.get();
        blazeValue = BLAZE_VALUE.get();
        flamingGroveRate = FLAMING_GROVE_RATE.get();
        defaultContractCredits = DEFAULT_CONTRACT_CREDITS.get();
        defaultMusketDamage = DEFAULT_MUSKET_DAMAGE.get();
        defaultMusketRange = DEFAULT_MUSKET_RANGE.get();
        musketDamageCap = MUSKET_DAMAGE_CAP.get();
        descTooltipKey = DESC_TOOLTIP_KEY.get();
        loreTooltipKey = LORE_TOOLTIP_KEY.get();
    }
}
