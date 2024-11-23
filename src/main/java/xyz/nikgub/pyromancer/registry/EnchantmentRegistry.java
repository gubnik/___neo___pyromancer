package xyz.nikgub.pyromancer.registry;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.enchantment.BlazingJournalEnchantment;
import xyz.nikgub.pyromancer.common.enchantment.MaceEnchantment;
import xyz.nikgub.pyromancer.common.entity.attack_effect.FlamingGuillotineEntity;
import xyz.nikgub.pyromancer.common.item.BlazingJournalItem;
import xyz.nikgub.pyromancer.common.item.MaceItem;
import xyz.nikgub.pyromancer.common.item.MusketItem;
import xyz.nikgub.pyromancer.common.item.ZweihanderItem;
import xyz.nikgub.pyromancer.data.ItemTagDatagen;

import java.util.Map;

public class EnchantmentRegistry
{

    public static final EnchantmentCategory BLAZING_JOURNAL = EnchantmentCategory.create("blazing_journal", (item -> item instanceof BlazingJournalItem));
    public static final EnchantmentCategory MACE = EnchantmentCategory.create("mace", (item -> item instanceof MaceItem));
    public static final EnchantmentCategory FROST_WEAPONS = EnchantmentCategory.create("frost_weapons", (item) -> new ItemStack(item).is(ItemTagDatagen.FROST_WEAPON));
    public static final EnchantmentCategory ZWEIHANDER_CATEGORY = EnchantmentCategory.create("zweihander", (item -> item instanceof ZweihanderItem));
    public static final EnchantmentCategory MUSKET_CATEGORY = EnchantmentCategory.create("musket", (item -> item instanceof MusketItem));
    public static final EnchantmentCategory SHIELD_CATEGORY = EnchantmentCategory.create("shield", (item -> item instanceof ShieldItem));
    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PyromancerMod.MOD_ID);

    public static final RegistryObject<Enchantment> FIERCE_FROST = ENCHANTMENTS.register("fierce_frost", () -> new Enchantment(Enchantment.Rarity.UNCOMMON, FROST_WEAPONS, new EquipmentSlot[]{})
    {
        @Override
        public int getMaxLevel ()
        {
            return 3;
        }
    });

    public static final RegistryObject<Enchantment> MELT = ENCHANTMENTS.register("melt", () -> new Enchantment(Enchantment.Rarity.UNCOMMON, FROST_WEAPONS, new EquipmentSlot[]{})
    {
        @Override
        public int getMaxLevel ()
        {
            return 5;
        }
    });

    public static final RegistryObject<Enchantment> WEIGHT = ENCHANTMENTS.register("weight",
            () -> new Enchantment(Enchantment.Rarity.COMMON, ZWEIHANDER_CATEGORY, new EquipmentSlot[]{})
            {
                @Override
                public int getMaxLevel ()
                {
                    return 5;
                }
            });

    public static final RegistryObject<Enchantment> POISE = ENCHANTMENTS.register("poise",
            () -> new Enchantment(Enchantment.Rarity.COMMON, ZWEIHANDER_CATEGORY, new EquipmentSlot[]{})
            {
                @Override
                public int getMaxLevel ()
                {
                    return 5;
                }
            });

    public static final RegistryObject<Enchantment> GIANT = ENCHANTMENTS.register("giant",
            () -> new Enchantment(Enchantment.Rarity.UNCOMMON, ZWEIHANDER_CATEGORY, new EquipmentSlot[]{})
            {
                @Override
                public int getMaxLevel ()
                {
                    return 3;
                }
            });

    public static final RegistryObject<Enchantment> CURSE_OF_CHAOS = ENCHANTMENTS.register("curse_of_chaos",
            () -> new Enchantment(Enchantment.Rarity.COMMON, ZWEIHANDER_CATEGORY, new EquipmentSlot[]{})
            {
                @Override
                public int getMaxLevel ()
                {
                    return 1;
                }

                @Override
                public boolean isTreasureOnly ()
                {
                    return true;
                }

                @Override
                public boolean isCurse ()
                {
                    return true;
                }
            });

    public static final RegistryObject<Enchantment> TROOPER = ENCHANTMENTS.register("trooper",
            () -> new Enchantment(Enchantment.Rarity.UNCOMMON, MUSKET_CATEGORY, new EquipmentSlot[]{})
            {
                @Override
                public int getMaxLevel ()
                {
                    return 1;
                }

                @Override
                public boolean isTreasureOnly ()
                {
                    return true;
                }
            });

    public static final RegistryObject<Enchantment> ASSAULT = ENCHANTMENTS.register("assault",
            () -> new Enchantment(Enchantment.Rarity.UNCOMMON, MUSKET_CATEGORY, new EquipmentSlot[]{})
            {
                @Override
                public int getMaxLevel ()
                {
                    return 1;
                }

                @Override
                public boolean isTreasureOnly ()
                {
                    return true;
                }
            });

    public static RegistryObject<Enchantment> FLAMING_GUILLOTINE = ENCHANTMENTS.register("flaming_guillotine",
            () -> new BlazingJournalEnchantment()
            {
                @Override
                public Class<? extends TieredItem> getWeaponClass ()
                {
                    return AxeItem.class;
                }

                @Override
                public void getAttack (Player player, Entity target)
                {
                    FlamingGuillotineEntity guillotine = FlamingGuillotineEntity.createWithDamage(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), player.level(), 3, true); //new FlamingGuillotineEntity(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), player.level());
                    guillotine.setSize(target.getBbWidth() / 0.6f);
                    guillotine.addToLevelForPlayerAt(player.level(), player, target.position());
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
                public void getAttack (Player player, Entity target)
                {
                    if (!(target instanceof LivingEntity livingEntity)) return;
                    livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.MELTDOWN.get(), 60, livingEntity.getEffect(MobEffectRegistry.MELTDOWN.get()).getAmplifier() + 1));
                }

                @Override
                public boolean getCondition (Player player, Entity target)
                {
                    return target.isOnFire();
                }
            });

    public static RegistryObject<Enchantment> METEORIC_STRIKE = ENCHANTMENTS.register("meteoric_strike",
            () -> new BlazingJournalEnchantment()
            {
                @Override
                public Class<? extends TieredItem> getWeaponClass ()
                {
                    return ShovelItem.class;
                }

                @Override
                public void getAttack (Player player, Entity target)
                {
                    if (!(target instanceof LivingEntity livingEntity)) return;
                    if (livingEntity.isOnFire())
                        livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.METEORIC_STRIKE.get(), 100, 0));
                    livingEntity.setRemainingFireTicks(livingEntity.getRemainingFireTicks() + 40);
                    livingEntity.setDeltaMovement(player.getLookAngle().multiply(2.5, 0, 2.5).add(0, 0.2, 0));
                }

                @Override
                public boolean getCondition (Player player, Entity target)
                {
                    return true;
                }
            });

    // TODO : implement
    public static RegistryObject<Enchantment> WRATHFUL_BLADES = ENCHANTMENTS.register("wrathful_blades",
            () -> new BlazingJournalEnchantment()
            {
                @Override
                public Class<? extends TieredItem> getWeaponClass ()
                {
                    return SwordItem.class;
                }

                @Override
                public void getAttack (Player player, Entity target)
                {
                }

                @Override
                public boolean getCondition (Player player, Entity target)
                {
                    return true;
                }
            });

    public static RegistryObject<Enchantment> INFERNAL_HARVEST = ENCHANTMENTS.register("infernal_harvest",
            () -> new BlazingJournalEnchantment()
            {
                @Override
                public Class<? extends TieredItem> getWeaponClass ()
                {
                    return HoeItem.class;
                }

                @Override
                public void getAttack (Player player, Entity target)
                {
                    if (!(player.level() instanceof ServerLevel level)) return;
                    final double X = target.getX();
                    final double Y = target.getY();
                    final double Z = target.getZ();
                    for (int tickCount = 0; tickCount < 20; tickCount++)
                    {
                        final double c = 1 - (double) tickCount / 20;
                        final double R = 4 * c;
                        final double sinK = R * Math.sin(Math.toRadians(tickCount * 18));
                        final double cosK = R * Math.cos(Math.toRadians(tickCount * 18));
                        level.sendParticles(ParticleTypes.FLAME, X + sinK, Y + tickCount * 0.1, Z + cosK, (int) (1 + 5 * c), 0.1, 0.1, 0.1, 0);
                        level.sendParticles(ParticleTypes.FLAME, X - sinK, Y + tickCount * 0.1, Z - cosK, (int) (1 + 5 * c), 0.1, 0.1, 0.1, 0);
                    }
                    double dx, dy, dz;
                    Vec3 direction;
                    for (LivingEntity entity : EntityUtils.entityCollector(new Vec3(X, Y, Z), 4, level))
                    {
                        if (!entity.equals(player))
                        {
                            entity.hurt(DamageSourceRegistry.blazingJournal(target, player), (float) player.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()) * 0.25f);
                            dx = X - entity.getX();
                            dy = Y - entity.getY();
                            dz = Z - entity.getZ();
                            direction = new Vec3(dx, dy, dz).normalize();
                            entity.setDeltaMovement(direction);
                            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 20);
                        }
                    }
                }

                @Override
                public boolean getCondition (Player player, Entity target)
                {
                    return true;
                }
            });


    public static RegistryObject<Enchantment> STURDINESS = ENCHANTMENTS.register("sturdiness",
            () -> new MaceEnchantment(Enchantment.Rarity.COMMON, MACE, new EquipmentSlot[]{},
                    Map.of(Attributes.ARMOR, (lev) -> new AttributeModifier(MaceEnchantment.STURDINESS_ARMOR_TOUGHNESS_UUID, "Weapon modifier", lev, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel ()
                {
                    return 5;
                }
            });

    public static RegistryObject<Enchantment> BLUNT_IMPACT = ENCHANTMENTS.register("blunt_impact",
            () -> new MaceEnchantment(Enchantment.Rarity.COMMON, MACE, new EquipmentSlot[]{},
                    Map.of(AttributeRegistry.ARMOR_PIERCING.get(), lev -> new AttributeModifier(MaceEnchantment.BLUNT_IMPACT_ARMOR_PIERCING_UUID, "Weapon modifier", lev * 2, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel ()
                {
                    return 5;
                }
            });

    public static RegistryObject<Enchantment> CLOSE_QUARTERS = ENCHANTMENTS.register("close_quarters",
            () -> new MaceEnchantment(Enchantment.Rarity.UNCOMMON, MACE, new EquipmentSlot[]{},
                    Map.of(ForgeMod.ENTITY_REACH.get(), (lev) -> new AttributeModifier(MaceEnchantment.CLOSE_QUARTERS_REACH_UUID, "Weapon modifier", -lev * 0.5f, AttributeModifier.Operation.ADDITION)))
            {
                @Override
                public int getMaxLevel ()
                {
                    return 3;
                }
            });

    public static class Utils
    {
        public static float tryCurseOfChaos (LivingEntity target)
        {
            if (target instanceof Player)
            {
                target.setSecondsOnFire(5);
                return 0.25F;
            }
            return -0.5F;
        }
    }

    public static final RegistryObject<Enchantment> SCATTERSHOT = ENCHANTMENTS.register("scattershot",
            () -> new Enchantment(Enchantment.Rarity.COMMON, MUSKET_CATEGORY, new EquipmentSlot[]{})
            {
                @Override
                public int getMaxLevel ()
                {
                    return 3;
                }

                @Override
                public boolean checkCompatibility (@NotNull Enchantment enchantment)
                {
                    return this != enchantment && enchantment != RIFLING.get();
                }
            });

    public static final RegistryObject<Enchantment> RIFLING = ENCHANTMENTS.register("rifling",
            () -> new Enchantment(Enchantment.Rarity.COMMON, MUSKET_CATEGORY, new EquipmentSlot[]{})
            {
                @Override
                public int getMaxLevel ()
                {
                    return 3;
                }

                @Override
                public boolean checkCompatibility (@NotNull Enchantment enchantment)
                {
                    return this != enchantment && enchantment != SCATTERSHOT.get();
                }
            });


}
