package xyz.nikgub.pyromancer.common.registries.vanila.enchantments;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import xyz.nikgub.pyromancer.common.items.MaceItem;
import xyz.nikgub.pyromancer.common.items.BlazingJournalItem;

public class EnchantmentCategoryRegistry {
    public static final EnchantmentCategory BLAZING_JOURNAL = EnchantmentCategory.create("blazing_journal", (item -> item instanceof BlazingJournalItem));
}
