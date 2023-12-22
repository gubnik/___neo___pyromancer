package net.nikgub.pyromancer;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.DetectedVersion;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
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
import net.nikgub.pyromancer.data.RegistriesDataGeneration;
import net.nikgub.pyromancer.ember.Ember;
import net.nikgub.pyromancer.enchantments.BlazingJournalEnchantment;
import net.nikgub.pyromancer.entities.attack_effects.flaming_guillotine.FlamingGuillotineModel;
import net.nikgub.pyromancer.entities.attack_effects.flaming_guillotine.FlamingGuillotineRenderer;
import net.nikgub.pyromancer.events.EmberEvent;
import net.nikgub.pyromancer.items.EmberItem;
import net.nikgub.pyromancer.items.blazing_journal.BlazingJournalItem;
import net.nikgub.pyromancer.items.quills.QuillItem;
import net.nikgub.pyromancer.registries.custom.EmberRegistry;
import net.nikgub.pyromancer.registries.vanila.AttributeRegistry;
import net.nikgub.pyromancer.registries.vanila.EntityTypeRegistry;
import net.nikgub.pyromancer.registries.vanila.ItemRegistry;
import net.nikgub.pyromancer.registries.vanila.enchantments.EnchantmentRegistry;
import net.nikgub.pyromancer.util.ItemUtils;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PyromancerMod.MOD_ID)
public class PyromancerMod
{
    public static final String MOD_ID = "pyromancer";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final RegistryObject<CreativeModeTab> PYROMANCER_TAB = CREATIVE_MODE_TABS.register("pyromancer", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.pyromancer"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(Items.FIRE_CHARGE::getDefaultInstance)
            .build());

    public PyromancerMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupClient);
        modEventBus.addListener(this::registerLayerDefinitions);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::gatherData);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PyromancerConfig.SPEC);
        EmberRegistry.EMBERS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        EnchantmentRegistry.ENCHANTMENTS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
    private void setupClient(final FMLCommonSetupEvent event) {
        EntityRenderers.register(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), FlamingGuillotineRenderer::new);
    }
    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FlamingGuillotineModel.LAYER_LOCATION, FlamingGuillotineModel::createBodyLayer);
        //event.registerLayerDefinition(PyromancerArmorModel.LAYER_LOCATION, PyromancerArmorModel::createBodyLayer);
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTab().equals(PYROMANCER_TAB.get()))
        {
            event.accept(new ItemStack(ItemRegistry.BLAZING_JOURNAL.get()));
            event.accept(new ItemStack(ItemRegistry.EMBER_ITEM.get()));

            for(Ember ember : EmberRegistry.REGISTRY.get().getValues().stream().toList())
            {
                ItemStack emberStack = new ItemStack(ItemRegistry.EMBER_ITEM.get());
                ember.applyToItemStack(emberStack);
                event.accept(emberStack);
            }
        }
    }
    public void gatherData(GatherDataEvent event){
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
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event)
        {

        }
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents
    {
        @SubscribeEvent
        public static void tooltipColorEvent(RenderTooltipEvent.Color event)
        {
            ItemStack itemStack = event.getItemStack();
            Ember ember = EmberRegistry.getFromItem(itemStack);
            if(Ember.emberItemStackPredicate(itemStack) && ember != null)
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
    public static class ServerForgeEvents
    {
    }
    @Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents
    {
        @SubscribeEvent
        public static void entityAttributeProvider(EntityAttributeModificationEvent event){
            event.add(EntityType.PLAYER, AttributeRegistry.BLAZE_CONSUMPTION.get());
            event.add(EntityType.PLAYER, AttributeRegistry.PYROMANCY_DAMAGE.get());
        }
    }
    @Mod.EventBusSubscriber(modid = PyromancerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents
    {
        @SubscribeEvent
        public static void emberEvent(EmberEvent event)
        {
        }
        @SubscribeEvent
        public static void playerAttackEvent(AttackEntityEvent event) {
            Entity target = event.getTarget();
            Player player = event.getEntity();
            ItemStack weapon = player.getMainHandItem();
            ItemStack journal = ItemUtils.guessJournal(player);
            if (journal == ItemStack.EMPTY || !(journal.getItem() instanceof BlazingJournalItem blazingJournalItem)) return;
            if ((blazingJournalItem.getItemFromItem(journal, 0)).getItem() instanceof QuillItem quillItem) {
                if (quillItem.getCondition().apply(player, weapon, journal)
                        && !target.hurtMarked) {
                    quillItem.getAttack().accept(player, weapon, journal);
                }
            }
            for(Enchantment enchantment : journal.getAllEnchantments().keySet()){
                if(enchantment instanceof BlazingJournalEnchantment blazingJournalEnchantment && blazingJournalEnchantment.getWeaponClass().isInstance(weapon.getItem())){
                    if(ItemUtils.getBlaze(player) > 0 && player.getAttackStrengthScale(0) > 0.7)
                    {
                        blazingJournalEnchantment.getAttack().accept(player, target);
                        ItemUtils.changeBlaze(player, -1);
                    } else break;
                }
            }
        }
    }
}
