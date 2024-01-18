package xyz.nikgub.pyromancer;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PyromancerConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.ConfigValue<Key> HIDDEN_DESCRIPTION_KEY = BUILDER
            .comment("Defines a key to show additional description on item")
            .defineEnum("hiddenDescriptionKey", Key.CTRL);
    public static final ForgeConfigSpec.ConfigValue<Key> EMBERS_DESCRIPTION_KEY = BUILDER
            .comment("Defines a key to show Ember description on item")
            .defineEnum("emberDescriptionKey", Key.ALT);
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EMBERS_BLACKLIST = BUILDER
            .comment("Defines a list of items' id that aren't able to have Embers applied")
            .defineList("emberBlacklist", PyromancerConfig::defaultBlacklist, PyromancerConfig::isValidItemValue);
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EMBERS_ADDITIONAL_ITEMS = BUILDER
            .comment("Defines a list of items' id that should be able to have Embers regardless of default conditions. However, blacklist has priority above this")
            .defineList("emberAdditionalItems", PyromancerConfig::defaultBlacklist, PyromancerConfig::isValidItemValue);
    public static final ForgeConfigSpec.ConfigValue<Integer> BLAZING_JOURNAL_MAX_CAPACITY = BUILDER
            .comment("Defines max amount of blaze one Blazing Journal can hold")
            .defineInRange("blazingJournalMaxCapacity", 512, 0, 8192);
    public static final ForgeConfigSpec.ConfigValue<Integer> BLAZE_VALUE = BUILDER
            .comment("Defines max amount of blaze one Blazing Journal can hold")
            .defineInRange("blazeValue", 8, 0, 128);
    static final ForgeConfigSpec SPEC = BUILDER.build();
    public static Key embersDescriptionKey;
    public static List<? extends String> emberBlacklist;
    public static List<? extends String> emberAdditionalItems;
    public static int blazingJournalMaxCapacity;
    public static int blazeValue;
    public
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        embersDescriptionKey = EMBERS_DESCRIPTION_KEY.get();
        emberBlacklist = EMBERS_BLACKLIST.get();
        emberAdditionalItems = EMBERS_ADDITIONAL_ITEMS.get();
        blazingJournalMaxCapacity = BLAZING_JOURNAL_MAX_CAPACITY.get();
        blazeValue = BLAZE_VALUE.get();
    }
    @SuppressWarnings("unused")
    public enum Key
    {
        ALT(Screen::hasAltDown),
        CTRL(Screen::hasControlDown),
        SHIFT(Screen::hasShiftDown);
        private final Supplier<Boolean> supplier;
        Key(Supplier<Boolean> supplier)
        {
            this.supplier = supplier;
        }
        public Supplier<Boolean> getSupplier() {
            return supplier;
        }
    }
    public static List<String> defaultBlacklist() {
        return new ArrayList<>();
    }
    public static boolean isValidItemValue(Object object)
    {
        boolean flag = false;
        for(Item item : ForgeRegistries.ITEMS.getValues().stream().toList())
        {
            if(object instanceof String string && item.toString().equals(string)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
