package xyz.nikgub.pyromancer.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.registry.EnchantmentRegistry;

import java.util.Set;
import java.util.UUID;

public class ZweihanderItem extends Item
{

    public static final Set<ToolAction> ACTIONS = Set.of(ToolActions.SWORD_SWEEP);
    private static final UUID REACH_UUID = UUID.fromString("ba808878-e417-4af3-9987-630bbcbd120f");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("ee28a837-0676-47a1-9e2a-8de73c49eb8c");
    private static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("a11982cc-39c5-4b6f-8bff-303a7c75deda");

    public ZweihanderItem (Properties properties)
    {
        super(properties.stacksTo(1).defaultDurability(1500));
    }

    @Override
    public boolean isDamageable (ItemStack itemStack)
    {
        return true;
    }

    @Override
    public boolean isEnchantable (@NotNull ItemStack itemStack)
    {
        return true;
    }

    @Override
    public int getEnchantmentValue (ItemStack itemStack)
    {
        return 15;
    }

    @Override
    public void inventoryTick (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int tick, boolean b)
    {
        if (!(entity instanceof Player player)) return;
        if (player.getMainHandItem() != itemStack)
        {
            CompoundTag tag = itemStack.getOrCreateTag();
            if (tag.getBoolean("ProperSwing")) tag.putBoolean("ProperSwing", false);
            return;

        }
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.getBoolean("ProperSwing")) return;
        if (player.getAttackStrengthScale(0) == 1)
        {
            tag.putBoolean("ProperSwing", false);
        }
    }

    public boolean hurtEnemy (@NotNull ItemStack itemStack, @NotNull LivingEntity target, @NotNull LivingEntity source)
    {
        double mod = Math.abs(target.getDeltaMovement().length() + source.getDeltaMovement().length()) * itemStack.getEnchantmentLevel(EnchantmentRegistry.WEIGHT.get()) * 0.35f;
        target.knockback(0.5 + mod, Math.sin(source.getYRot() * ((float) Math.PI / 180F)), -Math.cos(source.getYRot() * ((float) Math.PI / 180F)));
        return false;
    }

    @Override
    public boolean canAttackBlock (@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player)
    {
        return !player.isCreative();
    }

    @Override
    public boolean canPerformAction (ItemStack stack, ToolAction toolAction)
    {
        return ACTIONS.contains(toolAction);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers (EquipmentSlot slot, ItemStack itemStack)
    {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        if (slot == EquipmentSlot.MAINHAND)
        {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 11D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.2D, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(REACH_UUID, "Weapon modifier", 1 + itemStack.getEnchantmentLevel(EnchantmentRegistry.GIANT.get()), AttributeModifier.Operation.ADDITION));
            if (itemStack.getOrCreateTag().getBoolean("ProperSwing"))
            {
                builder.put(Attributes.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(KNOCKBACK_RESISTANCE_UUID, "Weapon modifier", 0.6 + itemStack.getEnchantmentLevel(EnchantmentRegistry.POISE.get()) * 0.1, AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.ARMOR_TOUGHNESS,
                        new AttributeModifier(TOUGHNESS_UUID, "Weapon modifier", (1 + itemStack.getEnchantmentLevel(EnchantmentRegistry.POISE.get())) * 5, AttributeModifier.Operation.ADDITION));
            }
        }
        return builder.build();
    }
}
