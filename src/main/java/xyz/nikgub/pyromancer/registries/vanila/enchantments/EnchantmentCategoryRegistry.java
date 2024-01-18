package xyz.nikgub.pyromancer.registries.vanila.enchantments;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import xyz.nikgub.pyromancer.items.MaceItem;
import xyz.nikgub.pyromancer.items.BlazingJournalItem;

public class EnchantmentCategoryRegistry {
    public static final EnchantmentCategory BLAZING_JOURNAL = EnchantmentCategory.create("blazing_journal", (item -> item instanceof BlazingJournalItem));
    public static final EnchantmentCategory MACE = EnchantmentCategory.create("mace", (item -> item instanceof MaceItem));
}