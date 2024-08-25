package xyz.nikgub.pyromancer;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.nikgub.incandescent.Incandescent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PyromancerConfig
{

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EMBERS_BLACKLIST = BUILDER
            .comment("Defines a list of items' id that aren't able to have Embers applied")
            .defineList("ember_blacklist", PyromancerConfig::defaultBlacklist, PyromancerConfig::isValidItemValue);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EMBERS_ADDITIONAL_ITEMS = BUILDER
            .comment("Defines a list of items' id that should be able to have Embers regardless of default conditions. However, blacklist has priority above this")
            .defineList("ember_additional_items", PyromancerConfig::defaultBlacklist, PyromancerConfig::isValidItemValue);

    public static final ForgeConfigSpec.ConfigValue<Integer> BLAZING_JOURNAL_MAX_CAPACITY = BUILDER
            .comment("Defines max amount of blaze one Blazing Journal can hold")
            .defineInRange("blazing_journal_max_capacity", 512, 0, 8192);

    public static final ForgeConfigSpec.ConfigValue<Integer> BLAZE_VALUE = BUILDER
            .comment("Defines max amount of blaze one Blazing Journal can hold")
            .defineInRange("blaze_value", 8, 0, 128);

    public static final ForgeConfigSpec.ConfigValue<Integer> FLAMING_GROVE_RATE = BUILDER
            .comment("Defines how often is Flaming Grove biome generated")
            .defineInRange("flaming_grove_rate", 5, 0, 10);
    public static final ForgeConfigSpec.ConfigValue<Incandescent.Key> EMBERS_DESCRIPTION_KEY = BUILDER
            .comment("Defines a key to show Ember description on item")
            .defineEnum("emberDescriptionKey", Incandescent.Key.ALT);
    public static final ForgeConfigSpec.ConfigValue<Incandescent.Key> QUILL_DESCRIPTION_KEY = BUILDER
            .comment("Defines a key to show quill attack description")
            .defineEnum("quill_description_key", Incandescent.Key.ALT);
    public static final ForgeConfigSpec.ConfigValue<Incandescent.Key> PYROMANCY_DESCRIPTION_KEY = BUILDER
            .comment("Defines a key to show pyromancy description")
            .defineEnum("pyromancy_description_key", Incandescent.Key.ALT);
    private static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_CONTRACT_CREDITS = BUILDER
            .comment("Defines the amount of credits the director gets when activating Accursed Contract")
            .define("default_contract_credits", 60);
    private static final ForgeConfigSpec.ConfigValue<Float> DEFAULT_MUSKET_DAMAGE = BUILDER
            .comment("Defines the default damage of a single musket shot")
            .define("default_musket_damage", 16f);
    private static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_MUSKET_RANGE = BUILDER
            .comment("Defines the default number of iterations in a single musket shot, 1 iteration = 0.2 blocks")
            .define("default_musket_range", 150);
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

    public static List<? extends String> emberBlacklist;
    public static List<? extends String> emberAdditionalItems;
    public static int blazingJournalMaxCapacity;
    public static int blazeValue;
    public static int flamingGroveRate;
    public static int defaultContractCredits;
    public static float defaultMusketDamage;
    public static int defaultMusketRange;
    public static float musketDamageCap;
    public static Incandescent.Key embersDescriptionKey;
    public static Incandescent.Key quillDescriptionKey;
    public static Incandescent.Key pyromancyDescriptionKey;
    public static Incandescent.Key descTooltipKey;
    public static Incandescent.Key loreTooltipKey;

    @SubscribeEvent
    static void onLoad (final ModConfigEvent event)
    {
        emberBlacklist = EMBERS_BLACKLIST.get();
        emberAdditionalItems = EMBERS_ADDITIONAL_ITEMS.get();
        blazingJournalMaxCapacity = BLAZING_JOURNAL_MAX_CAPACITY.get();
        blazeValue = BLAZE_VALUE.get();
        flamingGroveRate = FLAMING_GROVE_RATE.get();
        defaultContractCredits = DEFAULT_CONTRACT_CREDITS.get();
        defaultMusketDamage = DEFAULT_MUSKET_DAMAGE.get();
        defaultMusketRange = DEFAULT_MUSKET_RANGE.get();
        musketDamageCap = MUSKET_DAMAGE_CAP.get();
        embersDescriptionKey = EMBERS_DESCRIPTION_KEY.get();
        quillDescriptionKey = QUILL_DESCRIPTION_KEY.get();
        pyromancyDescriptionKey = PYROMANCY_DESCRIPTION_KEY.get();
        descTooltipKey = DESC_TOOLTIP_KEY.get();
        loreTooltipKey = LORE_TOOLTIP_KEY.get();
    }

    public static List<String> defaultBlacklist ()
    {
        return new ArrayList<>();
    }

    public static boolean isValidItemValue (Object object)
    {
        boolean flag = false;
        for (Item item : ForgeRegistries.ITEMS.getValues().stream().toList())
        {
            if (object instanceof String string && item.toString().equals(string))
            {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
