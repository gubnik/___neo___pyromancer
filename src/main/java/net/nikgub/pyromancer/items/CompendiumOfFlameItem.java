package net.nikgub.pyromancer.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.ember.UniqueEmberBehaviour;
import net.nikgub.pyromancer.items.capabilities.CompendiumOfFlameCapability;
import net.nikgub.pyromancer.registries.vanila.AttributeRegistry;
import net.nikgub.pyromancer.util.GeneralUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Item that inherits {@link BlazingJournalItem}'s full functionality and is capable of storing 5 {@link UsablePyromancyItem}s
 * For details regarding storage, see {@link CompendiumOfFlameCapability}
 *
 *
 */
@UniqueEmberBehaviour(allow = UniqueEmberBehaviour.AllowanceModifier.DENY)
public class CompendiumOfFlameItem extends BlazingJournalItem implements INotStupidTooltipItem, IGradientNameItem {
    /**
     * String of int tag associated with an active slot
     * Said int tag must be between 1 and 5
     */
    public static final String ACTIVE_SLOT_TAG = "___PYROMANCER_COMPENDIUM_ACTIVE_SLOT___";
    /**
     * String of boolean tag associated with rendering pyromancy stored within this item
     * Used in {@link net.nikgub.pyromancer.mixin.client.ItemRendererMixin}
     */
    public static final String PYROMANCY_CUSTOM_RENDER_TAG = "___PYROMANCER_PYROMANCY_CUSTOM_RENDER___";
    /**
     * String of boolean tag associated with marking whether this item is in offhand
     * Used in {@link net.nikgub.pyromancer.mixin.client.ItemRendererMixin}
     * A nifty little workaround to move all the logic regarding player's chosen main arm to somewhere else
     */
    public static final String IS_OFFHAND = "___COMPENDIUM_IS_OFFHAND___";
    private ItemStack currentlyActiveItem = ItemStack.EMPTY;
    public CompendiumOfFlameItem(Properties properties) {
        super(properties);
    }
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new CompendiumOfFlameCapability();
    }
    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int tick, boolean m)
    {
        CompoundTag tag = itemStack.getOrCreateTag();
        if(tag.getInt(ACTIVE_SLOT_TAG) == 0) tag.putInt(ACTIVE_SLOT_TAG, 1);
        if(tag.getDouble("CustomModelData") != 0) return;
        for (int i = 0; i < CompendiumOfFlameCapability.MAX_ITEMS + 1; i++)
        {
            PyromancerMod.LOGGER.info("Item in slot " + i + " is " + this.getItemFromItem(itemStack, i));
            if(this.getItemFromItem(itemStack, i).getItem() instanceof UsablePyromancyItem) tag.putDouble("CustomModelData", 1);
        }
        tag.putBoolean(IS_OFFHAND, (entity instanceof LivingEntity livingEntity && livingEntity.getOffhandItem().equals(itemStack)));
    }
    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack inSlot, @NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player, @NotNull SlotAccess slotAccess)
    {
        if(!super.overrideOtherStackedOnMe(inSlot, held, slot, clickAction, player, slotAccess))
        {
            if(held.getItem() instanceof  UsablePyromancyItem) return pyromancyBehaviour(inSlot, held, slotAccess);
        }
        return super.overrideOtherStackedOnMe(inSlot, held, slot, clickAction, player, slotAccess);
    }
    public boolean pyromancyBehaviour(ItemStack inSlot, ItemStack held, SlotAccess slotAccess)
    {
        for(int i = 1; i < CompendiumOfFlameCapability.MAX_ITEMS + 1; i++)
        {
            if(!(this.getItemFromItem(inSlot, i).getItem() instanceof UsablePyromancyItem))
            {
                this.setItemInItem(inSlot, held, i);
                slotAccess.set(ItemStack.EMPTY);
                break;
            }
        }
        return true;
    }
    /*
    Use item part
     */
    @Override
    @ApiStatus.Internal
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        if(hand == InteractionHand.OFF_HAND)
        {
            this.currentlyActiveItem = ItemStack.EMPTY;
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        ItemStack itemStack = player.getItemInHand(hand);
        ItemStack temp = this.getItemFromItem(itemStack, itemStack.getOrCreateTag().getInt(ACTIVE_SLOT_TAG));
        if(temp == null) return InteractionResultHolder.fail(itemStack);
        this.currentlyActiveItem = temp;
        if(!(this.currentlyActiveItem.getItem() instanceof UsablePyromancyItem usablePyromancyItem)) return InteractionResultHolder.fail(itemStack);
        return usablePyromancyItem.use(level, player, hand);
    }
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        if(this.currentlyActiveItem.getItem() instanceof UsablePyromancyItem)
            this.currentlyActiveItem.onUseTick(level, entity, tick);
    }
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity)
    {
        if(this.currentlyActiveItem.getItem() instanceof UsablePyromancyItem) this.currentlyActiveItem.getItem().finishUsingItem(itemStack, level, entity);
        return itemStack;
    }
    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity, int tick)
    {
        if(this.currentlyActiveItem.getItem() instanceof UsablePyromancyItem) this.currentlyActiveItem.getItem().releaseUsing(itemStack, level, entity, tick);
    }
    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack)
    {
        return UseAnim.BOW;
    }
    @Override
    public int getUseDuration(@NotNull ItemStack itemStack)
    {
        if(this.currentlyActiveItem.getItem() instanceof UsablePyromancyItem)
            return this.currentlyActiveItem.getUseDuration();
        else return 0;
    }
    /*
    Attribute part
     */
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot, ItemStack itemStack) {
        if(!(this.currentlyActiveItem.getItem() instanceof UsablePyromancyItem pyromancyItem)) return new ImmutableMultimap.Builder<Attribute, AttributeModifier>().build();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        if(slot == EquipmentSlot.MAINHAND)
        {
            builder.put(AttributeRegistry.PYROMANCY_DAMAGE.get(), new AttributeModifier(IPyromancyItem.BASE_PYROMANCY_DAMAGE_UUID, "Weapon modifier", pyromancyItem.getPyromancyModifiers().getSecond(), AttributeModifier.Operation.ADDITION));
            builder.put(AttributeRegistry.BLAZE_CONSUMPTION.get(), new AttributeModifier(IPyromancyItem.BASE_BLAZE_CONSUMPTION_UUID, "Weapon modifier", pyromancyItem.getPyromancyModifiers().getFirst(), AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return builder.build();
    }
    @Override
    public Map<Attribute, Pair<UUID, ChatFormatting>> specialColoredUUID() {
        return Map.of(
                AttributeRegistry.PYROMANCY_DAMAGE.get(), Pair.of(IPyromancyItem.BASE_PYROMANCY_DAMAGE_UUID, ChatFormatting.GOLD),
                AttributeRegistry.BLAZE_CONSUMPTION.get(), Pair.of(IPyromancyItem.BASE_BLAZE_CONSUMPTION_UUID, ChatFormatting.GOLD)
        );
    }
    @Override
    public BiFunction<Player, Attribute, Double> getAdditionalPlayerBonus() {
        return ((player, attribute) -> {
            double d0 = 0;
            if (player.getOffhandItem().getItem() instanceof BlazingJournalItem) {
                d0 += IPyromancyItem.getAttributeBonus(player, attribute);
            }
            return d0;
        });
    }

    @Override
    public boolean getGradientCondition(ItemStack itemStack) {
        return true;
    }

    @Override
    public Pair<Integer, Integer> getGradientColors() {
        return Pair.of(GeneralUtils.rgbToColorInteger(100, 50, 0), GeneralUtils.rgbToColorInteger(0, 50, 100));
    }

    @Override
    public int getGradientTickTime() {
        return 20;
    }
}
