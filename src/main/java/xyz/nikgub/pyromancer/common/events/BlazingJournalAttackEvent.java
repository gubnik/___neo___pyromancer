package xyz.nikgub.pyromancer.common.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import xyz.nikgub.pyromancer.common.enchantments.BlazingJournalEnchantment;

@SuppressWarnings("unused")
public class BlazingJournalAttackEvent extends AttackEntityEvent {
    private final ItemStack blazingJournal;
    private final ItemStack weapon;
    private final BlazingJournalEnchantment enchantment;
    public BlazingJournalAttackEvent(Player player, Entity target, ItemStack blazingJournal, ItemStack weapon, BlazingJournalEnchantment enchantment) {
        super(player, target);
        this.blazingJournal = blazingJournal;
        this.weapon = weapon;
        this.enchantment = enchantment;
    }
    public ItemStack getBlazingJournal() {
        return blazingJournal;
    }
    public ItemStack getWeapon() {
        return weapon;
    }
    public BlazingJournalEnchantment getEnchantment() {
        return enchantment;
    }
}
