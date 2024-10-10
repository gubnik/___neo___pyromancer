package xyz.nikgub.pyromancer.registry;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.enchantment.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.common.entity.attack_effect.FlamingGuillotineEntity;
import xyz.nikgub.pyromancer.common.item.*;
import xyz.nikgub.pyromancer.common.item.armor.ArmorOfHellblazeMonarchItem;
import xyz.nikgub.pyromancer.common.item.armor.MarauderArmorItem;
import xyz.nikgub.pyromancer.common.item.armor.PyromancerArmorItem;
import xyz.nikgub.pyromancer.data.DamageTypeDatagen;

@SuppressWarnings("unused")
public class ItemRegistry
{
    public static final  DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PyromancerMod.MOD_ID);

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
    public static final RegistryObject<Item> CINNABAR_CHUNK = ITEMS.register("cinnabar_chunk", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DROPS_OF_MERCURY = ITEMS.register("drops_of_mercury", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRIMSTONE = ITEMS.register("brimstone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EVENBURNING_HEART = ITEMS.register("everburning_heart", EverburningHeartItem::new);
    public static final RegistryObject<Item> RIMEBLOOD = ITEMS.register("rimeblood", () -> new Item(new Item.Properties().rarity(RarityRegistry.FROST_RARITY)));
    public static final RegistryObject<Item> RIMEBRASS_INGOT = ITEMS.register("rimebrass_ingot", () -> new Item(new Item.Properties().rarity(RarityRegistry.FROST_RARITY)));
    public static final RegistryObject<Item> ANCIENT_PLATING = ITEMS.register("ancient_plating", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));



    public static final RegistryObject<BlazingJournalItem> BLAZING_JOURNAL = ITEMS.register("blazing_journal", () -> new BlazingJournalItem(new Item.Properties()));
    public static final RegistryObject<CompendiumOfFlameItem> COMPENDIUM_OF_FLAME = ITEMS.register("compendium_of_flame",
            () -> new CompendiumOfFlameItem(new Item.Properties()));


    public static final RegistryObject<AccursedContractItem> ACCURSED_CONTRACT = ITEMS.register("accursed_contract", AccursedContractItem::new);
    public static final RegistryObject<Item> EMBER_ITEM = ITEMS.register("ember", () -> new EmberItem(new Item.Properties()));
    public static final RegistryObject<Item> BLAZING_QUILL = ITEMS.register("blazing_quill",
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
                public void getAttack (Player player, Entity target, ItemStack weaponStack, ItemStack journalStack)
                {
                }

                @Override
                public boolean isActivated (DamageSource damageSource, Player player, Entity target, ItemStack weaponStack, ItemStack journalStack)
                {
                    return false;
                }
            }
    );

    public static final RegistryObject<Item> SMOLDERING_TWIG = ITEMS.register("smoldering_twig",
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
                public void getAttack (Player player, Entity target, ItemStack weaponStack, ItemStack journalStack)
                {
                    if (target instanceof LivingEntity entity)
                    {
                        entity.addEffect(new MobEffectInstance(MobEffectRegistry.OILED.get(), 20, 5));
                    }
                }

                @Override
                public boolean isActivated (DamageSource damageSource, Player player, Entity target, ItemStack weaponStack, ItemStack journalStack)
                {
                    if (!(target instanceof LivingEntity entity && !entity.hasEffect(MobEffectRegistry.OILED.get())))
                    {
                        return false;
                    }
                    var enchantments = journalStack.getAllEnchantments().keySet().stream().filter(enchantment -> enchantment instanceof BlazingJournalEnchantment).map(enchantment -> (BlazingJournalEnchantment) enchantment).toList();
                    for (BlazingJournalEnchantment enchantment : enchantments)
                    {
                        if (enchantment.getWeaponClass().isInstance(weaponStack.getItem()) && enchantment.getCondition(player, target))
                        {
                            return true;
                        }
                    }
                    return false;
                }
            }
    );

    public static final RegistryObject<Item> HELLBLAZE_QUILL = ITEMS.register("hellblaze_quill",
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
                    return 2;
                }

                @Override
                public void getAttack (Player player, Entity target, ItemStack weaponStack, ItemStack journalStack)
                {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.FIERY_AEGIS.get(), 40, 0, true, false));
                }

                @Override
                public boolean isActivated (DamageSource damageSource, Player player, Entity target, ItemStack weaponStack, ItemStack journalStack)
                {
                    return (damageSource.is(DamageTypeDatagen.IS_EMBER) || damageSource.is(DamageTypeDatagen.IS_PYROMANCY));
                }
            }
    );

    public static final RegistryObject<Item> SOULFLAME_QUILL = ITEMS.register("soulflame_quill",
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
                    return 0;
                }

                @Override
                public void getAttack (Player player, Entity target, ItemStack weaponStack, ItemStack journalStack)
                {
                    if (player instanceof ServerPlayer serverPlayer && serverPlayer.isCreative()) return;
                    final int cost = (int)player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get());
                    BlazingJournalItem.changeBlaze(player, cost);
                    player.setHealth(player.getHealth() - (float) cost / 2);
                    if (player instanceof ServerPlayer sPlayer)
                        sPlayer.sendChatMessage(
                                OutgoingChatMessage.create(PlayerChatMessage.system("Soulflame Quill")),
                                false,
                                ChatType.bind(ChatType.MSG_COMMAND_OUTGOING, sPlayer).withTargetName(sPlayer.getDisplayName()
                                )
                        );
                }

                @Override
                public boolean isActivated (DamageSource damageSource, Player player, Entity target, ItemStack weaponStack, ItemStack journalStack)
                {
                    return (damageSource.is(DamageTypeDatagen.IS_EMBER) || damageSource.is(DamageTypeDatagen.IS_PYROMANCY));
                }
            }
    );


    public static final RegistryObject<BombsackItem> BOMBSACK = ITEMS.register("bombsack",
            () -> new BombsackItem(new Item.Properties(), EntityTypeRegistry.BOMBSACK::get));
    public static final RegistryObject<BombsackItem> SCATTERSHOT_BOMBSACK = ITEMS.register("scattershot_bombsack",
            () -> new BombsackItem(new Item.Properties(), EntityTypeRegistry.SCATTERSHOT_BOMBSACK::get));
    public static final RegistryObject<BombsackItem> NAPALM_BOMBSACK = ITEMS.register("napalm_bombsack",
            () -> new BombsackItem(new Item.Properties(), EntityTypeRegistry.NAPALM_BOMBSACK::get));

    public static final RegistryObject<SizzlingHandItem> SIZZLING_HAND = ITEMS.register("sizzling_hand",
            () -> new SizzlingHandItem(new Item.Properties()));
    public static final RegistryObject<CourtOfEmbersItem> COURT_OF_EMBERS = ITEMS.register("court_of_embers",
            () -> new CourtOfEmbersItem(new Item.Properties()));
    public static final RegistryObject<SymbolOfSunItem> SYMBOL_OF_SUN = ITEMS.register("symbol_of_sun",
            () -> new SymbolOfSunItem(new Item.Properties()));
    public static final RegistryObject<FlammenklingeItem> FLAMMENKLINGE = ITEMS.register("flammenklinge",
            FlammenklingeItem::new);

    // TOOLS
    public static final RegistryObject<Item> AMBER_PICKAXE = ITEMS.register("amber_pickaxe",
            () -> new PickaxeItem(TierRegistry.AMBER, 1, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> AMBER_AXE = ITEMS.register("amber_axe",
            () -> new AxeItem(TierRegistry.AMBER, 5, -3.0f, new Item.Properties()));
    public static final RegistryObject<Item> AMBER_SHOVEL = ITEMS.register("amber_shovel",
            () -> new ShovelItem(TierRegistry.AMBER, 1, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> AMBER_HOE = ITEMS.register("amber_hoe",
            () -> new HoeItem(TierRegistry.AMBER, 1, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> AMBER_SWORD = ITEMS.register("amber_sword",
            () -> new SwordItem(TierRegistry.AMBER, 3, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> WOODEN_MACE = ITEMS.register("wooden_mace",
            () -> new MaceItem(Tiers.WOOD, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STONE_MACE = ITEMS.register("stone_mace",
            () -> new MaceItem(Tiers.STONE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IRON_MACE = ITEMS.register("iron_mace",
            () -> new MaceItem(Tiers.IRON, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GOLDEN_MACE = ITEMS.register("golden_mace",
            () -> new MaceItem(Tiers.GOLD, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DIAMOND_MACE = ITEMS.register("diamond_mace",
            () -> new MaceItem(Tiers.DIAMOND, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NETHERITE_MACE = ITEMS.register("netherite_mace",
            () -> new MaceItem(Tiers.NETHERITE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> AMBER_MACE = ITEMS.register("amber_mace",
            () -> new MaceItem(TierRegistry.AMBER, new Item.Properties()));

    // ARMOR
    public static final RegistryObject<MarauderArmorItem> MARAUDER_HELM = ITEMS.register("marauder_helm",
            () -> new MarauderArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<MarauderArmorItem> MARAUDER_CAPE = ITEMS.register("marauder_cape",
            () -> new MarauderArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<MarauderArmorItem> MARAUDER_PANTS = ITEMS.register("marauder_pants",
            () -> new MarauderArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<MarauderArmorItem> MARAUDER_BOOTS = ITEMS.register("marauder_boots",
            () -> new MarauderArmorItem(ArmorItem.Type.BOOTS));

    public static final RegistryObject<PyromancerArmorItem> PYROMANCER_HELMET = ITEMS.register("pyromancer_helmet",
            () -> new PyromancerArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<PyromancerArmorItem> PYROMANCER_CHESTPLATE = ITEMS.register("pyromancer_chestplate",
            () -> new PyromancerArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<PyromancerArmorItem> PYROMANCER_LEGGINGS = ITEMS.register("pyromancer_leggings",
            () -> new PyromancerArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<PyromancerArmorItem> PYROMANCER_BOOTS = ITEMS.register("pyromancer_boots",
            () -> new PyromancerArmorItem(ArmorItem.Type.BOOTS));

    public static final RegistryObject<ArmorOfHellblazeMonarchItem> HELLBLAZE_MONARCH_HELMET = ITEMS.register("hellblaze_monarch_helmet",
            () -> new ArmorOfHellblazeMonarchItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<ArmorOfHellblazeMonarchItem> HELLBLAZE_MONARCH_CHESTPLATE = ITEMS.register("hellblaze_monarch_chestplate",
            () -> new ArmorOfHellblazeMonarchItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<ArmorOfHellblazeMonarchItem> HELLBLAZE_MONARCH_LEGGINGS = ITEMS.register("hellblaze_monarch_leggings",
            () -> new ArmorOfHellblazeMonarchItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<ArmorOfHellblazeMonarchItem> HELLBLAZE_MONARCH_BOOTS = ITEMS.register("hellblaze_monarch_boots",
            () -> new ArmorOfHellblazeMonarchItem(ArmorItem.Type.BOOTS));
    // SPAWN EGGS
    public static final RegistryObject<Item> UNBURNED_SPAWN_EGG = ITEMS.register("unburned_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.UNBURNED, -10268354, -3297142, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> FROSTCOPPER_GOLEM_SPAWN_EGG = ITEMS.register("frostcopper_golem_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.FROSTCOPPER_GOLEM, -2390958, -9251329, new Item.Properties()));
    public static final RegistryObject<Item> PYROENT_SPAWN_EGG = ITEMS.register("pyroent_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.PYROENT, -10725806, -2403542, new Item.Properties()));
    public static final RegistryObject<Item> PYRACORN_SPAWN_EGG = ITEMS.register("pyracorn_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.PYRACORN, -8887200, -39359, new Item.Properties()));
    public static final RegistryObject<Item> SCORCH_SPAWN_EGG = ITEMS.register("scorch_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.SCORCH, -8891844, -3775978, new Item.Properties()));
}
