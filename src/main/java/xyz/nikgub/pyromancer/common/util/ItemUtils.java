package xyz.nikgub.pyromancer.common.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;

public class ItemUtils
{

    public static boolean hasFullSetEquipped (LivingEntity entity, ArmorItem checkedArmorItem)
    {
        boolean b = true;
        for (EquipmentSlot equipmentSlot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET})
        {
            if (!(entity.getItemBySlot(equipmentSlot).getItem() instanceof ArmorItem armorItem) || !armorItem.getMaterial().equals(checkedArmorItem.getMaterial()))
            {
                b = false;
                break;
            }
        }
        return b;
    }
}
