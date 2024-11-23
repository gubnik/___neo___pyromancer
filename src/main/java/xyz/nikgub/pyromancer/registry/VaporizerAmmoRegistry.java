package xyz.nikgub.pyromancer.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.item.VaporizerItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class VaporizerAmmoRegistry
{
    public static final ResourceKey<Registry<VaporizerItem.Ammo>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(PyromancerMod.MOD_ID, "vaporizer_ammo"));
    public static final DeferredRegister<VaporizerItem.Ammo> AMMO = DeferredRegister.create(REGISTRY_KEY, PyromancerMod.MOD_ID);
    public static final Supplier<IForgeRegistry<VaporizerItem.Ammo>> REGISTRY = AMMO.makeRegistry(() -> new RegistryBuilder<VaporizerItem.Ammo>().disableOverrides());
    public static final RegistryObject<VaporizerItem.Ammo> BLAZE_POWDER = AMMO.register("blaze_powder",
            () -> new VaporizerItem.Ammo(Items.BLAZE_POWDER)
            {
                @Override
                public void runConsumeEffect (ItemStack itemStack, LivingEntity living)
                {
                    processRay(ray(itemStack, living, ParticleTypes.FLAME, 3, 100, 1, 0.5f, 0.05f),
                            DamageSourceRegistry.vaporizer(living),
                            (livingEntity) ->
                            {
                                livingEntity.setSecondsOnFire(2);
                                return false;
                            });
                }
            });
    public static final RegistryObject<VaporizerItem.Ammo> AMBER = AMMO.register("amber",
            () -> new VaporizerItem.Ammo(ItemRegistry.AMBER.get())
            {
                @Override
                public void runConsumeEffect (ItemStack itemStack, LivingEntity living)
                {
                    generalRayAmmo(itemStack, living, ParticleTypes.CRIT, 1, 200, 2, 0.1f, 0.01f,
                            DamageSourceRegistry.vaporizer(living),
                            (livingEntity) ->
                            {
                                livingEntity.setSecondsOnFire(2);
                                return false;
                            });
                }
            });

    public static final RegistryObject<VaporizerItem.Ammo> MERCURY = AMMO.register("mercury",
            () -> new VaporizerItem.Ammo(ItemRegistry.DROPS_OF_MERCURY.get())
            {
                @Override
                public void runConsumeEffect (ItemStack itemStack, LivingEntity living)
                {
                    generalRayAmmo(itemStack, living, ParticleRegistry.VAPORIZER_MERCURY.get(), 1, 100, 1, 0.3f, 0.025f,
                            DamageSourceRegistry.vaporizer(living),
                            (livingEntity) ->
                            {
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 30, 2));
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 4));
                                return false;
                            });
                }
            });

    public static boolean isAmmo (Item item)
    {
        return REGISTRY.get().getValues().stream()
                .map(VaporizerItem.Ammo::getConsumedItem)
                .collect(Collectors.toSet())
                .contains(item);
    }

    public static VaporizerItem.Ammo getAmmo (Item item)
    {
        return REGISTRY.get()
                .getValues()
                .stream()
                .filter(ammo -> ammo.getConsumedItem() == item)
                .findFirst().orElseThrow(() -> new NoSuchElementException("No such element: " + item));
    }

    private static void generalRayAmmo (ItemStack itemStack, LivingEntity entity, SimpleParticleType particleType, int rays, int iterations, float initialDamage, float radius, float inaccuracy,
                                        DamageSource damageSource, Function<LivingEntity, Boolean> attackEffect)
    {
        processRay(ray(itemStack, entity, particleType, rays, iterations, initialDamage, radius, inaccuracy), damageSource, attackEffect);
    }

    private static void processRay (Map<LivingEntity, Float> map, DamageSource damageSource, Function<LivingEntity, Boolean> attackEffect)
    {
        for (var entry : map.entrySet())
        {
            var livingEntity = entry.getKey();
            var damage = entry.getValue();
            if (!attackEffect.apply(livingEntity))
            {
                livingEntity.hurt(damageSource, damage);
            }
        }
    }

    private static Map<LivingEntity, Float> ray (ItemStack itemStack, LivingEntity entity, SimpleParticleType particleType, int rays, int iterations, float initialDamage, float radius, float inaccuracy)
    {
        Map<LivingEntity, Float> ret = new HashMap<>();
        if (!(entity.level() instanceof ServerLevel level)) return ret;
        final double x = entity.getX();
        final double y = entity.getY() + entity.getEyeHeight() - 0.4;
        final double z = entity.getZ();
        final Vec3 look = entity.getLookAngle();
        for (int i = 0; i < rays; i++)
        {
            double distance = 1.2;
            float calculatedDamage = initialDamage;
            final Vec3 spreadModifiers = new Vec3(
                    ThreadLocalRandom.current().nextDouble(-inaccuracy, inaccuracy) / 2,
                    ThreadLocalRandom.current().nextDouble(-inaccuracy, inaccuracy) / 4,
                    ThreadLocalRandom.current().nextDouble(-inaccuracy, inaccuracy) / 2
            );
            final Vec3 angles = look.add(spreadModifiers);
            Vec3 lookPos;
            List<? extends LivingEntity> tempList;
            do
            {
                lookPos = new Vec3(x + angles.x * distance, y + angles.y * distance, z + angles.z * distance);
                tempList = EntityUtils.entityCollector(lookPos, radius, entity.level());
                level.sendParticles(particleType, lookPos.x, lookPos.y, lookPos.z, 2, radius, 0, radius, 0);
                distance += 0.2;
                for (LivingEntity living : tempList)
                {
                    if (living == entity) continue;
                    ret.merge(living, calculatedDamage, Float::sum);
                }
                BlockPos blockPos = level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(distance)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
                if (level.getBlockState(blockPos).canOcclude() || distance >= iterations)
                {
                    break;
                }
            } while (true);
        }
        return ret;
    }
}
