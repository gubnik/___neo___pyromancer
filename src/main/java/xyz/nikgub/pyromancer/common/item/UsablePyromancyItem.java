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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.item_interfaces.IBetterAttributeTooltipItem;
import xyz.nikgub.incandescent.common.item_interfaces.IExtensibleTooltipItem;
import xyz.nikgub.incandescent.common.item_interfaces.IGradientNameItem;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.incandescent.util.Hypermap;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.mixin.client.ItemRendererMixin;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;

import java.util.List;
import java.util.UUID;

/**
 * Class that should be extended whenever making pyromancy based on vanilla use system <p>
 * Not to be confused with {@link IPyromancyItem}, which is a general interface for any pyromancy
 */
public abstract class UsablePyromancyItem extends Item implements IPyromancyItem, IBetterAttributeTooltipItem, IGradientNameItem, IExtensibleTooltipItem
{
    public UsablePyromancyItem (Properties properties)
    {
        super(properties.stacksTo(1));
    }

    /**
     * Method to provide an optional additional logic for {@link ItemRendererMixin} pyromancyRenderManager() method
     *
     * @param poseStack PoseStack to transform
     */
    public abstract void compendiumTransforms (PoseStack poseStack, ItemDisplayContext displayContext);

    @Override
    public abstract @NotNull ItemStack finishUsingItem (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity);

    @Override
    public abstract @NotNull UseAnim getUseAnimation (@NotNull ItemStack itemStack);

    @Override
    public abstract int getUseDuration (@NotNull ItemStack itemStack);

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use (@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        if (BlazingJournalItem.getBlaze(player) > player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()))
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
    public Hypermap<Attribute, UUID, Style> getDefaultAttributesStyles (ItemStack itemStack)
    {
        return Hypermap.of(
            AttributeRegistry.PYROMANCY_DAMAGE.get(), BASE_PYROMANCY_DAMAGE_UUID, Style.EMPTY.applyFormat(ChatFormatting.GOLD),
            AttributeRegistry.BLAZE_CONSUMPTION.get(), BASE_BLAZE_CONSUMPTION_UUID, Style.EMPTY.applyFormat(ChatFormatting.GOLD)
        );
    }

    @Override
    public double getAdditionalPlayerBonus (final ItemStack itemStack, final Player player, final Attribute attribute)
    {
        return this.getAttributeBonus(player, attribute);
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
}
