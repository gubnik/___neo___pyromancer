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

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.client.model.armor.PyromancerArmorModel;
import xyz.nikgub.pyromancer.registry.ArmorMaterialsRegistry;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;

import java.util.EnumMap;
import java.util.UUID;
import java.util.function.Consumer;

public class PyromancerArmorItem extends ArmorItem
{

    private static final EnumMap<Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) ->
    {
        map.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        map.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        map.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        map.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });

    private final Multimap<Attribute, AttributeModifier> attributes;

    public PyromancerArmorItem (Type type)
    {
        super(ArmorMaterialsRegistry.PYROMANCER_ARMOR, type, new Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        builder.put(Attributes.ARMOR,
            new AttributeModifier(uuid, "Armor modifier", this.getDefense(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS,
            new AttributeModifier(uuid, "Armor toughness", this.getToughness(), AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0)
            builder.put(Attributes.KNOCKBACK_RESISTANCE,
                new AttributeModifier(uuid, "Armor knockback resistance", ArmorMaterialsRegistry.HELLBLAZE_MONARCH.getKnockbackResistance(), AttributeModifier.Operation.ADDITION));
        builder.put(AttributeRegistry.PYROMANCY_DAMAGE.get(),
            new AttributeModifier(uuid, "Pyromancy damage modifier", this.getAttributeValue(type.getSlot(), AttributeRegistry.PYROMANCY_DAMAGE.get()), AttributeModifier.Operation.MULTIPLY_BASE));
        this.attributes = builder.build();
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers (@NotNull EquipmentSlot equipmentSlot)
    {
        return equipmentSlot == this.type.getSlot() ? this.attributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public String getArmorTexture (ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        return "pyromancer:textures/model/armor/pyromancer_armor.png";
    }

    public float getAttributeValue (EquipmentSlot equipmentSlot, Attribute attribute)
    {
        int[] VALUES = new int[]{10, 15, 20, 10};
        return VALUES[equipmentSlot.getIndex()] * 0.0025F * (attribute.equals(AttributeRegistry.PYROMANCY_DAMAGE.get()) ? 2 : 1);
    }

    @Override
    public void initializeClient (Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            @Override
            @SuppressWarnings("rawtypes")
            public @NotNull HumanoidModel getHumanoidArmorModel (LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel)
            {
                return PyromancerArmorModel.getHumanoidModel(slot);
            }
        });
    }
}
