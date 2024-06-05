package xyz.nikgub.pyromancer.common.enchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import xyz.nikgub.pyromancer.common.registries.EnchantmentRegistry;
import xyz.nikgub.pyromancer.common.util.ItemUtils;

public abstract class BlazingJournalEnchantment extends Enchantment {

    public BlazingJournalEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentRegistry.BLAZING_JOURNAL, new EquipmentSlot[]{});
    }
    public abstract Class<? extends TieredItem> getWeaponClass();
    public abstract void getAttack(Player player, Entity target);
    public abstract boolean getCondition(Player player, Entity target);
    public boolean defaultCondition(Player player)
    {
        return ItemUtils.getBlaze(player) > 0 && player.getAttackStrengthScale(0) > 0.7;
    }
}
