package xyz.nikgub.pyromancer.enchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import xyz.nikgub.pyromancer.registries.vanila.EnchantmentCategoryRegistry;
import xyz.nikgub.pyromancer.util.ItemUtils;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class BlazingJournalEnchantment extends Enchantment {
    private final Class<? extends TieredItem> weaponClass;
    private final BiConsumer<Player, Entity> attack;
    private final BiFunction<Player, Entity, Boolean> condition;

    public BlazingJournalEnchantment(Class<? extends TieredItem> tieredClass, BiConsumer<Player, Entity> attack, BiFunction<Player, Entity, Boolean> condition) {
        super(Rarity.UNCOMMON, EnchantmentCategoryRegistry.BLAZING_JOURNAL, new EquipmentSlot[]{});
        this.weaponClass = tieredClass;
        this.attack = attack;
        this.condition = condition;
    }
    public Class<? extends TieredItem> getWeaponClass(){
        return this.weaponClass;
    }
    public BiConsumer<Player, Entity> getAttack(){
        return this.attack;
    }
    public BiFunction<Player, Entity, Boolean> getCondition() {
        return condition;
    }
    public boolean defaultCondition(Player player)
    {
        return ItemUtils.getBlaze(player) > 0 && player.getAttackStrengthScale(0) > 0.7;
    }
}
