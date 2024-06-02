package xyz.nikgub.pyromancer.common.registries.vanila.enchantments;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.enchantments.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.common.enchantments.MaceEnchantment;
import xyz.nikgub.pyromancer.common.entities.attack_effects.FlamingGuillotineEntity;
import xyz.nikgub.pyromancer.common.registries.vanila.AttributeRegistry;
import xyz.nikgub.pyromancer.common.registries.vanila.EntityTypeRegistry;
import xyz.nikgub.pyromancer.common.registries.vanila.MobEffectRegistry;

import java.util.Map;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PyromancerMod.MOD_ID);

    public static RegistryObject<Enchantment> FLAMING_GUILLOTINE = ENCHANTMENTS.register("flaming_guillotine",
            () -> new BlazingJournalEnchantment(AxeItem.class,
                    ((player, entity) -> {
                        FlamingGuillotineEntity guillotine = new FlamingGuillotineEntity(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), player.level());
                        guillotine.setPlayerUuid(player.getUUID());
                        guillotine.setSize(entity.getBbWidth() / 0.6f);
                        guillotine.moveTo(entity.position());
                        guillotine.setYRot(player.getYRot());
                        player.level().addFreshEntity(guillotine);
                    }), ((player, entity) -> entity.isOnFire())));

    public static RegistryObject<Enchantment> METAL_MELTDOWN = ENCHANTMENTS.register("metal_meltdown",
            () -> new BlazingJournalEnchantment(PickaxeItem.class,
                    ((player, entity) -> {
                        if(!(entity instanceof LivingEntity livingEntity)) return;
                        livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.MELTDOWN.get(), 60,  livingEntity.getEffect(MobEffectRegistry.MELTDOWN.get()).getAmplifier() + 1));
                    }), ((player, entity) -> entity.isOnFire())));

    public static RegistryObject<Enchantment> STURDINESS = ENCHANTMENTS.register("sturdiness",
            () -> new MaceEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategoryRegistry.MACE, new EquipmentSlot[]{},
                    Map.of(Attributes.ARMOR, (lev) -> new AttributeModifier(MaceEnchantment.STURDINESS_ARMOR_TOUGHNESS_UUID, "Weapon modifier", lev, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel() {return 5;}
            });

    public static RegistryObject<Enchantment> BLUNT_IMPACT = ENCHANTMENTS.register("blunt_impact",
            () -> new MaceEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategoryRegistry.MACE, new EquipmentSlot[]{},
                    Map.of(AttributeRegistry.ARMOR_PIERCING.get(), lev -> new AttributeModifier(MaceEnchantment.BLUNT_IMPACT_ARMOR_PIERCING_UUID, "Weapon modifier", lev * 2, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel() {return 5;}
            });

    //public static RegistryObject<Enchantment> BLUNT_IMPACT = ENCHANTMENTS.register("blunt_impact",
    //        () -> new MaceEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategoryRegistry.MACE, new EquipmentSlot[]{},
    //                Map.of(
    //                        //AttributeRegistry.ARMOR_PIERCING.get(), lev -> new AttributeModifier(MaceEnchantment.BLUNT_IMPACT_ARMOR_PIERCING_UUID, "Weapon modifier", lev * 2, AttributeModifier.Operation.ADDITION)
    //                ))
    //        {
    //            @Override
    //            public int getMaxLevel() {return 2;}
    //        });

    public static RegistryObject<Enchantment> CLOSE_QUARTERS = ENCHANTMENTS.register("close_quarters",
            () -> new MaceEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategoryRegistry.MACE, new EquipmentSlot[]{},
                    Map.of(ForgeMod.ENTITY_REACH.get(), (lev) -> new AttributeModifier(MaceEnchantment.CLOSE_QUARTERS_REACH_UUID, "Weapon modifier", -lev * 0.5f, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel() {return 3;}
            });
}
