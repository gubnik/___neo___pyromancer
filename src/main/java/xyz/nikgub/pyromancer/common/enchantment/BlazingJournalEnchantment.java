package xyz.nikgub.pyromancer.common.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import xyz.nikgub.pyromancer.common.util.ItemUtils;
import xyz.nikgub.pyromancer.registries.EnchantmentRegistry;

public abstract class BlazingJournalEnchantment extends Enchantment
{
    public BlazingJournalEnchantment ()
    {
        super(Rarity.UNCOMMON, EnchantmentRegistry.BLAZING_JOURNAL, new EquipmentSlot[]{});
    }

    public abstract Class<? extends TieredItem> getWeaponClass ();

    public abstract void getAttack (Player player, Entity target);

    public abstract boolean getCondition (Player player, Entity target);

    public boolean globalCondition (Player player)
    {
        return ItemUtils.getBlaze(player) > 0 && player.getAttackStrengthScale(0) > 0.7;
    }
}
