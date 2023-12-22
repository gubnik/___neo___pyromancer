package net.nikgub.pyromancer.enchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.nikgub.pyromancer.registries.vanila.enchantments.EnchantmentCategoryRegistry;

import java.util.function.BiConsumer;

public class BlazingJournalEnchantment extends Enchantment {
    private final Class<? extends TieredItem> weaponClass;
    private final BiConsumer<Player, Entity> attack;
    public BlazingJournalEnchantment(Class<? extends TieredItem> tieredClass, BiConsumer<Player, Entity> biConsumer) {
        super(Rarity.UNCOMMON, EnchantmentCategoryRegistry.BLAZING_JOURNAL, new EquipmentSlot[]{});
        this.weaponClass = tieredClass;
        this.attack = biConsumer;
    }
    public Class<? extends TieredItem> getWeaponClass(){
        return this.weaponClass;
    }
    public BiConsumer<Player, Entity> getAttack(){
        return this.attack;
    }
}
