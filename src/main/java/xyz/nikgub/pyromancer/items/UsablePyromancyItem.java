package xyz.nikgub.pyromancer.items;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import xyz.nikgub.incandescent.item.IGradientNameItem;
import xyz.nikgub.incandescent.item.INotStupidTooltipItem;
import xyz.nikgub.incandescent.util.GeneralUtils;
import xyz.nikgub.pyromancer.registries.vanila.AttributeRegistry;
import xyz.nikgub.pyromancer.util.ItemUtils;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.mixin.client.ItemRendererMixin;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Class that should be extended whenever making pyromancy based on vanilla use system <p>
 * Not to be confused with {@link IPyromancyItem}, which is a general interface for any pyromancy
 */
public class UsablePyromancyItem extends Item implements IPyromancyItem, INotStupidTooltipItem, IGradientNameItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public UsablePyromancyItem(Properties properties) {
        super(properties.stacksTo(1));
        ImmutableListMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();
        builder.put(AttributeRegistry.PYROMANCY_DAMAGE.get(), new AttributeModifier(BASE_PYROMANCY_DAMAGE_UUID, "Weapon modifier", this.getPyromancyModifiers().getSecond(), AttributeModifier.Operation.ADDITION));
        builder.put(AttributeRegistry.BLAZE_CONSUMPTION.get(), new AttributeModifier(BASE_BLAZE_CONSUMPTION_UUID, "Weapon modifier", this.getPyromancyModifiers().getFirst(), AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    /**
     * Method to provide an optional additional logic for {@link ItemRendererMixin} pyromancyRenderManager() method
     * @param poseStack     PoseStack to transform
     */
    public void compendiumTransforms(PoseStack poseStack, ItemDisplayContext displayContext)
    {
        poseStack.scale(1f,1f,1f);
    }
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
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
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
    }
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity)
    {
        return itemStack;
    }
    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack)
    {
        return UseAnim.CUSTOM;
    }
    @Override
    public int getUseDuration(@NotNull ItemStack itemStack)
    {
        return 72000;
    }
    @Override
    public Pair<Integer, Float> getPyromancyModifiers() {
        return Pair.of(0, 0f);
    }
    @Override
    public Map<Attribute, Pair<UUID, ChatFormatting>> specialColoredUUID() {
        return Map.of(
                AttributeRegistry.PYROMANCY_DAMAGE.get(), Pair.of(BASE_PYROMANCY_DAMAGE_UUID, ChatFormatting.GOLD),
                AttributeRegistry.BLAZE_CONSUMPTION.get(), Pair.of(BASE_BLAZE_CONSUMPTION_UUID, ChatFormatting.GOLD)
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
        return Pair.of(
                GeneralUtils.rgbToColorInteger(200, 57, 0),
                GeneralUtils.rgbToColorInteger(240, 129, 0)
        );
    }

    @Override
    public int getGradientTickTime() {
        return 60;
    }
}
