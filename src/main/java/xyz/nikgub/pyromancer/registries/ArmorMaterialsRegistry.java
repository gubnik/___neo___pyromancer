package xyz.nikgub.pyromancer.registries;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum ArmorMaterialsRegistry implements ArmorMaterial
{
    MARAUDER_ARMOR("marauder", 20, new int[]{1, 2, 3, 1}, 12, SoundEvents.ARMOR_EQUIP_NETHERITE, 0, 0.02f, () -> Ingredient.of(ItemRegistry.HOGLIN_HIDE.get())),
    PYROMANCER_ARMOR("pyromancer", 20, new int[]{2, 3, 4, 2}, 13, SoundEvents.ARMOR_EQUIP_NETHERITE, 0, 0, () -> Ingredient.of(Items.LEATHER)),
    HELLBLAZE_MONARCH("hellblaze_monarch", 20, new int[]{2, 5, 6, 2}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 5, 0.1f, () -> Ingredient.of(Items.LEATHER));

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredient;

    ArmorMaterialsRegistry (String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient)
    {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient.get();
    }

    @Override
    public int getDurabilityForType (ArmorItem.Type type)
    {
        return HEALTH_PER_SLOT[type.getSlot().getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType (ArmorItem.Type type)
    {
        return this.slotProtections[type.getSlot().getIndex()];
    }

    public int getEnchantmentValue ()
    {
        return this.enchantmentValue;
    }

    public @NotNull SoundEvent getEquipSound ()
    {
        return this.sound;
    }

    public @NotNull Ingredient getRepairIngredient ()
    {
        return this.repairIngredient;
    }

    public @NotNull String getName ()
    {
        return this.name;
    }

    public float getToughness ()
    {
        return this.toughness;
    }

    public float getKnockbackResistance ()
    {
        return this.knockbackResistance;
    }
}

