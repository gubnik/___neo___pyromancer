package net.nikgub.pyromancer.registries.vanila.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.enchantments.BlazingJournalEnchantment;
import net.nikgub.pyromancer.enchantments.MaceEnchantment;
import net.nikgub.pyromancer.entities.attack_effects.flaming_guillotine.FlamingGuillotineEntity;
import net.nikgub.pyromancer.registries.vanila.EntityTypeRegistry;

import java.util.Map;

public class EnchantmentRegistry {
    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PyromancerMod.MOD_ID);
    public static RegistryObject<Enchantment> FLAMING_GUILLOTINE = ENCHANTMENTS.register("flaming_guillotine",
            () -> new BlazingJournalEnchantment(AxeItem.class, ((player, livingEntity) -> {
                FlamingGuillotineEntity guillotine = new FlamingGuillotineEntity(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), player.level());
                guillotine.setPlayerUuid(player.getUUID());
                guillotine.setSize(livingEntity.getBbWidth()/0.6f);
                guillotine.moveTo(livingEntity.position());
                guillotine.setYRot(player.getYRot());
                player.level().addFreshEntity(guillotine);
            })));
    public static RegistryObject<Enchantment> STURDINESS = ENCHANTMENTS.register("sturdiness",
            () -> new MaceEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategoryRegistry.MACE, new EquipmentSlot[]{},
                    Map.of(Attributes.ARMOR, (lev) -> new AttributeModifier(MaceEnchantment.STURDINESS_ARMOR_TOUGHNESS_UUID, "Weapon modifier", lev, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel() {return 5;}
            });
}
