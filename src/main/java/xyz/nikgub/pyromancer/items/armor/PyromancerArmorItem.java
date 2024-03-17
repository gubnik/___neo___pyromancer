package xyz.nikgub.pyromancer.items.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.client.models.armor.PyromancerArmorModel;
import xyz.nikgub.pyromancer.registries.vanila.ArmorMaterialsRegistry;
import xyz.nikgub.pyromancer.registries.vanila.AttributeRegistry;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PyromancerArmorItem extends ArmorItem {

    public final Map<EquipmentSlot, UUID> PYROMANCY_DAMAGE_UUID_MAP = Map.of(
            EquipmentSlot.HEAD, UUID.fromString("6aada1d8-4b50-11ee-be56-0242ac120002"),
            EquipmentSlot.CHEST, UUID.fromString("6aada1d8-4b50-11ee-be56-0242ac120003"),
            EquipmentSlot.LEGS, UUID.fromString("6aada1d8-4b50-11ee-be56-0242ac120004"),
            EquipmentSlot.FEET, UUID.fromString("6aada1d8-4b50-11ee-be56-0242ac120005")
    );

    private final Multimap<Attribute, AttributeModifier> attributes;

    public PyromancerArmorItem(Type type) {
        super(ArmorMaterialsRegistry.PYROMANCER_ARMOR, type, new Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        EquipmentSlot slot = type.getSlot();
        builder.putAll(super.getDefaultAttributeModifiers(slot));
        builder.put(AttributeRegistry.PYROMANCY_DAMAGE.get(),
                new AttributeModifier(PYROMANCY_DAMAGE_UUID_MAP.get(slot), "Pyromancy damage modifier", this.getAttributeValue(slot, AttributeRegistry.PYROMANCY_DAMAGE.get()), AttributeModifier.Operation.MULTIPLY_BASE));
        this.attributes = builder.build();
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        return equipmentSlot == this.type.getSlot() ? this.attributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "pyromancer:textures/model/armor/pyromancer_armor.png";
    }

    public float getAttributeValue(EquipmentSlot equipmentSlot, Attribute attribute){
        int[] VALUES = new int[]{10, 15, 20, 10};
        return VALUES[equipmentSlot.getIndex()] * 0.005F * (attribute.equals(AttributeRegistry.PYROMANCY_DAMAGE.get()) ? 2 : 1);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            @SuppressWarnings("rawtypes")
            public @NotNull HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                return PyromancerArmorModel.getHumanoidModel(slot);
            }
        });
    }
}
