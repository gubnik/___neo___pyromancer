package xyz.nikgub.pyromancer;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.DetectedVersion;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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
import xyz.nikgub.pyromancer.client.models.armor.PyromancerArmorModel;
import xyz.nikgub.pyromancer.client.models.entities.FlamingGuillotineModel;
import xyz.nikgub.pyromancer.client.models.entities.PyronadoModel;
import xyz.nikgub.pyromancer.client.models.entities.UnburnedModel;
import xyz.nikgub.pyromancer.client.renderers.FlamingGuillotineRenderer;
import xyz.nikgub.pyromancer.client.renderers.PyronadoRenderer;
import xyz.nikgub.pyromancer.client.renderers.UnburnedRenderer;
import xyz.nikgub.pyromancer.data.DamageTypeDatagen;
import xyz.nikgub.pyromancer.data.RegistriesDataGeneration;
import xyz.nikgub.pyromancer.ember.Ember;
import xyz.nikgub.pyromancer.enchantments.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.entities.unburned.UnburnedEntity;
import xyz.nikgub.pyromancer.events.BlazingJournalAttackEvent;
import xyz.nikgub.pyromancer.items.*;
import xyz.nikgub.pyromancer.network.NetworkCore;
import xyz.nikgub.pyromancer.registries.custom.EmberRegistry;
import xyz.nikgub.pyromancer.registries.vanila.*;
import xyz.nikgub.pyromancer.registries.vanila.enchantments.EnchantmentRegistry;
import xyz.nikgub.pyromancer.util.GeneralUtils;
import xyz.nikgub.pyromancer.util.ItemUtils;

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
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupClient);
        modEventBus.addListener(this::registerLayerDefinitions);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::entityAttributeSupplier);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PyromancerConfig.SPEC);
        EmberRegistry.EMBERS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        EnchantmentRegistry.ENCHANTMENTS.register(modEventBus);
        MobEffectRegistry.MOB_EFFECTS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        NetworkCore.register();
    }

    private void setupClient(final FMLCommonSetupEvent event) {
        EntityRenderers.register(EntityTypeRegistry.BOMBSACK.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.SIZZLING_HAND_FIREBALL.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), FlamingGuillotineRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.PYRONADO.get(), PyronadoRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.UNBURNED.get(), UnburnedRenderer::new);
    }
    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PyromancerArmorModel.LAYER_LOCATION, PyromancerArmorModel::createBodyLayer);
        event.registerLayerDefinition(FlamingGuillotineModel.LAYER_LOCATION, FlamingGuillotineModel::createBodyLayer);
        event.registerLayerDefinition(PyronadoModel.LAYER_LOCATION, PyronadoModel::createBodyLayer);
        event.registerLayerDefinition(UnburnedModel.LAYER_LOCATION, UnburnedModel::createBodyLayer);
    }
    private void entityAttributeSupplier(EntityAttributeCreationEvent event)
    {
        event.put(EntityTypeRegistry.UNBURNED.get(), UnburnedEntity.setAttributes());
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTab().equals(PYROMANCER_TAB.get()))
        {
            List<Item> MACES = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof MaceItem).map(RegistryObject::get).toList();
            List<Item> USABLE_PYROMANCY = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof UsablePyromancyItem).map(RegistryObject::get).toList();
            List<Item> PYROMANCY = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof IPyromancyItem && !(registryObject.get() instanceof UsablePyromancyItem)).map(RegistryObject::get).toList();
            List<Item> QUILLS = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof QuillItem).map(RegistryObject::get).toList();
            List<ItemStack> EMBERS = EmberRegistry.REGISTRY.get().getValues().stream().map(ember -> ember.applyToItemStack(new ItemStack(ItemRegistry.EMBER_ITEM.get()))).toList();
            List<Item> ARMOR = ItemRegistry.ITEMS.getEntries().stream().filter(registryObject -> registryObject.get() instanceof ArmorItem).map(RegistryObject::get).toList();

            event.accept(new ItemStack(ItemRegistry.BLAZING_JOURNAL.get()));
            event.accept(new ItemStack(ItemRegistry.COMPENDIUM_OF_FLAME.get()));
            for(Item item : MACES) event.accept(item);
            for(Item item : USABLE_PYROMANCY) event.accept(item);
            for(Item item : PYROMANCY) event.accept(item);
            for(Item item : QUILLS) event.accept(item);
            event.accept(new ItemStack(ItemRegistry.EMBER_ITEM.get()));
            for(ItemStack item : EMBERS) event.accept(item);
            for(Item item : ARMOR) event.accept(item);
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
            Ember ember = EmberRegistry.getFromItem(itemStack);
            if((Ember.emberItemStackPredicate(itemStack) || itemStack.getItem() instanceof EmberItem)&& ember != null)
            {
                event.setBackgroundStart(ember.getType().getColor());
            }
        }
        @SubscribeEvent
        public static void tooltipContentsEvent(ItemTooltipEvent event)
        {
            ItemStack itemStack = event.getItemStack();
            Ember ember = EmberRegistry.getFromItem(itemStack);
            if(ember == null) return;
            if(Ember.emberItemStackPredicate(itemStack) || itemStack.getItem() instanceof EmberItem
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
    @Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    @SuppressWarnings("unused")
    public static class ModEvents
    {
        @SubscribeEvent
        public static void entityAttributeProvider(EntityAttributeModificationEvent event){
            event.add(EntityType.PLAYER, AttributeRegistry.BLAZE_CONSUMPTION.get());
            event.add(EntityType.PLAYER, AttributeRegistry.PYROMANCY_DAMAGE.get());
        }
    }
    @Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    @SuppressWarnings("unused")
    public static class ForgeEvents {
        @SubscribeEvent
        public static void playerAttackEvent(LivingAttackEvent event) {
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
            if ((blazingJournalItem.getItemFromItem(journal, 0)).getItem() instanceof QuillItem quillItem) {
                if (quillItem.getCondition().apply(player, weapon, journal)
                        && !target.hurtMarked) {
                    quillItem.getAttack().accept(player, weapon, journal);
                }
            }
        }
        public static void blazingJournalAttackProcessor(ItemStack journal, ItemStack weapon, Player player, Entity target)
        {
            for (BlazingJournalEnchantment blazingJournalEnchantment : journal.getAllEnchantments().keySet().stream().filter(enchantment -> enchantment instanceof BlazingJournalEnchantment).map(enchantment -> (BlazingJournalEnchantment) enchantment).toList())
            {
                if (blazingJournalEnchantment.defaultCondition(player)) {
                    if (blazingJournalEnchantment.getWeaponClass().isInstance(weapon.getItem())
                            && blazingJournalEnchantment.getCondition().apply(player, target)) {
                        BlazingJournalAttackEvent blazingJournalAttackEvent = BlazingJournalItem.getBlazingJournalAttackEvent(player, target, journal, weapon, blazingJournalEnchantment);
                        blazingJournalEnchantment.getAttack().accept(player, target);
                        ItemUtils.changeBlaze(player, -1);
                    }
                } else break;
            }
        }
    }
}
