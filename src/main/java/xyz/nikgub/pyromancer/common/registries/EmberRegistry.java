package xyz.nikgub.pyromancer.common.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.animations.EmberAnimationList;
import xyz.nikgub.pyromancer.common.ember.Ember;
import xyz.nikgub.pyromancer.common.ember.EmberType;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class EmberRegistry{
    public static String EMBER_TAG = "___PYROMANCER_EMBER_TAG___";
    public static final ResourceKey<Registry<Ember>> EMBER_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(PyromancerMod.MOD_ID, "embers"));
    public static final DeferredRegister<Ember> EMBERS = DeferredRegister.create(EMBER_REGISTRY_KEY, PyromancerMod.MOD_ID);
    public static final Supplier<IForgeRegistry<Ember>> REGISTRY = EMBERS.makeRegistry(() -> new RegistryBuilder<Ember>().disableOverrides());
    public static RegistryObject<Ember> registerEmber(Ember ember)
    {
        return EMBERS.register(ember.getName(), () -> ember);
    }
    @Nullable
    public static Ember getEmberByName(String name)
    {
        return REGISTRY.get()
                .getValues()
                .stream()
                .filter((ember -> Objects.equals(ember.getName(), name)))
                .findFirst().orElse(null);
    }

    public static RegistryObject<Ember> SOULFLAME_IGNITION = registerEmber(new Ember("soulflame_ignition", EmberType.SOULFLAME.get(), Ember.GENERAL_WEAPONS, EmberAnimationList.SOULFLAME_IGNITION)
    {
        @Override
        public void finishEvent (ItemStack itemStack, Level level, LivingEntity entity)
        {
            if (!(entity.level() instanceof ServerLevel serverLevel)) return;
            for (int loop = 0; loop < 4; loop++)
            {
                final Vec3 pos = entity.getEyePosition();
                final Vec3 initAngle = entity.getLookAngle();
                Vec3 lookAngle = new Vec3(initAngle.x, initAngle.y, initAngle.z);
                for (int i = 0; i < 200; i++)
                {
                    lookAngle = lookAngle.multiply(1.05, 1.05, 1.05);
                    final double d = 0.2;
                    double dx = ThreadLocalRandom.current().nextDouble(-d, d);
                    double dy = ThreadLocalRandom.current().nextDouble(-d, d);
                    double dz = ThreadLocalRandom.current().nextDouble(-d, d);
                    lookAngle = lookAngle.add(dx, dy, dz);
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x + lookAngle.x, pos.y + lookAngle.y, pos.z + lookAngle.z, 1, 0, 0, 0, 0);
                }
            }
        }
    });

    public static RegistryObject<Ember> PRESERVING_FLAME = registerEmber(new Ember("preserving_flame", EmberType.FLAME.get(), Ember.MACES, EmberAnimationList.PRESERVING_FLAME)
    {
        @Override
        public void tickEvent (Level level, LivingEntity entity, ItemStack itemStack, int tick)
        {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 4, true, false));
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10, 4, true, false));
        }
    });

}
