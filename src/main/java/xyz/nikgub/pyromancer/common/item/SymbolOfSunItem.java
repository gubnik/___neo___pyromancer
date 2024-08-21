package xyz.nikgub.pyromancer.common.item;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.item.IExtensibleTooltipItem;
import xyz.nikgub.incandescent.common.item.IGradientNameItem;
import xyz.nikgub.incandescent.common.item.INotStupidTooltipItem;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registries.AttributeRegistry;
import xyz.nikgub.pyromancer.registries.MobEffectRegistry;
import xyz.nikgub.pyromancer.registries.TierRegistry;
import xyz.nikgub.pyromancer.common.util.ItemUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class SymbolOfSunItem extends MaceItem implements IPyromancyItem, INotStupidTooltipItem, IGradientNameItem, IExtensibleTooltipItem
{
    public static final float DEFAULT_DAMAGE = 10F;

    public SymbolOfSunItem(Properties properties) {
        super(TierRegistry.SYMBOL_OF_SUN, properties.stacksTo(1));
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        if (!(entity instanceof Player player)) return;
        Vec3 movementVector = player.getDeltaMovement();
        Vec3 directionVector;
        PyromancerMod.LOGGER.info("{}", movementVector.length());
        double multCoeff = 1.5D;
        if (movementVector.multiply(1, 0, 1).length() <= 0.125D)
        {
            multCoeff = 2.5D;
            directionVector = player.getLookAngle().multiply(1, 0, 1).normalize();
        }
        else if (player.isSwimming())
        {
            multCoeff = 0.75D;
            directionVector = player.getLookAngle();
        }
        else directionVector  = movementVector.multiply(1, 0, 1).normalize();
        player.setDeltaMovement(movementVector.x + multCoeff * directionVector.x, movementVector.y + multCoeff * directionVector.y, movementVector.z + multCoeff * directionVector.z);
        player.addEffect(new MobEffectInstance(MobEffectRegistry.SOLAR_COLLISION.get(), 10, 0, false, true));
        ItemUtils.changeBlaze(player, -(int) getDefaultBlazeCost());
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity, int tick)
    {
        if(!(entity instanceof Player player)) return;
        player.getCooldowns().addCooldown(itemStack.getItem(), 20);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity)
    {
        this.releaseUsing(itemStack, level, entity, getUseDuration(itemStack));
        return itemStack;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack)
    {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack)
    {
        return 1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        if(ItemUtils.getBlaze(player) > player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()))
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers (@NotNull EquipmentSlot slot, ItemStack stack) {
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
    public void appendHoverText(@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        gatherTooltipLines(list, "pyromancer.pyromancy_hidden_line", "desc", PyromancerConfig.pyromancyDescriptionKey);
    }

    @Override
    public Map<Attribute, Pair<UUID, Style>> specialColoredUUID(ItemStack itemStack) {
        return Map.of(
                AttributeRegistry.PYROMANCY_DAMAGE.get(), Pair.of(BASE_PYROMANCY_DAMAGE_UUID, Style.EMPTY.applyFormat(ChatFormatting.GOLD)),
                AttributeRegistry.BLAZE_CONSUMPTION.get(), Pair.of(BASE_BLAZE_CONSUMPTION_UUID, Style.EMPTY.applyFormat(ChatFormatting.GOLD))
        );
    }

    @Override
    public BiFunction<Player, Attribute, Double> getAdditionalPlayerBonus(ItemStack itemStack) {
        return ((player, attribute) -> {
            double d0 = 0;
            d0 += IPyromancyItem.getAttributeBonus(player, attribute);
            return d0;
        });
    }

    @Override
    public boolean getGradientCondition(ItemStack itemStack) {
        return true;
    }

    @Override
    public Pair<Integer, Integer> getGradientColors(ItemStack itemStack) {
        return Pair.of(
                GeneralUtils.rgbToColorInteger(200, 57, 0),
                GeneralUtils.rgbToColorInteger(240, 129, 0)
        );
    }

    @Override
    public int getGradientTickTime(ItemStack itemStack) {
        return 60;
    }

    @Override
    public float getDefaultPyromancyDamage() {
        return DEFAULT_DAMAGE * 0.5f;
    }

    @Override
    public int getDefaultBlazeCost() {
        return 4;
    }
}
