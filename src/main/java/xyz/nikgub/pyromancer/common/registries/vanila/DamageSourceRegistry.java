package xyz.nikgub.pyromancer.common.registries.vanila;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import xyz.nikgub.pyromancer.data.DamageTypeDatagen;
import xyz.nikgub.pyromancer.common.entities.attack_effects.AttackEffectEntity;
import xyz.nikgub.pyromancer.common.entities.attack_effects.PyronadoEntity;
import xyz.nikgub.pyromancer.common.entities.projectiles.SizzlingHandFireball;
import xyz.nikgub.pyromancer.common.entities.projectiles.BombsackProjectile;
import xyz.nikgub.pyromancer.common.entities.unburned.UnburnedEntity;

public class DamageSourceRegistry {
    public static DamageSource hellblaze(Entity entity)
    {
        assert entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.HELLBLAZE_KEY),
                entity
        );
    }

    public static DamageSource firebriar(Entity entity)
    {
        assert entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.FIREBRIAR_KEY),
                entity
        );
    }

    public static DamageSource sizzlingHand(SizzlingHandFireball sizzlingHandFireball, Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
            owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.SIZZLING_HAND_KEY),
            sizzlingHandFireball,
            owner
        );
    }

    public static DamageSource bombsack(BombsackProjectile bombsackProjectile, Entity owner) {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.BOMBSACK_KEY),
                bombsackProjectile,
                owner
        );
    }

    public static DamageSource pyronado(PyronadoEntity pyronadoEntity, Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
            owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.COURT_OF_EMBERS_KEY),
            pyronadoEntity,
            owner
        );
    }

    public static DamageSource blazingJournal(AttackEffectEntity entity, Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.BLAZING_JOURNAL_PROJECTION_KEY),
                entity,
                owner
        );
    }

    public static DamageSource unburnedExplosion(UnburnedEntity unburned)
    {
        assert unburned.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                unburned.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.UNBURNED_KEY),
                unburned
        );
    }

}
