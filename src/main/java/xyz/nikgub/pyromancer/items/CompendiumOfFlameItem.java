package xyz.nikgub.pyromancer.items;

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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.incandescent.item.IGradientNameItem;
import xyz.nikgub.incandescent.item.INotStupidTooltipItem;
import xyz.nikgub.incandescent.util.GeneralUtils;
import xyz.nikgub.pyromancer.items.capabilities.CompendiumOfFlameCapability;
import xyz.nikgub.pyromancer.mixin.client.ItemRendererMixin;
import xyz.nikgub.pyromancer.registries.vanila.AttributeRegistry;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Item that inherits {@link BlazingJournalItem}'s full functionality and is capable of storing 5 {@link UsablePyromancyItem}s <p>
 * For details regarding storage, see {@link CompendiumOfFlameCapability}
 */
public class CompendiumOfFlameItem extends BlazingJournalItem implements INotStupidTooltipItem, IGradientNameItem {

    /**
     * String of int tag associated with an active slot <p>
     * Said int tag must be between 1 and 5
     */
    public static final String ACTIVE_SLOT_TAG = "___PYROMANCER_COMPENDIUM_ACTIVE_SLOT___";

    /**
     * String of boolean tag associated with rendering pyromancy stored within this item <p>
     * Used in {@link ItemRendererMixin}
     */
    public static final String PYROMANCY_CUSTOM_RENDER_TAG = "___PYROMANCER_PYROMANCY_CUSTOM_RENDER___";

    /**
     * String of boolean tag associated with marking whether this item is in offhand <p>
     * Used in {@link ItemRendererMixin} <p>
     * A nifty little workaround to move all the logic regarding player's chosen main arm to somewhere else
     */
    public static final String IS_OFFHAND = "___COMPENDIUM_IS_OFFHAND___";

    public CompendiumOfFlameItem(Properties properties) {
        super(properties);
    }

    private ItemStack getCurrentlyActiveItem(ItemStack itemStack)
    {
        return this.getItemFromItem(itemStack, itemStack.getOrCreateTag().getInt(ACTIVE_SLOT_TAG));
    }

    private void setCurrentlyActiveItem(ItemStack itemStack, ItemStack ITEM)
    {
        this.setItemInItem(itemStack, ITEM, itemStack.getOrCreateTag().getInt(ACTIVE_SLOT_TAG));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new CompendiumOfFlameCapability();
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int tick, boolean b)
    {
        CompoundTag tag = itemStack.getOrCreateTag();
        if(tag.getInt(ACTIVE_SLOT_TAG) == 0) tag.putInt(ACTIVE_SLOT_TAG, 1);
        tag.putBoolean(IS_OFFHAND, (entity instanceof LivingEntity livingEntity && livingEntity.getOffhandItem().equals(itemStack)));
        if(tag.getDouble("CustomModelData") != 0) return;
        for (int i = 0; i < CompendiumOfFlameCapability.MAX_ITEMS + 1; i++)
        {
            if(this.getItemFromItem(itemStack, i).getItem() instanceof UsablePyromancyItem) tag.putDouble("CustomModelData", 1);
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack inSlot, @NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player, @NotNull SlotAccess slotAccess)
    {
        if(held.getItem() instanceof  UsablePyromancyItem) return pyromancyBehaviour(inSlot, held, slotAccess);
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
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        ItemStack itemStack = player.getItemInHand(hand);
        ItemStack temp = this.getItemFromItem(itemStack, itemStack.getOrCreateTag().getInt(ACTIVE_SLOT_TAG));
        if(temp == null) return InteractionResultHolder.fail(itemStack);
        setCurrentlyActiveItem(itemStack, temp);
        if(!(getCurrentlyActiveItem(itemStack).getItem() instanceof UsablePyromancyItem usablePyromancyItem)) return InteractionResultHolder.fail(itemStack);
        return usablePyromancyItem.use(level, player, hand);
    }
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        if(getCurrentlyActiveItem(itemStack).getItem() instanceof UsablePyromancyItem)
            getCurrentlyActiveItem(itemStack).onUseTick(level, entity, tick);
    }
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity)
    {
        if(getCurrentlyActiveItem(itemStack).getItem() instanceof UsablePyromancyItem) getCurrentlyActiveItem(itemStack).getItem().finishUsingItem(itemStack, level, entity);
        return itemStack;
    }
    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity, int tick)
    {
        if(getCurrentlyActiveItem(itemStack).getItem() instanceof UsablePyromancyItem) getCurrentlyActiveItem(itemStack).getItem().releaseUsing(itemStack, level, entity, tick);
    }
    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack)
    {
        return UseAnim.BOW;
    }
    @Override
    public int getUseDuration(@NotNull ItemStack itemStack)
    {
        if(getCurrentlyActiveItem(itemStack).getItem() instanceof UsablePyromancyItem)
            return getCurrentlyActiveItem(itemStack).getUseDuration();
        else return 0;
    }

    /*
    Attribute part
     */
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot, ItemStack itemStack)
    {
        if(!(getCurrentlyActiveItem(itemStack).getItem() instanceof UsablePyromancyItem pyromancyItem) || itemStack.getOrCreateTag().getBoolean(IS_OFFHAND)) return new ImmutableMultimap.Builder<Attribute, AttributeModifier>().build();
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
    public Map<Attribute, Pair<UUID, ChatFormatting>> specialColoredUUID()
    {
        return Map.of(
                AttributeRegistry.PYROMANCY_DAMAGE.get(), Pair.of(IPyromancyItem.BASE_PYROMANCY_DAMAGE_UUID, ChatFormatting.GOLD),
                AttributeRegistry.BLAZE_CONSUMPTION.get(), Pair.of(IPyromancyItem.BASE_BLAZE_CONSUMPTION_UUID, ChatFormatting.GOLD)
        );
    }

    @Override
    public BiFunction<Player, Attribute, Double> getAdditionalPlayerBonus()
    {
        return ((player, attribute) ->
        {
            double d0 = 0;
            if (player.getOffhandItem().getItem() instanceof BlazingJournalItem)
            {
                d0 += IPyromancyItem.getAttributeBonus(player, attribute);
            }
            return d0;
        });
    }

    @Override
    public boolean getGradientCondition(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public Pair<Integer, Integer> getGradientColors()
    {
        return Pair.of(GeneralUtils.rgbToColorInteger(200, 100, 12), GeneralUtils.rgbToColorInteger(120, 30, 80));
    }

    @Override
    public int getGradientTickTime()
    {
        return 60;
    }
}
