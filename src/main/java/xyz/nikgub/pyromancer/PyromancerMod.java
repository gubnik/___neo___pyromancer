/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, nikgub_

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

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.DetectedVersion;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.client.model.armor.ArmorOfHellblazeMonarchModel;
import xyz.nikgub.pyromancer.client.model.armor.PyromancerArmorModel;
import xyz.nikgub.pyromancer.client.model.entity.*;
import xyz.nikgub.pyromancer.client.renderer.*;
import xyz.nikgub.pyromancer.common.ember.Ember;
import xyz.nikgub.pyromancer.common.enchantment.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.common.entity.FrostcopperGolemEntity;
import xyz.nikgub.pyromancer.common.entity.UnburnedEntity;
import xyz.nikgub.pyromancer.common.entity.UnburnedSpiritEntity;
import xyz.nikgub.pyromancer.common.event.BlazingJournalAttackEvent;
import xyz.nikgub.pyromancer.common.item.*;
import xyz.nikgub.pyromancer.common.util.ItemUtils;
import xyz.nikgub.pyromancer.common.worldgen.NetherPyrowoodTrunkPlacer;
import xyz.nikgub.pyromancer.data.DamageTypeDatagen;
import xyz.nikgub.pyromancer.data.RegistriesDataGeneration;
import xyz.nikgub.pyromancer.network.NetworkCore;
import xyz.nikgub.pyromancer.registries.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod(PyromancerMod.MOD_ID)
public class PyromancerMod
{
    public static int clientTick;
    public static final String MOD_ID = "pyromancer";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final RegistryObject<CreativeModeTab> PYROMANCER_TAB = CREATIVE_MODE_TABS.register("pyromancer", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.pyromancer"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemRegistry.BLAZING_JOURNAL.get().getDefaultInstance())
            .build());

    public PyromancerMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupClient);
        modEventBus.addListener(this::registerLayerDefinitions);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::entityAttributeSupplier);

        EmberRegistry.EMBERS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        EnchantmentRegistry.ENCHANTMENTS.register(modEventBus);
        MobEffectRegistry.MOB_EFFECTS.register(modEventBus);
        NetherPyrowoodTrunkPlacer.TRUNK_TYPE_REGISTRY.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PyromancerConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        NetworkCore.register();
        NetherBiomeRegistry.setupTerrablender();
    }

    private void setupClient(final FMLCommonSetupEvent event)
	{
        EntityRenderers.register(EntityTypeRegistry.BOMBSACK.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.SIZZLING_HAND_FIREBALL.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), FlamingGuillotineRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.UNBURNED_SPIRIT.get(), UnburnedSpiritRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.PYRONADO.get(), PyronadoRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.UNBURNED.get(), UnburnedRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.FROSTCOPPER_GOLEM.get(), FrostcopperGolemRenderer::new);
    }

    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
        event.registerLayerDefinition(PyromancerArmorModel.LAYER_LOCATION, PyromancerArmorModel::createBodyLayer);
        event.registerLayerDefinition(ArmorOfHellblazeMonarchModel.LAYER_LOCATION, ArmorOfHellblazeMonarchModel::createBodyLayer);
        event.registerLayerDefinition(FlamingGuillotineModel.LAYER_LOCATION, FlamingGuillotineModel::createBodyLayer);
        event.registerLayerDefinition(UnburnedSpiritModel.LAYER_LOCATION, UnburnedSpiritModel::createBodyLayer);
        event.registerLayerDefinition(PyronadoModel.LAYER_LOCATION, PyronadoModel::createBodyLayer);
        event.registerLayerDefinition(UnburnedModel.LAYER_LOCATION, UnburnedModel::createBodyLayer);
        event.registerLayerDefinition(FrostcopperGolemModel.LAYER_LOCATION, FrostcopperGolemModel::createbodyLayer);
    }

    private void entityAttributeSupplier(EntityAttributeCreationEvent event)
    {
        event.put(EntityTypeRegistry.UNBURNED.get(), UnburnedEntity.setAttributes());
        event.put(EntityTypeRegistry.FROSTCOPPER_GOLEM.get(), FrostcopperGolemEntity.setAttributes());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTab().equals(PYROMANCER_TAB.get()))
        {
            List<Item> TOOLS = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof TieredItem).map(RegistryObject::get).toList();
            List<Item> PYROMANCY = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof IPyromancyItem).map(RegistryObject::get).toList();
            List<Item> QUILLS = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof QuillItem).map(RegistryObject::get).toList();
            List<ItemStack> EMBERS = EmberRegistry.REGISTRY.get().getValues().stream().map(ember -> ember.applyToItemStack(new ItemStack(ItemRegistry.EMBER_ITEM.get()))).toList();
            List<Item> ARMOR = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof ArmorItem).map(RegistryObject::get).toList();

            List<Item> ALL_ELSE = new ArrayList<>(ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> !(item instanceof BlockItem) && !(item instanceof SpawnEggItem)).toList());
            ALL_ELSE.removeAll(TOOLS);
            ALL_ELSE.removeAll(PYROMANCY);
            ALL_ELSE.removeAll(QUILLS);
            ALL_ELSE.removeAll(ARMOR);

            for (Item item : ALL_ELSE) event.accept(item);
            for (Item item : TOOLS) event.accept(item);
            for (Item item : ARMOR) event.accept(item);
            event.accept(new ItemStack(ItemRegistry.BLAZING_JOURNAL.get()));
            event.accept(new ItemStack(ItemRegistry.COMPENDIUM_OF_FLAME.get()));
            for (Item item : QUILLS) event.accept(item);
            for (Item item : PYROMANCY) event.accept(item);
            event.accept(new ItemStack(ItemRegistry.EMBER_ITEM.get()));
            for (ItemStack item : EMBERS) event.accept(item);
        }
        else if(event.getTabKey().equals(CreativeModeTabs.BUILDING_BLOCKS))
        {
            List<Block> BLOCKS = BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
            for (Block block : BLOCKS) event.accept(block);
        }
        else if(event.getTabKey().equals(CreativeModeTabs.SPAWN_EGGS))
        {
            event.accept(new ItemStack(ItemRegistry.UNBURNED_SPAWN_EGG.get()));
        }
    }

    public void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        /**/
        RegistriesDataGeneration.addProviders(event.includeServer(), generator, output, lookupProvider, existingFileHelper);
        generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
                Component.literal("Resources for Pyromancer"),
                DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
                Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion)))));
    }

    @SubscribeEvent
    public void lootLoad(LootTableLoadEvent event)
	{
        List<String> toModify = List.of(
                "minecraft:chests/simple_dungeon",
                "minecraft:chests/abandoned_mineshaft",
                "minecraft:chests/igloo_chest"
        );
        if (toModify.stream().anyMatch((s -> event.getName().toString().equals(s)))) {
            event.getTable().addPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(ItemRegistry.ANCIENT_PLATING.get()))
                    .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.04F, 0.04F)
                    ).build());
        }
    }

    @Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    @SuppressWarnings("unused")
    public static class ModEvents
    {
        @SubscribeEvent
        public static void entityAttributeProvider(EntityAttributeModificationEvent event)
	{
            event.add(EntityType.PLAYER, AttributeRegistry.BLAZE_CONSUMPTION.get());
            event.add(EntityType.PLAYER, AttributeRegistry.PYROMANCY_DAMAGE.get());
            event.add(EntityType.PLAYER, AttributeRegistry.ARMOR_PIERCING.get());
            event.add(EntityType.PLAYER, AttributeRegistry.COLD_BUILDUP.get());
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    @SuppressWarnings("unused")
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event)
        {

        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    @SuppressWarnings("unused")
    public static class ClientForgeEvents
    {
        @SubscribeEvent
        public static void clientTick(final TickEvent.ClientTickEvent event)
        {
            clientTick++;
        }
        @SubscribeEvent
        public static void tooltipColorEvent(RenderTooltipEvent.Color event)
        {
            ItemStack itemStack = event.getItemStack();
            Ember ember = Ember.getFromItem(itemStack);
            if (ember != null)
            {
                event.setBackgroundStart(ember.getType().getColor());
            }
        }
        @SubscribeEvent
        public static void tooltipContentsEvent(ItemTooltipEvent event)
        {
            ItemStack itemStack = event.getItemStack();
            Ember ember = Ember.getFromItem(itemStack);
            if(ember == null) return;
            if(itemStack.getItem() instanceof EmberItem
            && event.getFlags() == TooltipFlag.ADVANCED)
            {
                event.getToolTip().add(Component.translatable(ember.toString()).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
    @SuppressWarnings("unused")
    public static class ServerForgeEvents
    {
    }

    @Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    @SuppressWarnings("unused")
    public static class ForgeEvents
    {
        public static void frostProcessor (LivingHurtEvent event)
        {

            final LivingEntity target = event.getEntity();

            if (!target.canFreeze()) return;

            final DamageSource damageSource = event.getSource();
            final Entity cause = damageSource.getEntity();
            final float damageAmount = event.getAmount();
            final int frostburnModifier = target.hasEffect(MobEffectRegistry.FROSTBURN.get()) ? target.getEffect(MobEffectRegistry.FROSTBURN.get()).getAmplifier() : -1;
            if (frostburnModifier >= 0)
            {
                if (!target.isAlive())
                {
                    event.setCanceled(event.isCancelable());
                    return;
                }
                float newDamage = damageAmount + (float) (Math.sqrt(frostburnModifier + 1) * (1 + Math.log(frostburnModifier + 1)));
                event.setAmount(newDamage);
            }
            if (GeneralUtils.isDirectDamage(damageSource))
            {
                if (!(cause instanceof LivingEntity entity)) return;

                AttributeMap attributeMap = entity.getAttributes();

                if (!(attributeMap.hasAttribute(AttributeRegistry.COLD_BUILDUP.get()))) return;

                double coldBuildup = attributeMap.getValue(AttributeRegistry.COLD_BUILDUP.get());

                if (coldBuildup <= 0) return;

                target.setTicksFrozen(target.getTicksFrozen() + (int)(coldBuildup * (damageAmount + 1)) + 1);
                GeneralUtils.coverInParticles(target, ParticleTypes.SNOWFLAKE, 0.002);
                ItemStack mainHandItem = entity.getMainHandItem();
                final int meltLevel = mainHandItem.getEnchantmentLevel(EnchantmentRegistry.MELT.get());
                final double meltModifier = (target.isOnFire()) ? meltLevel * 0.1D : 0.0D;
                if (meltModifier != 0)
                {
                    event.setAmount(event.getAmount() * (1 + meltLevel));
                    target.extinguishFire();
                }

                if (!(target.isFullyFrozen())) return;

                target.addEffect(new MobEffectInstance(MobEffectRegistry.FROSTBURN.get(), 100, frostburnModifier + 1, false, true, false));
                target.setTicksFrozen(target.getTicksFrozen() + 200);
            }

        }

        @SubscribeEvent
        public static void onLivingHurt (LivingHurtEvent event)
        {
            if (event.getEntity().hasEffect(MobEffectRegistry.FIERY_AEGIS.get()) && event.getSource().getDirectEntity() instanceof LivingEntity attacker)
            {
                event.setAmount(MobEffectRegistry.FIERY_AEGIS.get().performAttack(event.getAmount(), event.getEntity(), attacker));
            }
            if (event.getSource().getDirectEntity() instanceof Player attacker && ItemUtils.hasFullSetEquipped(attacker, ItemRegistry.HELLBLAZE_MONARCH_HELMET.get())
            && event.getEntity().level() instanceof ServerLevel level && event.getAmount() >= 7 && (event.getSource().is(DamageTypeDatagen.IS_PYROMANCY) || event.getSource().is(DamageTypeDatagen.IS_EMBER)))
            {
                UnburnedSpiritEntity spirit = new UnburnedSpiritEntity(EntityTypeRegistry.UNBURNED_SPIRIT.get(), level);
                spirit.addToLevelForPlayerAt(level, attacker, event.getEntity().position());
            }
            frostProcessor(event);
            if (!(event.getSource().getEntity() instanceof Player sourceEntity)) return;
            LivingEntity entity = event.getEntity();
            double armor_pierce = sourceEntity.getAttributeValue(AttributeRegistry.ARMOR_PIERCING.get());
            if (armor_pierce >= entity.getAttributeValue(Attributes.ARMOR))
                event.setAmount(event.getAmount() + (float) armor_pierce);
        }

        @SubscribeEvent
        public static void livingAttackEvent(LivingAttackEvent event)
	{
            DamageSource damageSource = event.getSource();
            blazingJournalAdvancementProcessor(damageSource.getEntity(), event.getEntity(), damageSource);
            if (!(damageSource.getDirectEntity() instanceof Player player)) return;
            Entity target = event.getEntity();
            ItemStack weapon = player.getMainHandItem();
            ItemStack journal = ItemUtils.guessJournal(player);
            if (journal == ItemStack.EMPTY || !(journal.getItem() instanceof BlazingJournalItem blazingJournalItem)) return;
            blazingJournalQuillProcessor(blazingJournalItem, journal, weapon, player, target);
            blazingJournalAttackProcessor(journal, weapon, player, target);
        }

        public static void blazingJournalAdvancementProcessor(Entity player, @Nullable Entity entity, @Nullable DamageSource source)
        {
            if(player == null) return;
            if(!(player instanceof ServerPlayer sPlayer)) return;
            if(source == null) return;
            if(source.is(DamageTypeDatagen.JOURNAL_PROJECTION))
            {
                GeneralUtils.addAdvancement(sPlayer, new ResourceLocation(PyromancerMod.MOD_ID,"pyromancer/journal_projection"));
            }
        }

        public static void blazingJournalQuillProcessor(BlazingJournalItem blazingJournalItem, ItemStack journal, ItemStack weapon, Player player, Entity target)
        {
            if ((blazingJournalItem.getItemFromItem(journal, 0)).getItem() instanceof QuillItem quillItem)
	{
                if (quillItem.getCondition(player, weapon, journal) && !target.hurtMarked)
                    quillItem.getAttack(player, weapon, journal);
            }
        }

        public static void blazingJournalAttackProcessor(ItemStack journal, ItemStack weapon, Player player, Entity target)
        {
            for (BlazingJournalEnchantment blazingJournalEnchantment : journal.getAllEnchantments().keySet().stream().filter(enchantment -> enchantment instanceof BlazingJournalEnchantment).map(enchantment -> (BlazingJournalEnchantment) enchantment).toList())
            {
                if (blazingJournalEnchantment.globalCondition(player)) {
                    if (blazingJournalEnchantment.getWeaponClass().isInstance(weapon.getItem())
                            && blazingJournalEnchantment.getCondition(player, target)) {
                        BlazingJournalAttackEvent blazingJournalAttackEvent = BlazingJournalItem.getBlazingJournalAttackEvent(player, target, journal, weapon, blazingJournalEnchantment);
                        blazingJournalEnchantment.getAttack(player, target);
                        ItemUtils.changeBlaze(player, -1);
                    }
                } else break;
            }
        }
    }
}
