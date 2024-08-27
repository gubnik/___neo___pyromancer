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
import xyz.nikgub.incandescent.common.item.IExtensibleTooltipItem;
import xyz.nikgub.incandescent.common.item.IGradientNameItem;
import xyz.nikgub.incandescent.common.item.INotStupidTooltipItem;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.mixin.client.ItemRendererMixin;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Class that should be extended whenever making pyromancy based on vanilla use system <p>
 * Not to be confused with {@link IPyromancyItem}, which is a general interface for any pyromancy
 */
public abstract class UsablePyromancyItem extends Item implements IPyromancyItem, INotStupidTooltipItem, IGradientNameItem, IExtensibleTooltipItem
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
        gatherTooltipLines(list, "pyromancer.pyromancy_hidden_line", "desc", PyromancerConfig.pyromancyDescriptionKey);
    }

    @Override
    public Map<Attribute, Pair<UUID, Style>> specialColoredUUID (ItemStack itemStack)
    {
        return Map.of(
                AttributeRegistry.PYROMANCY_DAMAGE.get(), Pair.of(BASE_PYROMANCY_DAMAGE_UUID, Style.EMPTY.applyFormat(ChatFormatting.GOLD)),
                AttributeRegistry.BLAZE_CONSUMPTION.get(), Pair.of(BASE_BLAZE_CONSUMPTION_UUID, Style.EMPTY.applyFormat(ChatFormatting.GOLD))
        );
    }

    @Override
    public BiFunction<Player, Attribute, Double> getAdditionalPlayerBonus (ItemStack itemStack)
    {
        return ((player, attribute) ->
        {
            double d0 = 0;
            d0 += IPyromancyItem.getAttributeBonus(player, attribute);
            return d0;
        });
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
