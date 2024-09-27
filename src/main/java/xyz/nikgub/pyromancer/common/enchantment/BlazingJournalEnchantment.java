package xyz.nikgub.pyromancer.common.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.common.item.BlazingJournalItem;
import xyz.nikgub.pyromancer.registry.EnchantmentRegistry;

public abstract class BlazingJournalEnchantment extends Enchantment
{
    public BlazingJournalEnchantment ()
    {
        super(Rarity.UNCOMMON, EnchantmentRegistry.BLAZING_JOURNAL, new EquipmentSlot[]{});
    }

    @Override
    public @NotNull Component getFullname(int pLevel)
    {
        Component component = super.getFullname(pLevel);
        Style style = component.getStyle();
        return component.copy().withStyle(style).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD);
    }

    public abstract Class<? extends TieredItem> getWeaponClass ();

    public abstract void getAttack (Player player, Entity target);

    public abstract boolean getCondition (Player player, Entity target);

    public boolean globalCondition (Player player)
    {
        return BlazingJournalItem.getBlaze(player) > 0 && player.getAttackStrengthScale(0) > 0.7;
    }
}
