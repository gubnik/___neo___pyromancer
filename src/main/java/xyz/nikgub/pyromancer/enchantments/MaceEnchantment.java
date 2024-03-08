package xyz.nikgub.pyromancer.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class MaceEnchantment extends Enchantment {
    public static final UUID STURDINESS_ARMOR_TOUGHNESS_UUID    = UUID.fromString("32e69cb7-d0ca-46b0-9e83-f612ce793eb9");
    public static final UUID CLOSE_QUARTERS_REACH_UUID          = UUID.fromString("3d778736-42e7-45d6-8382-3a126a656581");
    public static final UUID BLUNT_IMPACT_ARMOR_PIERCING_UUID   = UUID.fromString("032edf8c-c051-4da1-8992-da55f030d767");

    private final Map<Attribute, Function<Integer, AttributeModifier>> attributes;
    public MaceEnchantment(Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots, Map<Attribute, Function<Integer, AttributeModifier>> attributes) {
        super(rarity, enchantmentCategory, equipmentSlots);
        this.attributes = attributes;
    }

    public Map<Attribute, Function<Integer, AttributeModifier>> getAttributes() {
        return attributes;
    }
}
