package xyz.nikgub.pyromancer.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.common.entity.*;
import xyz.nikgub.pyromancer.common.entity.attack_effect.FlamingGuillotineEntity;
import xyz.nikgub.pyromancer.common.entity.attack_effect.PyronadoEntity;
import xyz.nikgub.pyromancer.common.entity.projectile.BombsackProjectile;
import xyz.nikgub.pyromancer.common.entity.projectile.NapalmBombsackProjectile;
import xyz.nikgub.pyromancer.common.entity.projectile.ScattershotBombsackProjectile;
import xyz.nikgub.pyromancer.common.entity.projectile.SizzlingHandFireball;

public class EntityTypeRegistry
{
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PyromancerMod.MOD_ID);

    public static final RegistryObject<EntityType<SizzlingHandFireball>> SIZZLING_HAND_FIREBALL = register("sizzling_hand_fireball",
            EntityType.Builder.<SizzlingHandFireball>of(SizzlingHandFireball::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(16));

    public static final RegistryObject<EntityType<BombsackProjectile>> BOMBSACK = register("bombsack",
            EntityType.Builder.of(BombsackProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(16));

    public static final RegistryObject<EntityType<ScattershotBombsackProjectile>> SCATTERSHOT_BOMBSACK = register("scattershot_bombsack",
            EntityType.Builder.of(ScattershotBombsackProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(16));

    public static final RegistryObject<EntityType<NapalmBombsackProjectile>> NAPALM_BOMBSACK = register("napalm_bombsack",
            EntityType.Builder.of(NapalmBombsackProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(16));

    public static final RegistryObject<EntityType<FlamingGuillotineEntity>> FLAMING_GUILLOTINE = register("flaming_guillotine",
            EntityType.Builder.of(FlamingGuillotineEntity::new, MobCategory.MISC)
                    .clientTrackingRange(128).setShouldReceiveVelocityUpdates(false));

    public static final RegistryObject<EntityType<UnburnedSpiritEntity>> UNBURNED_SPIRIT = register("unburned_spirit",
            EntityType.Builder.of(UnburnedSpiritEntity::new, MobCategory.MISC)
                    .clientTrackingRange(128).setShouldReceiveVelocityUpdates(false).fireImmune());

    public static final RegistryObject<EntityType<PyronadoEntity>> PYRONADO = register("pyronado",
            EntityType.Builder.of(PyronadoEntity::new, MobCategory.MISC)
                    .clientTrackingRange(128).setShouldReceiveVelocityUpdates(false));

    public static final RegistryObject<EntityType<UnburnedEntity>> UNBURNED = register("unburned",
            EntityType.Builder.of(UnburnedEntity::new, MobCategory.MONSTER)
                    .sized(1.3f, 5.2f).fireImmune());

    public static final RegistryObject<EntityType<ScorchEntity>> SCORCH = register("scorch",
            EntityType.Builder.of(ScorchEntity::new, MobCategory.MONSTER)
                    .sized(1.3f, 1.2f).fireImmune());

    public static final RegistryObject<EntityType<PyracornEntity>> PYRACORN = register("pyracorn",
            EntityType.Builder.of(PyracornEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.5f).fireImmune());
    public static final RegistryObject<EntityType<PyroentEntity>> PYROENT = register("pyroent",
            EntityType.Builder.of(PyroentEntity::new, MobCategory.MONSTER)
                    .sized(1.5f, 4.0f).fireImmune());

    public static final RegistryObject<EntityType<FrostcopperGolemEntity>> FROSTCOPPER_GOLEM = register("frostcopper_golem",
            EntityType.Builder.of(FrostcopperGolemEntity::new, MobCategory.MONSTER)
                    .sized(1.1f, 2.1f));

    private static <T extends Entity> RegistryObject<EntityType<T>> register (String registry_name, EntityType.Builder<T> entityTypeBuilder)
    {
        return ENTITY_TYPES.register(registry_name, () -> entityTypeBuilder.build(registry_name));
    }
}
