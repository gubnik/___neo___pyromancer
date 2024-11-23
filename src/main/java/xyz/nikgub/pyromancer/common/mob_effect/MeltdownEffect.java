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
package xyz.nikgub.pyromancer.common.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MeltdownEffect extends SmartMobEffect
{
    public UUID MELTDOWN_MODIFIER_UUID = UUID.fromString("13caa470-1df0-11ee-be56-0242ac120002");

    public MeltdownEffect ()
    {
        super(MobEffectCategory.HARMFUL, 156, 78, 12);
        this.addAttributeModifier(Attributes.ARMOR, MELTDOWN_MODIFIER_UUID.toString(), -1, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isDurationEffectTick (int duration, int amplifier)
    {
        return true;
    }

    @Override
    public @NotNull MobEffect addAttributeModifier (@NotNull Attribute attribute, @NotNull String uuidString, double amount, AttributeModifier.@NotNull Operation operation)
    {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(uuidString), this::getDescriptionId, amount + 1, operation);
        this.getAttributeModifiers().put(attribute, attributemodifier);
        return this;
    }
}
