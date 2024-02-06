package xyz.nikgub.pyromancer.items;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.nikgub.incandescent.item.IContainerItem;
import xyz.nikgub.incandescent.util.GeneralUtils;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.enchantments.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.events.BlazingJournalAttackEvent;
import xyz.nikgub.pyromancer.items.capabilities.BlazingJournalCapability;
import xyz.nikgub.pyromancer.registries.vanila.AttributeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * <h2>Core mod item</h2>
 * Generally can be looked on as a glorified mana bar
 * Modified by {@link QuillItem}, consult said class for more info
 */
public class BlazingJournalItem extends Item implements IContainerItem {
    public final static String BLAZE_TAG_NAME = "PYROMANCER_BLAZING_JOURNAL_TAG";
    public BlazingJournalItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack)
    {
        Style style = Component.translatable(this.getDescriptionId(itemStack)).getStyle();
        style = style.withColor(GeneralUtils.rgbToColorInteger(255, 132, 16)).withBold(true);
        return Component.translatable(this.getDescriptionId(itemStack)).withStyle(style);
    }
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot, ItemStack itemStack) {
        if(!(this.getItemFromItem(itemStack, 0).getItem() instanceof QuillItem quillItem)) return super.getAttributeModifiers(slot, itemStack);
        if(slot == EquipmentSlot.OFFHAND)
        {
            ImmutableListMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();
            Pair<Integer, Float> pair = quillItem.getPyromancyModifiers();
            if(pair.getFirst() != 0)
                builder.put(AttributeRegistry.BLAZE_CONSUMPTION.get(), new AttributeModifier(IPyromancyItem.JOURNAL_BLAZE_CONSUMPTION_UUID, "Weapon modifier", pair.getFirst(), AttributeModifier.Operation.ADDITION));
            if(pair.getSecond() != 0)
                builder.put(AttributeRegistry.PYROMANCY_DAMAGE.get(), new AttributeModifier(IPyromancyItem.JOURNAL_PYROMANCY_DAMAGE_UUID, "Weapon modifier", pair.getSecond(), AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        else return super.getAttributeModifiers(slot, itemStack);
    }
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new BlazingJournalCapability();
    }
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        String blazeLine = Component.translatable("blazing_journal.blaze_value.desc").getString() + itemStack.getOrCreateTag().getInt(BLAZE_TAG_NAME);
        list.add(Component.literal(blazeLine + " / " + PyromancerConfig.blazingJournalMaxCapacity).withStyle(ChatFormatting.GOLD));
    }
    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack inSlot, @NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player, @NotNull SlotAccess slotAccess)
    {
        if(!(clickAction == ClickAction.SECONDARY)) return false;
        if(held.getItem() instanceof  QuillItem) return this.quillBehaviour(inSlot, held, slotAccess);
        if(held.getItem() == Items.BLAZE_POWDER) return this.blazeBehaviour(inSlot, slotAccess);
        return false;
    }
    public boolean quillBehaviour(ItemStack inSlot, ItemStack held, SlotAccess slotAccess)
    {
        if(this.getItemFromItem(inSlot, 0).getItem() instanceof QuillItem)
        {
            this.getItemFromItem(inSlot, 0).getOrCreateTag().putBoolean(QuillItem.QUILL_RENDER_TAG, false);
            slotAccess.set(this.getItemFromItem(inSlot, 0));
            this.setItemInItem(inSlot, held, 0);
        }
        else {
            this.setItemInItem(inSlot, held, 0);
            slotAccess.set(ItemStack.EMPTY);
        }
        return true;
    }
    public boolean blazeBehaviour(ItemStack inSlot, SlotAccess slotAccess)
    {
        int maxCapacity = PyromancerConfig.blazingJournalMaxCapacity,
                value = PyromancerConfig.blazeValue;
        CompoundTag tag = inSlot.getOrCreateTag();
        if(tag.getInt(BLAZE_TAG_NAME) <= maxCapacity - value)
        {
            while(tag.getInt(BLAZE_TAG_NAME) <= maxCapacity - value)
            {
                slotAccess.get().shrink(1);
                tag.putInt(BLAZE_TAG_NAME, tag.getInt(BLAZE_TAG_NAME) + value);
            }
        }
        else if(tag.getInt(BLAZE_TAG_NAME) < maxCapacity)
        {
            slotAccess.get().shrink(1);
            tag.putInt(BLAZE_TAG_NAME, maxCapacity);
        }
        return true;
    }
    @NotNull
    public static BlazingJournalAttackEvent getBlazingJournalAttackEvent(Player player, Entity target, ItemStack journal, ItemStack weapon, BlazingJournalEnchantment enchantment)
    {
        BlazingJournalAttackEvent event = new BlazingJournalAttackEvent(player, target, journal, weapon, enchantment);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }
}
