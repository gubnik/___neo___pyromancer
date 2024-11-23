package xyz.nikgub.pyromancer.common.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import xyz.nikgub.pyromancer.common.ember.UniqueEmberBehaviour;
import xyz.nikgub.pyromancer.registry.AttributeRegistry;

import java.util.Collection;
import java.util.UUID;

/**
 * Interface that enables pyromancy functionality <p>
 * Must be used when making any pyromancy
 */
@UniqueEmberBehaviour(allow = UniqueEmberBehaviour.AllowanceModifier.DENY)
public interface IPyromancyItem
{

    UUID BASE_BLAZE_CONSUMPTION_UUID = UUID.fromString("39f6d6b6-f4f9-11ed-a05b-0242ac120003");
    UUID BASE_PYROMANCY_DAMAGE_UUID = UUID.fromString("4ec062f8-14ff-11ee-be56-0242ac120002");
    UUID JOURNAL_BLAZE_CONSUMPTION_UUID = UUID.fromString("574d4092-16c3-11ee-be56-0242ac120002");
    UUID JOURNAL_PYROMANCY_DAMAGE_UUID = UUID.fromString("704049d2-16c3-11ee-be56-0242ac120002");

    default float getAttributeBonus (Player player, Attribute attribute)
    {
        float d0 = 0;
        float M0 = 0;
        float M1 = 0;
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        for (ItemStack itemStack : player.getArmorSlots())
        {
            for (EquipmentSlot slot : slots)
            {
                for (AttributeModifier attributeModifier : itemStack.getAttributeModifiers(slot).get(attribute))
                {
                    switch(attributeModifier.getOperation())
                    {
                        case ADDITION -> d0 += (float) attributeModifier.getAmount();
                        case MULTIPLY_BASE -> M0 += (float) attributeModifier.getAmount();
                        case MULTIPLY_TOTAL -> M1 += (float) attributeModifier.getAmount();
                    }
                }
            }
        }
        if (player.getMainHandItem().getItem() instanceof CompendiumOfFlameItem compendiumOfFlameItem)
        {
            if (!(compendiumOfFlameItem.getItemFromItem(player.getMainHandItem(), 0).getItem() instanceof QuillItem quillItem))
                return d0;
            if (attribute == AttributeRegistry.PYROMANCY_DAMAGE.get()) d0 += quillItem.getDefaultPyromancyDamageBonus();
            else if (attribute == AttributeRegistry.BLAZE_CONSUMPTION.get()) d0 += quillItem.getDefaultBlazeCostBonus();
            return d0;
        }
        //if (!(player.getOffhandItem().getItem() instanceof BlazingJournalItem)) return d0;
        Collection<AttributeModifier> collection = player.getOffhandItem().getAttributeModifiers(EquipmentSlot.OFFHAND).get(attribute);
        for (AttributeModifier attributeModifier : collection) d0 += (float) attributeModifier.getAmount();
        return d0 + this.getDefaultPyromancyDamage() * M0 + this.getDefaultPyromancyDamage() * M1;
    }

    float getDefaultPyromancyDamage ();

    int getDefaultBlazeCost ();
}
