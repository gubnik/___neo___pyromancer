package xyz.nikgub.pyromancer.registries;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.common.item.MaceItem;
import xyz.nikgub.pyromancer.common.item.SymbolOfSunItem;

public enum TierRegistry implements Tier
{

    AMBER(256, 13f, 2f, 4, 17, ItemRegistry.AMBER.get()),
    SYMBOL_OF_SUN(2780, 13f, SymbolOfSunItem.DEFAULT_DAMAGE - MaceItem.DEFAULT_DAMAGE - 1, 4, 0, ItemRegistry.AMBER.get());

    private final int useCount;
    private final float speed;
    private final float attackDamageBonus;
    private final int level;
    private final int enchantmentValue;
    private final ItemLike ingredient;

    TierRegistry (int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, ItemLike ingredient)
    {
        this.useCount = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.level = level;
        this.enchantmentValue = enchantmentValue;
        this.ingredient = ingredient;
    }

    @Override
    public int getUses ()
    {
        return this.useCount;
    }

    @Override
    public float getSpeed ()
    {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus ()
    {
        return this.attackDamageBonus;
    }

    @Override
    public int getLevel ()
    {
        return this.level;
    }

    @Override
    public int getEnchantmentValue ()
    {
        return this.enchantmentValue;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient ()
    {
        return Ingredient.of(this.ingredient);
    }
}
