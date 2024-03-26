package xyz.nikgub.pyromancer.registries.vanila;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.enchantments.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.entities.attack_effects.flaming_guillotine.FlamingGuillotineEntity;

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
}
