/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, Nikolay Gubankov (aka nikgub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package xyz.nikgub.pyromancer.common.item.armor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import xyz.nikgub.pyromancer.registry.ArmorMaterialsRegistry;

public class MarauderArmorItem extends ArmorItem
{
    public MarauderArmorItem (Type type)
    {
        super(ArmorMaterialsRegistry.MARAUDER_ARMOR, type, new Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
    }

    @Override
    public String getArmorTexture (ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        return slot == EquipmentSlot.LEGS ? "pyromancer:textures/model/armor/marauder_layer_2.png" : "pyromancer:textures/model/armor/marauder_layer_1.png";
    }
}
