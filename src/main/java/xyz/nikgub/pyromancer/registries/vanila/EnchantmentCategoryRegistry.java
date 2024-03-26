package xyz.nikgub.pyromancer.registries.vanila;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import xyz.nikgub.pyromancer.items.BlazingJournalItem;

public class EnchantmentCategoryRegistry {
    public static final EnchantmentCategory BLAZING_JOURNAL = EnchantmentCategory.create("blazing_journal", (item -> item instanceof BlazingJournalItem));
}
