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

import com.mojang.logging.LogUtils;
import net.minecraft.DetectedVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
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
import xyz.nikgub.incandescent.Incandescent;
import xyz.nikgub.incandescent.common.event.SyncEntityNBTEvent;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.incandescent.pyranim.parser.PyranimParser;
import xyz.nikgub.pyromancer.client.model.armor.ArmorOfHellblazeMonarchModel;
import xyz.nikgub.pyromancer.client.model.armor.PyromancerArmorModel;
import xyz.nikgub.pyromancer.client.model.entity.*;
import xyz.nikgub.pyromancer.client.particle.BrimflameParticle;
import xyz.nikgub.pyromancer.client.particle.MercuryVaporParticle;
import xyz.nikgub.pyromancer.client.renderer.entity.*;
import xyz.nikgub.pyromancer.common.contract.ContractDirector;
import xyz.nikgub.pyromancer.common.enchantment.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.common.entity.*;
import xyz.nikgub.pyromancer.common.item.*;
import xyz.nikgub.pyromancer.common.worldgen.NetherPyrowoodTrunkPlacer;
import xyz.nikgub.pyromancer.data.BiomeDatagen;
import xyz.nikgub.pyromancer.data.DamageTypeDatagen;
import xyz.nikgub.pyromancer.data.ItemTagDatagen;
import xyz.nikgub.pyromancer.data.RegistriesDataGeneration;
import xyz.nikgub.pyromancer.network.NetworkCore;
import xyz.nikgub.pyromancer.registry.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod(PyromancerMod.MOD_ID)
public class PyromancerMod
{
    public static final String MOD_ID = "pyromancer";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final RegistryObject<CreativeModeTab> PYROMANCER_TAB = CREATIVE_MODE_TABS.register("pyromancer", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.pyromancer"))
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> ItemRegistry.BLAZING_JOURNAL.get().getDefaultInstance())
        .build());
    public static int clientTick;

    public static final PyranimParser PARSER = new PyranimParser.Builder().build();

    public static Map<Item, Boolean> DO_INFUSION_RENDER;

    public PyromancerMod ()
    {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PyromancerConfig.SPEC);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupClient);

        modEventBus.addListener(this::registerLayerDefinitions);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::entityAttributeSupplier);
        modEventBus.addListener(this::registerSpawnPlacements);

        ParticleRegistry.PARTICLES.register(modEventBus);
        ContractRegistry.CONTRACTS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        EnchantmentRegistry.ENCHANTMENTS.register(modEventBus);
        MobEffectRegistry.MOB_EFFECTS.register(modEventBus);
        NetherPyrowoodTrunkPlacer.TRUNK_TYPE_REGISTRY.register(modEventBus);
        SoundEventRegistry.SOUNDS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::registerParticles);
    }

    private void commonSetup (final FMLCommonSetupEvent event)
    {
        NetworkCore.register();
        BiomeDatagen.setupTerrablender();
        event.enqueueWork(InfusionItem::makeRecipes);

        DO_INFUSION_RENDER = BuiltInRegistries.ITEM.stream().collect(Collectors.toMap(item -> item, item ->
            Arrays.stream(item.getClass().getMethods()).anyMatch(m -> Arrays.equals(m.getParameterTypes(), new Class<?>[]{Level.class, Player.class, InteractionHand.class}) &&
                m.getReturnType() == InteractionResultHolder.class && m.getDeclaringClass() == Item.class) && item.getMaxStackSize(item.getDefaultInstance()) == 1));
    }

    private void setupClient (final FMLCommonSetupEvent event)
    {
        EntityRenderers.register(EntityTypeRegistry.BOMBSACK.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.SCATTERSHOT_BOMBSACK.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.NAPALM_BOMBSACK.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.SIZZLING_HAND_FIREBALL.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), FlamingGuillotineRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.UNBURNED_SPIRIT.get(), UnburnedSpiritRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.PYRONADO.get(), PyronadoRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.UNBURNED.get(), UnburnedRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.FROSTCOPPER_GOLEM.get(), FrostcopperGolemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.SCORCH.get(), ScorchRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.PYRACORN.get(), PyracornRender::new);
        EntityRenderers.register(EntityTypeRegistry.PYROENT.get(), PyroentRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.RIMEGAZER.get(), RimegazerRenderer::new);
    }

    public void registerParticles (RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(ParticleRegistry.BRIMSTONE_FLAME_PARTICLE.get(), BrimflameParticle::provider);
        event.registerSpriteSet(ParticleRegistry.MERCURY_VAPOR.get(), MercuryVaporParticle::provider);
        event.registerSpriteSet(ParticleRegistry.VAPORIZER_MERCURY.get(), MercuryVaporParticle::vaporizerProvider);
    }

    private void registerLayerDefinitions (EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(PyromancerArmorModel.LAYER_LOCATION, PyromancerArmorModel::createBodyLayer);
        event.registerLayerDefinition(ArmorOfHellblazeMonarchModel.LAYER_LOCATION, ArmorOfHellblazeMonarchModel::createBodyLayer);
        event.registerLayerDefinition(FlamingGuillotineModel.LAYER_LOCATION, FlamingGuillotineModel::createBodyLayer);
        event.registerLayerDefinition(UnburnedSpiritModel.LAYER_LOCATION, UnburnedSpiritModel::createBodyLayer);
        event.registerLayerDefinition(PyronadoModel.LAYER_LOCATION, PyronadoModel::createBodyLayer);
        event.registerLayerDefinition(UnburnedModel.LAYER_LOCATION, UnburnedModel::createBodyLayer);
        event.registerLayerDefinition(FrostcopperGolemModel.LAYER_LOCATION, FrostcopperGolemModel::createbodyLayer);
        event.registerLayerDefinition(ScorchModel.LAYER_LOCATION, ScorchModel::createBodyLayer);
        event.registerLayerDefinition(PyracornModel.LAYER_LOCATION, PyracornModel::createBodyLayer);
        event.registerLayerDefinition(PyroentModel.LAYER_LOCATION, PyroentModel::createBodyLayer);
        event.registerLayerDefinition(RimegazerModel.LAYER_LOCATION, RimegazerModel::createBodyLayer);
    }

    private void entityAttributeSupplier (EntityAttributeCreationEvent event)
    {
        event.put(EntityTypeRegistry.UNBURNED.get(), UnburnedEntity.setAttributes());
        event.put(EntityTypeRegistry.FROSTCOPPER_GOLEM.get(), FrostcopperGolemEntity.setAttributes());
        event.put(EntityTypeRegistry.SCORCH.get(), ScorchEntity.setAttributes());
        event.put(EntityTypeRegistry.PYRACORN.get(), PyracornEntity.setAttributes());
        event.put(EntityTypeRegistry.PYROENT.get(), PyroentEntity.setAttributes());
        event.put(EntityTypeRegistry.RIMEGAZER.get(), RimegazerEntity.setAttributes());
    }

    private void addCreative (BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTab().equals(PYROMANCER_TAB.get()))
        {
            List<Item> PYROMANCY = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof IPyromancyItem).map(RegistryObject::get).toList();
            List<Item> TOOLS = new ArrayList<>(ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof TieredItem && !(registryObject.get() instanceof IPyromancyItem)).map(RegistryObject::get).toList());
            List<Item> QUILLS = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof QuillItem).map(RegistryObject::get).toList();
            List<Item> ARMOR = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof ArmorItem).map(RegistryObject::get).toList();
            List<Item> INFUSIONS = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof InfusionItem).map(RegistryObject::get).toList();
            List<Item> ALL_ELSE = new ArrayList<>(ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> !(item instanceof BlockItem) && !(item instanceof SpawnEggItem)).toList());
            TOOLS.addAll(List.of(
                ItemRegistry.ZWEIHANDER.get(),
                ItemRegistry.MUSKET.get(),
                ItemRegistry.HOARFROST_GREATSWORD.get(),
                ItemRegistry.SPEAR_OF_MOROZ.get()
            ));
            ALL_ELSE.removeAll(TOOLS);
            ALL_ELSE.removeAll(PYROMANCY);
            ALL_ELSE.removeAll(QUILLS);
            ALL_ELSE.removeAll(ARMOR);
            ALL_ELSE.removeAll(INFUSIONS);

            event.accept(new ItemStack(ItemRegistry.BLAZING_JOURNAL.get()));
            event.accept(new ItemStack(ItemRegistry.COMPENDIUM_OF_FLAME.get()));
            for (Item item : TOOLS) event.accept(item);
            for (Item item : ARMOR) event.accept(item);
            for (Item item : QUILLS) event.accept(item);
            for (Item item : PYROMANCY) event.accept(item);
            for (Item item : INFUSIONS) event.accept(item);
            for (Item item : ALL_ELSE) event.accept(item);
        } else if (event.getTabKey().equals(CreativeModeTabs.BUILDING_BLOCKS) || event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS))
        {
            List<Block> BLOCKS = BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
            for (Block block : BLOCKS)
            {
                boolean isDecorative = block instanceof GrowingPlantBlock || block instanceof BushBlock || block instanceof TallGrassBlock;
                if ((isDecorative && event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS))
                    || (!isDecorative && event.getTabKey().equals(CreativeModeTabs.BUILDING_BLOCKS)))
                {
                    event.accept(block);
                }
            }
        } else if (event.getTabKey().equals(CreativeModeTabs.SPAWN_EGGS))
        {
            ItemRegistry.ITEMS.getEntries().stream().filter(itemRegistryObject -> itemRegistryObject.get() instanceof SpawnEggItem).forEach(itemRegistryObject -> event.accept(itemRegistryObject.get()));
        }
    }

    public void gatherData (GatherDataEvent event)
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

    public void registerSpawnPlacements (SpawnPlacementRegisterEvent event)
    {
        //event.register(EntityTypeRegistry.FROSTCOPPER_GOLEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FrostcopperGolemEntity::spawnPredicate, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(EntityTypeRegistry.PYRACORN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PyracornEntity::spawnPredicate, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(EntityTypeRegistry.SCORCH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScorchEntity::spawnPredicate, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(EntityTypeRegistry.PYROENT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PyroentEntity::spawnPredicate, SpawnPlacementRegisterEvent.Operation.OR);
    }

    @SubscribeEvent
    public void nbtSync (SyncEntityNBTEvent event)
    {
        if (ContractRegistry.isContractMob(event.getEntity()))
        {
            event.setDoSync(true);
        }
    }

    @SubscribeEvent
    public void lootLoad (LootTableLoadEvent event)
    {
        final String eventName = event.getName().toString();
        List<String> toModify = List.of(
            "minecraft:chests/simple_dungeon",
            "minecraft:chests/abandoned_mineshaft",
            "minecraft:chests/igloo_chest"
        );
        if (toModify.stream().anyMatch((eventName::equals)))
        {
            event.getTable().addPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(ItemRegistry.ANCIENT_PLATING.get()))
                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.04F, 0.04F)
                ).build());
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void attackEntityEvent (final AttackEntityEvent event)
    {
        Player player = event.getEntity();
        if (!(player.getMainHandItem().getItem() instanceof ZweihanderItem)) return;
        if (!(event.getTarget() instanceof LivingEntity)) return;
        player.getMainHandItem().getOrCreateTag().putBoolean("ProperSwing", (player.getAttackStrengthScale(0) == 1));
        if (player.getAttackStrengthScale(0) != 1) event.setCanceled(true);
    }

    @SubscribeEvent
    public void livingDeathEvent (LivingDeathEvent event)
    {
        DamageSource damageSource = event.getSource();
        ItemStack hand;
        if (damageSource.is(DamageTypes.FALL) && event.getEntity() instanceof ServerPlayer entity && entity.getMainHandItem().is(ItemTagDatagen.FLAMMENKLINGE_PLUNGE_COMPATIBLE))
        {
            GeneralUtils.addAdvancement(entity, new ResourceLocation(MOD_ID, "pyromancer/flammenklinge_death"));
        }
        if (damageSource.getEntity() instanceof LivingEntity entity && (hand = entity.getMainHandItem()).getItem() instanceof MusketItem && !MusketItem.isLoaded(hand)
            && hand.getEnchantmentLevel(EnchantmentRegistry.TROOPER.get()) != 0 && GeneralUtils.isDirectDamage(damageSource))
        {
            MusketItem.reload(entity, hand);
        }
    }

    @Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    @SuppressWarnings("unused")
    public static class ModEvents
    {
        @SubscribeEvent
        public static void entityAttributeProvider (EntityAttributeModificationEvent event)
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
        public static void registerClientTooltip (RegisterClientTooltipComponentFactoriesEvent event)
        {

        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    @SuppressWarnings("unused")
    public static class ClientForgeEvents
    {
        @SubscribeEvent
        public static void clientTick (final TickEvent.ClientTickEvent event)
        {
            clientTick++;
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

        @SubscribeEvent
        public static void onEntityJoinLevel (EntityJoinLevelEvent event)
        {
            //Entity entity = event.getEntity();
            //if (entity.level() instanceof ServerLevel)
            //{
            //    NetworkCore.sendToAll(new SyncEntityNbtPacket(entity.getId(), entity.getPersistentData()));
            //    LOGGER.info("Server entity : {}", entity);
            //    LOGGER.info("Server NBT : {}", entity.getPersistentData());
            //}
            //CompoundTag tag = new CompoundTag();
            //tag.putBoolean(ContractDirector.CONTRACT_SPAWN_TAG, true);
            //LOGGER.info("Client entity : {}", entity);
            //NetworkCore.sendToServer(new FixContractSpawnNbtPacket(entity.getId(), tag));
        }

        @SubscribeEvent
        public static void preventContractFriendlyFire (LivingAttackEvent event)
        {
            boolean isHurtContract = event.getEntity().getPersistentData().getBoolean(ContractDirector.CONTRACT_SPAWN_TAG);
            DamageSource source = event.getSource();
            boolean isSourceContract = source.getEntity() != null && source.getEntity().getPersistentData().getBoolean(ContractDirector.CONTRACT_SPAWN_TAG);
            boolean isDirectSourceContract = source.getDirectEntity() != null && source.getDirectEntity().getPersistentData().getBoolean(ContractDirector.CONTRACT_SPAWN_TAG);
            if (isHurtContract && (isSourceContract || isDirectSourceContract))
            {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void preventContractDrops (LivingDropsEvent event)
        {
            if (event.getEntity().getPersistentData().getBoolean(ContractDirector.CONTRACT_SPAWN_TAG))
            {
                event.setCanceled(event.isCancelable());
            }
        }

        @SubscribeEvent
        public static void preventContractExperienceDrops (LivingExperienceDropEvent event)
        {
            if (event.getEntity().getPersistentData().getBoolean(ContractDirector.CONTRACT_SPAWN_TAG))
            {
                event.setCanceled(event.isCancelable());
            }
        }

        @SubscribeEvent
        public static void livingFallEvent (LivingFallEvent event)
        {
            LivingEntity entity = event.getEntity();
            if (!(entity instanceof Player player)) return;
            ItemStack stack = FlammenklingeItem.fetchStack(player);
            if (stack.isEmpty()) return;
            CompoundTag tag = stack.getOrCreateTag();
            float dmgBonus = event.getDistance();
            if (dmgBonus < 2)
            {
                tag.putInt(FlammenklingeItem.ENEMIES_COUNTER_TAG, 0);
                return;
            }
            final List<Vec3> positions = new ArrayList<>();
            if (tag.getInt(FlammenklingeItem.ENEMIES_COUNTER_TAG) > 0)
            {
                if (player instanceof ServerPlayer sp && tag.getInt(FlammenklingeItem.ENEMIES_COUNTER_TAG) >= 3)
                {
                    GeneralUtils.addAdvancement(sp, new ResourceLocation(MOD_ID, "pyromancer/flammenklinge_plunge"));
                }
                tag.putInt(FlammenklingeItem.ENEMIES_COUNTER_TAG, 0);
                for (LivingEntity target : EntityUtils.entityCollector(player.getPosition(0), 4 + Mth.clamp(dmgBonus / 4, 0, 2), player.level()))
                {
                    if (target == player) continue;
                    target.setRemainingFireTicks(target.getRemainingFireTicks() + 80);
                    target.hurt(DamageSourceRegistry.flammenklingeLand(player), (float) player.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()) + dmgBonus);
                    target.setDeltaMovement(target.position().subtract(entity.position()).scale(0.75));
                    if (target.level() instanceof ServerLevel level)
                    {
                        final Vec3 pos = BlockPos.containing(target.position()).below().getCenter();
                        level.sendParticles(ParticleTypes.LAVA, pos.x, pos.y + 2, pos.z, 20, 0, 4, 0, 0.02);
                    }
                }
                event.setCanceled(event.isCancelable());
                assert Minecraft.getInstance().player != null;
                final long endTick = Minecraft.getInstance().player.tickCount + 6;
                Incandescent.runShakeFor(5D, localPlayer -> localPlayer.tickCount > endTick);
            }
        }

        @SubscribeEvent
        public static void livingAttackEvent (LivingAttackEvent event)
        {
            DamageSource damageSource = event.getSource();
            blazingJournalAdvancementProcessor(damageSource.getEntity(), event.getEntity(), damageSource);
            if (!(damageSource.getDirectEntity() instanceof Player player)) return;
            Entity target = event.getEntity();
            ItemStack weapon = player.getMainHandItem();
            ItemStack journal = BlazingJournalItem.guessJournal(player);
            if (journal == ItemStack.EMPTY || !(journal.getItem() instanceof BlazingJournalItem blazingJournalItem))
                return;
            blazingJournalQuillProcessor(blazingJournalItem, damageSource, journal, weapon, player, target);
            blazingJournalAttackProcessor(journal, weapon, player, target);
        }

        public static void blazingJournalAdvancementProcessor (Entity player, @Nullable Entity entity, @Nullable DamageSource source)
        {
            if (player == null) return;
            if (!(player instanceof ServerPlayer sPlayer)) return;
            if (source == null) return;
            if (source.is(DamageTypeDatagen.JOURNAL_PROJECTION))
            {
                GeneralUtils.addAdvancement(sPlayer, new ResourceLocation(PyromancerMod.MOD_ID, "pyromancer/journal_projection"));
            }
        }

        public static void blazingJournalQuillProcessor (BlazingJournalItem blazingJournalItem, DamageSource damageSource, ItemStack journal, ItemStack weapon, Player player, Entity target)
        {
            if ((blazingJournalItem.getItemFromItem(journal, 0)).getItem() instanceof QuillItem quillItem)
            {
                if (quillItem.isActivated(damageSource, player, target, weapon, journal) && !target.hurtMarked)
                    quillItem.getAttack(player, target, weapon, journal);
            }
        }

        public static void blazingJournalAttackProcessor (ItemStack journal, ItemStack weapon, Player player, Entity target)
        {
            for (BlazingJournalEnchantment blazingJournalEnchantment : journal.getAllEnchantments().keySet().stream().filter(enchantment -> enchantment instanceof BlazingJournalEnchantment).map(enchantment -> (BlazingJournalEnchantment) enchantment).toList())
            {
                if (!blazingJournalEnchantment.globalCondition(player))
                {
                    break;
                }
                if (!blazingJournalEnchantment.getWeaponClass().isInstance(weapon.getItem()) || !blazingJournalEnchantment.getCondition(player, target))
                {
                    continue;
                }
                blazingJournalEnchantment.getAttack(player, target);
                BlazingJournalItem.changeBlaze(player, -1);
            }
        }
    }
}
