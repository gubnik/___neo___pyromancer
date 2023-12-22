package net.nikgub.pyromancer.registries.vanila;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.pyromancer.PyromancerMod;
import net.nikgub.pyromancer.entities.attack_effects.flaming_guillotine.FlamingGuillotineEntity;
import net.nikgub.pyromancer.entities.projectiles.SizzlingHandFireball;

public class EntityTypeRegistry {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PyromancerMod.MOD_ID);
    public static final RegistryObject<EntityType<SizzlingHandFireball>> SIZZLING_HAND_FIREBALL = register("sizzling_hand_fireball",
            EntityType.Builder.<SizzlingHandFireball>of(SizzlingHandFireball::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(16));
    public static final RegistryObject<EntityType<FlamingGuillotineEntity>> FLAMING_GUILLOTINE = register("flaming_guillotine",
            EntityType.Builder.<FlamingGuillotineEntity>of(FlamingGuillotineEntity::new, MobCategory.MISC)
                    .clientTrackingRange(128).setShouldReceiveVelocityUpdates(false));
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registry_name, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITY_TYPES.register(registry_name, () -> entityTypeBuilder.build(registry_name));
    }
}
