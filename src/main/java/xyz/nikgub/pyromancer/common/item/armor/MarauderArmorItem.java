package xyz.nikgub.pyromancer.common.item.armor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import xyz.nikgub.pyromancer.registries.ArmorMaterialsRegistry;

public class MarauderArmorItem extends ArmorItem {
    public MarauderArmorItem(Type type) {
        super(ArmorMaterialsRegistry.MARAUDER_ARMOR, type, new Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
    }
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS ? "pyromancer:textures/model/armor/marauder_layer_2.png" : "pyromancer:textures/model/armor/marauder_layer_1.png";
    }
}
