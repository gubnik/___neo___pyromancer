package net.nikgub.pyromancer.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class MaceEnchantment extends Enchantment {
    public static final UUID STURDINESS_ARMOR_TOUGHNESS_UUID = UUID.fromString("32e69cb7-d0ca-46b0-9e83-f612ce793eb9");
    private final Map<Attribute, Function<Integer, AttributeModifier>> attributes;
    public MaceEnchantment(Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots, Map<Attribute, Function<Integer, AttributeModifier>> attributes) {
        super(rarity, enchantmentCategory, equipmentSlots);
        this.attributes = attributes;
    }

    public Map<Attribute, Function<Integer, AttributeModifier>> getAttributes() {
        return attributes;
    }
}
