package xyz.nikgub.pyromancer.items.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.client.models.armor.PyromancerArmorModel;
import xyz.nikgub.pyromancer.registries.vanila.ArmorMaterialsRegistry;

import java.util.function.Consumer;

public class MarauderArmorItem extends ArmorItem {
    public MarauderArmorItem(Type type) {
        super(ArmorMaterialsRegistry.MARAUDER_ARMOR, type, new Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
    }
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS ? "pyromancer:textures/model/armor/marauder_layer_2.png" : "pyromancer:textures/model/armor/marauder_layer_1.png";
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
