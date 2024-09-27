package xyz.nikgub.pyromancer.registry;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.entity.attack_effect.FlamingGuillotineEntity;
import xyz.nikgub.pyromancer.common.item.*;
import xyz.nikgub.pyromancer.common.item.armor.ArmorOfHellblazeMonarchItem;
import xyz.nikgub.pyromancer.common.item.armor.MarauderArmorItem;
import xyz.nikgub.pyromancer.common.item.armor.PyromancerArmorItem;

@SuppressWarnings("unused")
public class ItemRegistry
{
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PyromancerMod.MOD_ID);

    public static final RegistryObject<MusketAmmunitionItem> IRON_MUSKET_BALL = ITEMS.register("iron_musket_ball", () -> new MusketAmmunitionItem(new Item.Properties(), (itemStack, source, entity) -> 1f));
    public static final RegistryObject<MusketAmmunitionItem> INQUISITORIAL_MUSKET_BALL = ITEMS.register("inquisitorial_musket_ball", () -> new MusketAmmunitionItem(new Item.Properties(), (itemStack, source, entity) ->
    {
        if (!(entity.level() instanceof ServerLevel level)) return 0.5f;
        FlamingGuillotineEntity guillotine = FlamingGuillotineEntity.createWithDamage(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), level, MusketItem.getMusketDamage(itemStack) * 0.5f, true);
        if (source instanceof Player player) guillotine.setPlayerUuid(player.getUUID());
        guillotine.setSize(entity.getBbWidth() / 0.6f);
        guillotine.moveTo(entity.position());
        guillotine.setYRot(source.getYRot());
        level.addFreshEntity(guillotine);
        return 0.5f;
    }));

    public static final RegistryObject<Item> HOARFROST_GREATSWORD = ITEMS.register("hoarfrost_greatsword",
            () -> new HoarfrostGreatswordItem(new Item.Properties().rarity(RarityRegistry.FROST_RARITY)));
    public static final RegistryObject<Item> SPEAR_OF_MOROZ = ITEMS.register("spear_of_moroz",
            () -> new SpearOfMorozItem(new Item.Properties().rarity(RarityRegistry.FROST_RARITY)));
    public static final RegistryObject<ZweihanderItem> ZWEIHANDER = ITEMS.register("zweihander",
            () -> new ZweihanderItem(new Item.Properties()));
    public static final RegistryObject<MusketItem> MUSKET = ITEMS.register("musket",
            () -> new MusketItem(new Item.Properties()));


    // infusion
    public static final RegistryObject<InfusionItem> FIERY_INFUSION = ITEMS.register("fiery_infusion", () -> new InfusionItem(new Item.Properties(), MobEffectRegistry.FIERY_INFUSION.get(),
            Ingredient.of(Items.BLAZE_POWDER)));
    public static final RegistryObject<InfusionItem> ICE_INFUSION = ITEMS.register("ice_infusion", () -> new InfusionItem(new Item.Properties(), MobEffectRegistry.ICE_INFUSION.get(),
            Ingredient.of(Items.BLUE_ICE)));
    public static final RegistryObject<InfusionItem> CREEPER_INFUSION = ITEMS.register("creeper_infusion", () -> new InfusionItem(new Item.Properties(), MobEffectRegistry.CREEPER_INFUSION.get(),
            Ingredient.of(Items.TNT)));
    public static final RegistryObject<InfusionItem> MIDAS_INFUSION = ITEMS.register("midas_infusion", () -> new InfusionItem(new Item.Properties(), MobEffectRegistry.MIDAS_INFUSION.get(),
            Ingredient.of(Items.EXPERIENCE_BOTTLE)));
    public static final RegistryObject<InfusionItem> OIL_INFUSION = ITEMS.register("oil_infusion", () -> new InfusionItem(new Item.Properties(), MobEffectRegistry.OIL_INFUSION.get(),
            Ingredient.of(Items.COAL_BLOCK)));


    // MATERIALS
    public static RegistryObject<Item> CINNABAR_CHUNK = ITEMS.register("cinnabar_chunk", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> DROPS_OF_MERCURY = ITEMS.register("drops_of_mercury", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> BRIMSTONE = ITEMS.register("brimstone", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> EVENBURNING_HEART = ITEMS.register("everburning_heart", EverburningHeartItem::new);
    public static final RegistryObject<Item> RIMEBLOOD = ITEMS.register("rimeblood", () -> new Item(new Item.Properties().rarity(RarityRegistry.FROST_RARITY)));
    public static final RegistryObject<Item> RIMEBRASS_INGOT = ITEMS.register("rimebrass_ingot", () -> new Item(new Item.Properties().rarity(RarityRegistry.FROST_RARITY)));
    public static final RegistryObject<Item> ANCIENT_PLATING = ITEMS.register("ancient_plating", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<AccursedContractItem> ACCURSED_CONTRACT = ITEMS.register("accursed_contract", () -> new AccursedContractItem(new Item.Properties().stacksTo(1)));


    public static RegistryObject<BlazingJournalItem> BLAZING_JOURNAL = ITEMS.register("blazing_journal", () -> new BlazingJournalItem(new Item.Properties()));
    public static RegistryObject<CompendiumOfFlameItem> COMPENDIUM_OF_FLAME = ITEMS.register("compendium_of_flame",
            () -> new CompendiumOfFlameItem(new Item.Properties()));


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
                public boolean getCondition (Player player, ItemStack weaponStack, ItemStack journalStack)
                {
                    return false;
                }
            }
    );
    public static RegistryObject<Item> SMOLDERING_TWIG = ITEMS.register("smoldering_twig",
            () -> new QuillItem(new Item.Properties())
            {
                @Override
                public float getDefaultPyromancyDamageBonus ()
                {
                    return 0;
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
                public boolean getCondition (Player player, ItemStack weaponStack, ItemStack journalStack)
                {
                    return false;
                }
            }
    );


    public static RegistryObject<BombsackItem> BOMBSACK = ITEMS.register("bombsack",
            () -> new BombsackItem(new Item.Properties(), EntityTypeRegistry.BOMBSACK::get));
    public static RegistryObject<BombsackItem> SCATTERSHOT_BOMBSACK = ITEMS.register("scattershot_bombsack",
            () -> new BombsackItem(new Item.Properties(), EntityTypeRegistry.SCATTERSHOT_BOMBSACK::get));
    public static RegistryObject<BombsackItem> NAPALM_BOMBSACK = ITEMS.register("napalm_bombsack",
            () -> new BombsackItem(new Item.Properties(), EntityTypeRegistry.NAPALM_BOMBSACK::get));

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
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.UNBURNED, -10268354, -3297142, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> FROSTCOPPER_GOLEM_SPAWN_EGG = ITEMS.register("frostcopper_golem_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.FROSTCOPPER_GOLEM, -2390958, -9251329, new Item.Properties()));
    public static RegistryObject<Item> PYROENT_SPAWN_EGG = ITEMS.register("pyroent_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.PYROENT, -10725806, -2403542, new Item.Properties()));
    public static RegistryObject<Item> PYRACORN_SPAWN_EGG = ITEMS.register("pyracorn_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.PYRACORN, -8887200, -39359, new Item.Properties()));
    public static RegistryObject<Item> SCORCH_SPAWN_EGG = ITEMS.register("scorch_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.SCORCH, -8891844, -3775978, new Item.Properties()));
}
