package xyz.nikgub.pyromancer.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.enchantments.MaceEnchantment;
import xyz.nikgub.pyromancer.registries.vanila.enchantments.EnchantmentRegistry;

import java.util.Map;

public class MaceItem extends TieredItem {
    public MaceItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
    public static final float DEFAULT_DAMAGE = 4f;
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull final EquipmentSlot slot, final ItemStack itemStack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        if(slot == EquipmentSlot.MAINHAND)
        {
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8d, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                    this.getTier().getAttackDamageBonus() + DEFAULT_DAMAGE
                    + itemStack.getEnchantmentLevel(EnchantmentRegistry.CLOSE_QUARTERS.get()) * 0.6f,
                    AttributeModifier.Operation.ADDITION));
            Map<Enchantment, Integer> itemEnchants = itemStack.getAllEnchantments();
            for(Enchantment enchantment : itemEnchants.keySet().stream().toList())
            {
                if(enchantment instanceof MaceEnchantment maceEnchantment)
                {
                    for(Attribute attribute : maceEnchantment.getAttributes().keySet().stream().toList())
                    {
                        builder.put(attribute, maceEnchantment.getAttributes().get(attribute).apply(itemEnchants.get(enchantment)));
                    }
                }
            }
        }
        return builder.build();
    }
}
