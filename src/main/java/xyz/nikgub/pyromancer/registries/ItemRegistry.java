package xyz.nikgub.pyromancer.registries;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.items.*;
import xyz.nikgub.pyromancer.common.items.armor.ArmorOfHellblazeMonarchItem;
import xyz.nikgub.pyromancer.common.items.armor.MarauderArmorItem;
import xyz.nikgub.pyromancer.common.items.armor.PyromancerArmorItem;

@SuppressWarnings("unused")
public class ItemRegistry {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PyromancerMod.MOD_ID);

    // MATERIALS
    public static RegistryObject<Item> HOGLIN_HIDE = ITEMS.register("hoglin_hide",  () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> NETHERITE_SHARD = ITEMS.register("netherite_shard", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> CINNABAR_CHUNK = ITEMS.register("cinnabar_chunk", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> DROPS_OF_MERCURY = ITEMS.register("drops_of_mercury", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> BRIMSTONE = ITEMS.register("brimstone", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new Item(new Item.Properties()));

    public static RegistryObject<Item> EVENBURNING_HEART = ITEMS.register("everburning_heart", () -> new Item(new Item.Properties().stacksTo(1)));

    public static RegistryObject<Item> BLAZING_JOURNAL = ITEMS.register("blazing_journal", () -> new BlazingJournalItem(new Item.Properties()));

    public static RegistryObject<Item> COMPENDIUM_OF_FLAME = ITEMS.register("compendium_of_flame", () -> new CompendiumOfFlameItem(new Item.Properties()));

    public static RegistryObject<Item> EMBER_ITEM = ITEMS.register("ember", () -> new EmberItem(new Item.Properties()));

    public static RegistryObject<Item> BLAZING_QUILL = ITEMS.register("blazing_quill",
            () -> new QuillItem(new Item.Properties())
            {
                @Override
                public float getDefaultPyromancyDamageBonus ()
                {
                    return 2;
                }

                @Override
                public int getDefaultBlazeCostBonus ()
                {
                    return 1;
                }

                @Override
                public void getAttack (Player player, ItemStack weaponStack, ItemStack journalStack)
                {
                }
                @Override
                public boolean getCondition(Player player, ItemStack weaponStack, ItemStack journalStack)
                {
                    return false;
                }
            }
    );

    public static RegistryObject<BombsackItem> BOMBSACK = ITEMS.register("bombsack",
            () -> new BombsackItem(new Item.Properties(), EntityTypeRegistry.BOMBSACK::get));

    public static RegistryObject<SizzlingHandItem> SIZZLING_HAND = ITEMS.register("sizzling_hand",
            () -> new SizzlingHandItem(new Item.Properties()));
    public static RegistryObject<CourtOfEmbersItem> COURT_OF_EMBERS = ITEMS.register("court_of_embers",
            () -> new CourtOfEmbersItem(new Item.Properties()));
    public static RegistryObject<SymbolOfSunItem> SYMBOL_OF_SUN = ITEMS.register("symbol_of_sun",
            () -> new SymbolOfSunItem(new Item.Properties()));

    // TOOLS
    public static RegistryObject<Item> AMBER_PICKAXE = ITEMS.register("amber_pickaxe",
            () -> new PickaxeItem(TierRegistry.AMBER, 1, -2.8f, new Item.Properties()));
    public static RegistryObject<Item> AMBER_AXE = ITEMS.register("amber_axe",
            () -> new AxeItem(TierRegistry.AMBER, 5, -3.0f, new Item.Properties()));
    public static RegistryObject<Item> AMBER_SHOVEL = ITEMS.register("amber_shovel",
            () -> new ShovelItem(TierRegistry.AMBER, 1, -2.8f, new Item.Properties()));
    public static RegistryObject<Item> AMBER_HOE = ITEMS.register("amber_hoe",
            () -> new HoeItem(TierRegistry.AMBER, 1, -2.8f, new Item.Properties()));
    public static RegistryObject<Item> AMBER_SWORD = ITEMS.register("amber_sword",
            () -> new SwordItem(TierRegistry.AMBER, 3, -2.8f, new Item.Properties()));

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
    public static RegistryObject<Item> AMBER_MACE = ITEMS.register("amber_mace",
            () -> new MaceItem(TierRegistry.AMBER, new Item.Properties()));

    // ARMOR
    public static RegistryObject<Item> MARAUDER_HELM = ITEMS.register("marauder_helm",
            () -> new MarauderArmorItem(ArmorItem.Type.HELMET));
    public static RegistryObject<Item> MARAUDER_CAPE = ITEMS.register("marauder_cape",
            () -> new MarauderArmorItem(ArmorItem.Type.CHESTPLATE));
    public static RegistryObject<Item> MARAUDER_PANTS = ITEMS.register("marauder_pants",
            () -> new MarauderArmorItem(ArmorItem.Type.LEGGINGS));
    public static RegistryObject<Item> MARAUDER_BOOTS = ITEMS.register("marauder_boots",
            () -> new MarauderArmorItem(ArmorItem.Type.BOOTS));

    public static RegistryObject<Item> PYROMANCER_HELMET = ITEMS.register("pyromancer_helmet",
            () -> new PyromancerArmorItem(ArmorItem.Type.HELMET));
    public static RegistryObject<Item> PYROMANCER_CHESTPLATE = ITEMS.register("pyromancer_chestplate",
            () -> new PyromancerArmorItem(ArmorItem.Type.CHESTPLATE));
    public static RegistryObject<Item> PYROMANCER_LEGGINGS = ITEMS.register("pyromancer_leggings",
            () -> new PyromancerArmorItem(ArmorItem.Type.LEGGINGS));
    public static RegistryObject<Item> PYROMANCER_BOOTS = ITEMS.register("pyromancer_boots",
            () -> new PyromancerArmorItem(ArmorItem.Type.BOOTS));

    public static RegistryObject<ArmorOfHellblazeMonarchItem> HELLBLAZE_MONARCH_HELMET = ITEMS.register("hellblaze_monarch_helmet",
            () -> new ArmorOfHellblazeMonarchItem(ArmorItem.Type.HELMET));
    public static RegistryObject<ArmorOfHellblazeMonarchItem> HELLBLAZE_MONARCH_CHESTPLATE = ITEMS.register("hellblaze_monarch_chestplate",
            () -> new ArmorOfHellblazeMonarchItem(ArmorItem.Type.CHESTPLATE));
    public static RegistryObject<ArmorOfHellblazeMonarchItem> HELLBLAZE_MONARCH_LEGGINGS = ITEMS.register("hellblaze_monarch_leggings",
            () -> new ArmorOfHellblazeMonarchItem(ArmorItem.Type.LEGGINGS));
    public static RegistryObject<ArmorOfHellblazeMonarchItem> HELLBLAZE_MONARCH_BOOTS = ITEMS.register("hellblaze_monarch_boots",
            () -> new ArmorOfHellblazeMonarchItem(ArmorItem.Type.BOOTS));

    // SPAWN EGGS
    public static RegistryObject<Item> UNBURNED_SPAWN_EGG = ITEMS.register("unburned_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.UNBURNED, GeneralUtils.rgbaToColorInteger(140, 100, 12, 100), GeneralUtils.rgbaToColorInteger(120, 90, 0, 100), new Item.Properties()));
}
