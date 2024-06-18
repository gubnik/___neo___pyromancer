package xyz.nikgub.pyromancer.registries;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.enchantments.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.common.enchantments.MaceEnchantment;
import xyz.nikgub.pyromancer.common.entities.attack_effects.FlamingGuillotineEntity;
import xyz.nikgub.pyromancer.common.items.BlazingJournalItem;
import xyz.nikgub.pyromancer.common.items.MaceItem;

import java.util.Map;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PyromancerMod.MOD_ID);

    public static final EnchantmentCategory BLAZING_JOURNAL = EnchantmentCategory.create("blazing_journal", (item -> item instanceof BlazingJournalItem));
    public static final EnchantmentCategory MACE = EnchantmentCategory.create("mace", (item -> item instanceof MaceItem));
    
    public static RegistryObject<Enchantment> FLAMING_GUILLOTINE = ENCHANTMENTS.register("flaming_guillotine",
            () -> new BlazingJournalEnchantment()
            {
                @Override
                public Class<? extends TieredItem> getWeaponClass ()
                {
                    return AxeItem.class;
                }

                @Override
                public void getAttack(Player player, Entity target)
                {
                    FlamingGuillotineEntity guillotine = new FlamingGuillotineEntity(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), player.level());
                    guillotine.setPlayerUuid(player.getUUID());
                    guillotine.setSize(target.getBbWidth() / 0.6f);
                    guillotine.moveTo(target.position());
                    guillotine.setYRot(player.getYRot());
                    player.level().addFreshEntity(guillotine);
                }

                @Override
                public boolean getCondition (Player player, Entity target)
                {
                    return target.isOnFire();
                }
            });

    public static RegistryObject<Enchantment> METAL_MELTDOWN = ENCHANTMENTS.register("metal_meltdown",
            () -> new BlazingJournalEnchantment()
            {
                @Override
                public Class<? extends TieredItem> getWeaponClass ()
                {
                    return PickaxeItem.class;
                }

                @Override
                public void getAttack(Player player, Entity target)
                {
                    if(!(target instanceof LivingEntity livingEntity)) return;
                    livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.MELTDOWN.get(), 60,  livingEntity.getEffect(MobEffectRegistry.MELTDOWN.get()).getAmplifier() + 1));
                }

                @Override
                public boolean getCondition (Player player, Entity target)
                {
                    return target.isOnFire();
                }
            });

    public static RegistryObject<Enchantment> STURDINESS = ENCHANTMENTS.register("sturdiness",
            () -> new MaceEnchantment(Enchantment.Rarity.COMMON, MACE, new EquipmentSlot[]{},
                    Map.of(Attributes.ARMOR, (lev) -> new AttributeModifier(MaceEnchantment.STURDINESS_ARMOR_TOUGHNESS_UUID, "Weapon modifier", lev, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel() {return 5;}
            });

    public static RegistryObject<Enchantment> BLUNT_IMPACT = ENCHANTMENTS.register("blunt_impact",
            () -> new MaceEnchantment(Enchantment.Rarity.COMMON, MACE, new EquipmentSlot[]{},
                    Map.of(AttributeRegistry.ARMOR_PIERCING.get(), lev -> new AttributeModifier(MaceEnchantment.BLUNT_IMPACT_ARMOR_PIERCING_UUID, "Weapon modifier", lev * 2, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel() {return 5;}
            });

    public static RegistryObject<Enchantment> CLOSE_QUARTERS = ENCHANTMENTS.register("close_quarters",
            () -> new MaceEnchantment(Enchantment.Rarity.UNCOMMON, MACE, new EquipmentSlot[]{},
                    Map.of(ForgeMod.ENTITY_REACH.get(), (lev) -> new AttributeModifier(MaceEnchantment.CLOSE_QUARTERS_REACH_UUID, "Weapon modifier", -lev * 0.5f, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel() {return 3;}
            });
}
