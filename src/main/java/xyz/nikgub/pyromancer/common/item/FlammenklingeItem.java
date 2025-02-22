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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import xyz.nikgub.incandescent.common.item_interfaces.IBetterAttributeTooltipItem;
import xyz.nikgub.incandescent.common.item_interfaces.IExtensibleTooltipItem;
import xyz.nikgub.incandescent.common.item_interfaces.IGradientNameItem;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.incandescent.common.util.GeneralUtils;
import xyz.nikgub.incandescent.util.Hypermap;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.data.ItemTagDatagen;
import xyz.nikgub.pyromancer.network.NetworkCore;
import xyz.nikgub.pyromancer.network.c2s.FlammenklingeMovementPacket;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;
import xyz.nikgub.pyromancer.registry.DamageSourceRegistry;
import xyz.nikgub.pyromancer.registry.ItemRegistry;
import xyz.nikgub.pyromancer.registry.TierRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class FlammenklingeItem extends SwordItem implements IPyromancyItem, IBetterAttributeTooltipItem, IGradientNameItem, IExtensibleTooltipItem
{
    public static final float DEFAULT_DAMAGE = 6F;

    public static final String ENEMIES_COUNTER_TAG = "___FLAMMENKLINGE_ENTITIES_LAUNCHED___";

    public FlammenklingeItem ()
    {
        super(TierRegistry.FLAMMENKLINGE, 0, -2.4f, new Properties().stacksTo(1));
    }

    public static ItemStack fetchStack (Entity entity)
    {
        AtomicReference<IItemHandler> aref = new AtomicReference<>();
        entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(aref::set);
        if (aref.get() != null)
        {
            for (int i = 0; i < aref.get().getSlots(); i++)
            {
                ItemStack itemStack = aref.get().getStackInSlot(i);
                if (itemStack.getItem() == ItemRegistry.FLAMMENKLINGE.get() && itemStack.getOrCreateTag().getInt(ENEMIES_COUNTER_TAG) > 0)
                {
                    //itemStack.getOrCreateTag().putInt(ENEMIES_COUNTER_TAG, 0);
                    return itemStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static void attackProper (@NotNull ServerPlayer self)
    {
        CompoundTag tag = self.getMainHandItem().getOrCreateTag();
        for (LivingEntity entity : EntityUtils.entityCollector(self.position(), 4, self.level()))
        {

            if (entity == self) continue;
            entity.setDeltaMovement(new Vec3(0, 1.2, 0));
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 40);
            entity.hurt(DamageSourceRegistry.flammenklingeLaunch(self), (float) self.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()));
            tag.putInt(FlammenklingeItem.ENEMIES_COUNTER_TAG, tag.getInt(FlammenklingeItem.ENEMIES_COUNTER_TAG) + 1);
        }
        tag.putInt(ENEMIES_COUNTER_TAG, tag.getInt(ENEMIES_COUNTER_TAG) + 10);
        NetworkCore.sendToAll(new FlammenklingeMovementPacket(self.getId(), new Vector3f(2.5f, 0, 2.5f)));
        if (!(self.level() instanceof ServerLevel serverLevel))
        {
            return;
        }
        vortexParticles(serverLevel, self.getX(), self.getY(), self.getZ());
    }

    private static void vortexParticles (ServerLevel level, double X, double Y, double Z)
    {
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

    @Override
    public void inventoryTick (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int tick, boolean b)
    {
        if (!(entity instanceof LivingEntity living)) return;
        CompoundTag tag = living.getMainHandItem().getOrCreateTag();
        if (!living.getMainHandItem().is(ItemTagDatagen.FLAMMENKLINGE_PLUNGE_COMPATIBLE))
        {
            tag.putInt(ENEMIES_COUNTER_TAG, 0);
        }
    }

    @Override
    public void onUseTick (@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        if (!(entity instanceof ServerPlayer player)) return;
        CompoundTag tag = player.getMainHandItem().getOrCreateTag();
        for (LivingEntity target : EntityUtils.entityCollector(player.position(), 4, level))
        {
            if (target == player) continue;
            target.hurt(DamageSourceRegistry.flammenklingeLaunch(player), (float) player.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()));
            target.setRemainingFireTicks(target.getRemainingFireTicks() + 40);
            tag.putInt(ENEMIES_COUNTER_TAG, tag.getInt(ENEMIES_COUNTER_TAG) + 1);
        }
        NetworkCore.sendToAll(new FlammenklingeMovementPacket(player.getId(), new Vector3f(2.5f, 0, 2.5f)));
        BlazingJournalItem.changeBlaze(player, -(int) player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()));
        entity.stopUsingItem();
        if (!(level instanceof ServerLevel serverLevel))
        {
            return;
        }
        vortexParticles(serverLevel, player.getX(), player.getY(), player.getZ());
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
    public boolean hurtEnemy (@NotNull ItemStack pStack, @NotNull LivingEntity target, @NotNull LivingEntity attacker)
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
}
