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
package xyz.nikgub.pyromancer.common.item;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.autogen_network.IncandescentNetworkAPI;
import xyz.nikgub.incandescent.common.item_interfaces.IBetterAttributeTooltipItem;
import xyz.nikgub.incandescent.common.item_interfaces.IExtensibleTooltipItem;
import xyz.nikgub.incandescent.common.item_interfaces.IGradientNameItem;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.incandescent.util.Hypermap;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.network.s2c.SymbolOfSunMovementPacket;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;
import xyz.nikgub.pyromancer.registry.MobEffectRegistry;
import xyz.nikgub.pyromancer.registry.TierRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SymbolOfSunItem extends MaceItem implements IPyromancyItem, IBetterAttributeTooltipItem, IExtensibleTooltipItem, IGradientNameItem
{
    public static final float DEFAULT_DAMAGE = 8F;

    public SymbolOfSunItem (Properties properties)
    {
        super(TierRegistry.SYMBOL_OF_SUN, properties.stacksTo(1));
    }

    @Override
    public void onUseTick (@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        if (!(entity instanceof ServerPlayer player)) return;
        IncandescentNetworkAPI.sendPacket(SymbolOfSunMovementPacket.create(player.getId()));
        player.addEffect(new MobEffectInstance(MobEffectRegistry.SOLAR_COLLISION.get(), 15, 0, false, true));
        BlazingJournalItem.changeBlaze(player, -(int) player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()));
        entity.stopUsingItem();
    }

    @Override
    public void onStopUsing (ItemStack itemStack, LivingEntity entity, int count)
    {
        if (!(entity instanceof Player player)) return;
        if (count > 0) player.getCooldowns().addCooldown(itemStack.getItem(), 30);
    }

    @Override
    public @NotNull UseAnim getUseAnimation (@NotNull ItemStack itemStack)
    {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration (@NotNull ItemStack itemStack)
    {
        return 1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use (@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        if (BlazingJournalItem.getBlaze(player) >= player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()))
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers (@NotNull EquipmentSlot slot, ItemStack stack)
    {
        ImmutableListMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();
        if (slot != EquipmentSlot.MAINHAND) return builder.build();
        Multimap<Attribute, AttributeModifier> parMap = super.getAttributeModifiers(slot, stack);
        for (Map.Entry<Attribute, Collection<AttributeModifier>> entry : parMap.asMap().entrySet())
            for (AttributeModifier modifier : entry.getValue())
                builder.put(entry.getKey(), modifier);
        builder.put(AttributeRegistry.PYROMANCY_DAMAGE.get(), new AttributeModifier(BASE_PYROMANCY_DAMAGE_UUID, "Weapon modifier", this.getDefaultPyromancyDamage(), AttributeModifier.Operation.ADDITION));
        builder.put(AttributeRegistry.BLAZE_CONSUMPTION.get(), new AttributeModifier(BASE_BLAZE_CONSUMPTION_UUID, "Weapon modifier", this.getDefaultBlazeCost(), AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void appendHoverText (@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        this.gatherTooltipLines(list, "pyromancer.hidden_desc", "desc", PyromancerConfig.descTooltipKey);
        this.gatherTooltipLines(list, "pyromancer.hidden_lore", "lore", PyromancerConfig.loreTooltipKey);
    }

    @Override
    public boolean getGradientCondition (ItemStack itemStack)
    {
        return true;
    }

    @Override
    public Pair<Integer, Integer> getGradientColors (ItemStack itemStack)
    {
        return Pair.of(
            GeneralUtils.rgbToColorInteger(200, 57, 0),
            GeneralUtils.rgbToColorInteger(240, 129, 0)
        );
    }

    @Override
    public int getGradientTickTime (ItemStack itemStack)
    {
        return 60;
    }

    @Override
    public float getDefaultPyromancyDamage ()
    {
        return DEFAULT_DAMAGE / 2 + 1;
    }

    @Override
    public int getDefaultBlazeCost ()
    {
        return 8;
    }

    @Override
    public Hypermap<Attribute, UUID, Style> getDefaultAttributesStyles (ItemStack itemStack)
    {
        return Hypermap.of(
            Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_UUID, this.defaultStyle(itemStack),
            Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_UUID, this.defaultStyle(itemStack),
            AttributeRegistry.PYROMANCY_DAMAGE.get(), BASE_PYROMANCY_DAMAGE_UUID, Style.EMPTY.applyFormat(ChatFormatting.GOLD),
            AttributeRegistry.BLAZE_CONSUMPTION.get(), BASE_BLAZE_CONSUMPTION_UUID, Style.EMPTY.applyFormat(ChatFormatting.GOLD)
        );
    }

    @Override
    public double getAdditionalPlayerBonus (ItemStack itemStack, Player player, Attribute attribute)
    {
        return 0;
    }
}
