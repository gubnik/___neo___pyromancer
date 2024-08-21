package xyz.nikgub.pyromancer.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.incandescent.common.util.EntityUtils;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.animation.EmberAnimationList;
import xyz.nikgub.pyromancer.common.ember.Ember;
import xyz.nikgub.pyromancer.common.ember.EmberType;
import xyz.nikgub.pyromancer.common.entity.attack_effect.FlamingGuillotineEntity;
import xyz.nikgub.pyromancer.common.item.MaceItem;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class EmberRegistry
{
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

    public static RegistryObject<Ember> SOULFLAME_IGNITION = registerEmber(new Ember("soulflame_ignition", EmberType.SOULFLAME, EmberAnimationList.SOULFLAME_IGNITION, SwordItem.class, AxeItem.class)
    {
        @Override
        public void finishEvent (ItemStack itemStack, Level level, LivingEntity entity)
        {
            if (!(entity.level() instanceof ServerLevel serverLevel)) return;
            final Vec3 pos = entity.getEyePosition();
            final Vec3 initAngle = entity.getLookAngle().multiply(1.1, 1.1, 1.1);
            for (int loop = 0; loop < 4; loop++)
            {
                Vec3 lookAngle = new Vec3(initAngle.x, initAngle.y, initAngle.z);
                Vec3 cPos;
                for (int i = 0; i < 50; i++)
                {
                    lookAngle = lookAngle.multiply(1.025, 1.025, 1.025);
                    final double d = 0.25;
                    double dx = ThreadLocalRandom.current().nextDouble(-d, d);
                    double dy = ThreadLocalRandom.current().nextDouble(-d / 4, d / 4);
                    double dz = ThreadLocalRandom.current().nextDouble(-d, d);
                    lookAngle = lookAngle.add(dx, dy, dz);
                    cPos = new Vec3(pos.x + lookAngle.x, pos.y + lookAngle.y, pos.z + lookAngle.z);
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, cPos.x, cPos.y, cPos.z, 1, 0, 0, 0, 0);
                    for (LivingEntity target : EntityUtils.entityCollector(cPos, 0.3, serverLevel))
                    {
                        if (target == entity) continue;
                        target.hurt(DamageSourceRegistry.soulflame(entity), (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    }
                }
            }
        }

        @Override
        public void tickEvent (Level level, LivingEntity entity, ItemStack itemStack, int tick)
        {
            if(!(level instanceof ServerLevel serverLevel)) return;
            final float c = (float)(tick)/(float)(this.getAnimation().getUseTime() - 5);
            final double R = 2.5 * c;
            final double X = entity.getX();
            final double Y = entity.getY();
            final double Z = entity.getZ();
            final double sinK = R * Math.sin(Math.toRadians(tick * 18));
            final double cosK = R * Math.cos(Math.toRadians(tick * 18));
            final SimpleParticleType particleType = ParticleTypes.SCULK_SOUL;
            serverLevel.sendParticles(particleType, X + sinK, Y + tick * 0.1, Z + cosK,1, 0.1, 0.1, 0.1, 0);
            serverLevel.sendParticles(particleType, X - sinK, Y + tick * 0.1, Z - cosK,1, 0.1, 0.1, 0.1, 0);
        }
    });

    public static RegistryObject<Ember> PRESERVING_FLAME = registerEmber(new Ember("preserving_flame", EmberType.FLAME, EmberAnimationList.PRESERVING_FLAME, MaceItem.class)
    {
        @Override
        public void tickEvent (Level level, LivingEntity entity, ItemStack itemStack, int tick)
        {
            entity.extinguishFire();
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.FIERY_AEGIS.get(), 2, 0, true, false));
        }
    });

    public static RegistryObject<Ember> EXECUTIONERS_FIRE = registerEmber(new Ember("executioners_fire", EmberType.HELLBLAZE, EmberAnimationList.EXECUTIONERS_FIRE, AxeItem.class)
    {
        @Override
        public void tickEvent (Level level, LivingEntity entity, ItemStack itemStack, int tick)
        {
            if (tick > 3) return;
            for (LivingEntity target : EntityUtils.entityCollector(entity.getEyePosition(), 2, level))
            {
                if (target == entity) continue;
                float multiplier = 1 / Mth.clamp(target.getHealth() / target.getMaxHealth(), 0.2f, 1f);
                float damage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * multiplier;
                if (!(target.hurt(DamageSourceRegistry.hellblaze(entity), damage))) continue;
                if (target.getHealth() > target.getMaxHealth() * 0.2) continue;
                FlamingGuillotineEntity guillotine = new FlamingGuillotineEntity(EntityTypeRegistry.FLAMING_GUILLOTINE.get(), level);
                guillotine.setPlayerUuid(entity.getUUID());
                guillotine.setSize(target.getBbWidth() / 0.6f);
                guillotine.moveTo(target.position());
                guillotine.setYRot(entity.getYRot());
                level.addFreshEntity(guillotine);
            }
        }
    });

}
