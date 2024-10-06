package xyz.nikgub.pyromancer.common.item;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.incandescent.common.item.IExtensibleTooltipItem;
import xyz.nikgub.incandescent.common.item.IGradientNameItem;
import xyz.nikgub.incandescent.common.item.INotStupidTooltipItem;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;
import xyz.nikgub.pyromancer.registry.TierRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class FlammenklingeItem extends SwordItem implements IPyromancyItem, INotStupidTooltipItem, IGradientNameItem, IExtensibleTooltipItem
{
    public static final float DEFAULT_DAMAGE = 6F;

    public static final String ENEMIES_COUNTER_TAG = "___FLAMMENKLINGE_ENTITIES_LAUNCHED___";

    public FlammenklingeItem ()
    {
        super(TierRegistry.FLAMMENKLINGE, 0, -2.4f, new Properties().stacksTo(1));
    }

    @Override
    public void inventoryTick (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int tick, boolean b)
    {
        if (!(entity instanceof LivingEntity livingEntity)) return;
        CompoundTag tag = livingEntity.getMainHandItem().getOrCreateTag();
        if (livingEntity.getMainHandItem().getItem() != ItemRegistry.FLAMMENKLINGE.get())
        {
            tag.putInt(ENEMIES_COUNTER_TAG, 0);
            return;
        }
    }

    @Override
    public void onUseTick (@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        if (!(entity instanceof Player player)) return;
        final Vec3 srcVec = player.getDeltaMovement();
        final Vec3 nVec = srcVec.multiply(2.4, 0, 2.4).add(0, 0.8, 0);
        CompoundTag tag = player.getMainHandItem().getOrCreateTag();
        for (LivingEntity target : EntityUtils.entityCollector(player.getEyePosition(), 2, player.level()))
        {
            target.setDeltaMovement(nVec);
            if (target == player) continue;
            target.setRemainingFireTicks(target.getRemainingFireTicks() + 40);
            target.hurt(DamageSourceRegistry.flammenklingeLaunch(player), (float) player.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()));
            tag.putInt(ENEMIES_COUNTER_TAG, tag.getInt(ENEMIES_COUNTER_TAG) + 1);
        }
        BlazingJournalItem.changeBlaze(player, -(int) getDefaultBlazeCost());
        entity.stopUsingItem();
        if (!(player.level() instanceof ServerLevel serverLevel))
        {
            return;
        }
        final double X = player.getX();
        final double Y = player.getY();
        final double Z = player.getZ();
        for (int i = 0; i < 20; i++)
        {
            int tickCount = 20 - i;
            final double c = (double) tickCount / 20;
            final double R = 2 * c;
            final double sinK = R * Math.sin(Math.toRadians(tickCount * 18));
            final double cosK = R * Math.cos(Math.toRadians(tickCount * 18));
            serverLevel.sendParticles(ParticleTypes.FLAME, X + sinK, Y + tickCount * 0.1, Z + cosK, (int) (1 + 5 * c), 0.1, 0.1, 0.1, 0);
            serverLevel.sendParticles(ParticleTypes.FLAME, X - sinK, Y + tickCount * 0.1, Z - cosK, (int) (1 + 5 * c), 0.1, 0.1, 0.1, 0);
        }
    }

    @Override
    public void onStopUsing(ItemStack itemStack, LivingEntity entity, int count)
    {
        if (!(entity instanceof Player player)) return;
        player.getCooldowns().addCooldown(itemStack.getItem(), 30);
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
        if (BlazingJournalItem.getBlaze(player) > player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()))
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack pStack, @NotNull LivingEntity target, @NotNull LivingEntity attacker)
    {
        /*
        Implementation in a PlayerMixin, Forge is being a bitch
         */
        return super.hurtEnemy(pStack, target, attacker);
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

    @Override
    public float getDefaultPyromancyDamage ()
    {
        return DEFAULT_DAMAGE * 0.5f;
    }

    @Override
    public int getDefaultBlazeCost ()
    {
        return 8;
    }

    public static void attackProper (@NotNull Player self, @NotNull Entity target, CallbackInfo callbackInfo)
    {
        final Vec3 srcVec = self.getDeltaMovement();
        final Vec3 nVec = new Vec3(srcVec.x * 1.5, 1.2, srcVec.z * 1.5);
        CompoundTag tag = self.getMainHandItem().getOrCreateTag();
        for (LivingEntity entity : EntityUtils.entityCollector(self.position(), 4, self.level()))
        {
            if (entity == self) continue;
            entity.setDeltaMovement(nVec);
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 40);
            entity.hurt(DamageSourceRegistry.flammenklingeLaunch(self), (float) self.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()));
            tag.putInt(FlammenklingeItem.ENEMIES_COUNTER_TAG, tag.getInt(FlammenklingeItem.ENEMIES_COUNTER_TAG) + 1);
        }
        self.setDeltaMovement(nVec);
        if (!(self.level() instanceof ServerLevel level))
        {
            callbackInfo.cancel();
            return;
        }
        final double X = self.getX();
        final double Y = self.getY();
        final double Z = self.getZ();
        for (int i = 0; i < 20; i++)
        {
            int tickCount = 20 - i;
            final double c = (double) tickCount / 20;
            final double R = 4 * c;
            final double sinK = R * Math.sin(Math.toRadians(tickCount * 18));
            final double cosK = R * Math.cos(Math.toRadians(tickCount * 18));
            level.sendParticles(ParticleTypes.FLAME, X + sinK, Y + tickCount * 0.1, Z + cosK, (int) (1 + 5 * c), 0.1, 0.1, 0.1, 0);
            level.sendParticles(ParticleTypes.FLAME, X - sinK, Y + tickCount * 0.1, Z - cosK, (int) (1 + 5 * c), 0.1, 0.1, 0.1, 0);
        }
    }

}
