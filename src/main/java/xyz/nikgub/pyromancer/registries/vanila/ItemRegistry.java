package xyz.nikgub.pyromancer.registries.vanila;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.items.*;
import xyz.nikgub.pyromancer.items.*;
import xyz.nikgub.pyromancer.util.GeneralUtils;

@SuppressWarnings("unused")
public class ItemRegistry {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PyromancerMod.MOD_ID);
    public static RegistryObject<Item> BLAZING_JOURNAL = ITEMS.register("blazing_journal",
            () -> new BlazingJournalItem(new Item.Properties()));
    public static RegistryObject<Item> COMPENDIUM_OF_FLAME = ITEMS.register("compendium_of_flame",
            () -> new CompendiumOfFlameItem(new Item.Properties()));
    public static RegistryObject<Item> EMBER_ITEM = ITEMS.register("ember",
            () -> new EmberItem(new Item.Properties()));
    public static RegistryObject<Item> SIZZLING_HAND = ITEMS.register("sizzling_hand",
            () -> new SizzlingHandItem(new Item.Properties()));
    public static RegistryObject<Item> BLAZING_QUILL = ITEMS.register("blazing_quill",
            () -> new QuillItem(new Item.Properties(),
                    ((player, weapon, journal) -> {
                    }),
                    ((player, weapon, journal) -> player.getAttackStrengthScale(0) > 0.7)
            ){
                @Override
                public Pair<Integer, Float> getPyromancyModifiers() {
                    return Pair.of(0, 1.5f);
                }
            }
    );
    public static RegistryObject<Item> WOODEN_MACE = ITEMS.register("wooden_mace",
            () -> new MaceItem(Tiers.WOOD, new Item.Properties().stacksTo(1)));
    public static RegistryObject<Item> STONE_MACE = ITEMS.register("stone_mace",
            () -> new MaceItem(Tiers.STONE, new Item.Properties().stacksTo(1)));
    public static RegistryObject<Item> IRON_MACE = ITEMS.register("iron_mace",
            () -> new MaceItem(Tiers.IRON, new Item.Properties().stacksTo(1)));
    public static RegistryObject<Item> GOLDEN_MACE = ITEMS.register("golden_mace",
            () -> new MaceItem(Tiers.GOLD, new Item.Properties().stacksTo(1)));
    public static RegistryObject<Item> DIAMOND_MACE = ITEMS.register("diamond_mace",
            () -> new MaceItem(Tiers.DIAMOND, new Item.Properties().stacksTo(1)));
    public static RegistryObject<Item> NETHERITE_MACE = ITEMS.register("netherite_mace",
            () -> new MaceItem(Tiers.NETHERITE, new Item.Properties().stacksTo(1)));

    // SPAWN EGGS

    public static RegistryObject<Item> UNBURNED_SPAWN_EGG = ITEMS.register("unburned_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.UNBURNED, GeneralUtils.rgbaToColorInteger(140, 100, 12, 100), GeneralUtils.rgbaToColorInteger(120, 90, 0, 100), new Item.Properties()));
}
