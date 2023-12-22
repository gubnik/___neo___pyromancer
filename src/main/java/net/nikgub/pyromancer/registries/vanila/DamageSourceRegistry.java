package net.nikgub.pyromancer.registries.vanila;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.nikgub.pyromancer.data.damage.PDamageType;
import net.nikgub.pyromancer.entities.attack_effects.AttackEffectEntity;
import net.nikgub.pyromancer.entities.projectiles.SizzlingHandFireball;

public class DamageSourceRegistry {
    public static DamageSource hellblaze(Entity entity)
    {
        return new DamageSource(
                entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(PDamageType.HELLBLAZE_KEY),
                entity
        );
    }
    public static DamageSource firebriar(Entity entity)
    {
        return new DamageSource(
                entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(PDamageType.FIREBRIAR_KEY),
                entity
        );
    }
    public static DamageSource sizzlingHand(SizzlingHandFireball sizzlingHandFireball, Entity owner) {
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(PDamageType.HELLBLAZE_KEY),
                sizzlingHandFireball,
                owner
        );
    }
    public static DamageSource blazingJournal(AttackEffectEntity entity, Entity owner) {
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(PDamageType.BLAZING_JOURNAL_PROJECTION_KEY),
                entity,
                owner
        );
    }
}
