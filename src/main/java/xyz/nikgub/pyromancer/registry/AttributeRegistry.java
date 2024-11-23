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
package xyz.nikgub.pyromancer.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;

public class AttributeRegistry
{
    public static DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, PyromancerMod.MOD_ID);

    public static RegistryObject<Attribute> ARMOR_PIERCING = registerAttribute(new RangedAttribute("pyromancer.armor_piercing", 0, -30, 30));

    public static RegistryObject<Attribute> BLAZE_CONSUMPTION = registerAttribute(new RangedAttribute("pyromancer.blaze_consumption", 0, 0, Integer.MAX_VALUE));

    public static RegistryObject<Attribute> PYROMANCY_DAMAGE = registerAttribute(new RangedAttribute("pyromancer.pyromancy_damage", 0, 0, Integer.MAX_VALUE));

    public static RegistryObject<Attribute> COLD_BUILDUP = registerAttribute(new RangedAttribute("pyromancer.cold_buildup", 0, 0, Integer.MAX_VALUE));

    private static RegistryObject<Attribute> registerAttribute (Attribute attribute)
    {
        return ATTRIBUTES.register(attribute.getDescriptionId(), () -> attribute);
    }
}
