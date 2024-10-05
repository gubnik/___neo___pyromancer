package xyz.nikgub.pyromancer.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.common.entity.FrostcopperGolemEntity;
import xyz.nikgub.pyromancer.common.entity.UnburnedEntity;
import xyz.nikgub.pyromancer.common.entity.attack_effect.PyronadoEntity;
import xyz.nikgub.pyromancer.common.entity.projectile.BombsackProjectile;
import xyz.nikgub.pyromancer.common.entity.projectile.SizzlingHandFireball;
import xyz.nikgub.pyromancer.data.DamageTypeDatagen;

import javax.annotation.Nullable;

public class DamageSourceRegistry
{

    public static DamageSource flame (Entity entity)
    {
        assert entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.HELLBLAZE_KEY),
                entity
        );
    }

    public static DamageSource hellblaze (Entity entity)
    {
        assert entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.HELLBLAZE_KEY),
                entity
        );
    }

    public static DamageSource soulflame (Entity entity)
    {
        assert entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.SOULFLAME_KEY),
                entity
        );
    }

    public static DamageSource firebriar (Level level)
    {
        assert level.registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                level.registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.FIREBRIAR_KEY)
        );
    }

    public static DamageSource sizzlingHand (SizzlingHandFireball sizzlingHandFireball, Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.SIZZLING_HAND_KEY),
                sizzlingHandFireball,
                owner
        );
    }

    public static DamageSource symbolOfSun (Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.SYMBOL_OF_SUN_KEY),
                owner,
                owner
        );
    }

    public static DamageSource bombsack (BombsackProjectile bombsackProjectile, Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.BOMBSACK_KEY),
                bombsackProjectile,
                owner
        );
    }

    public static DamageSource pyronado (PyronadoEntity pyronadoEntity, Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.COURT_OF_EMBERS_KEY),
                pyronadoEntity,
                owner
        );
    }

    public static DamageSource blazingJournal (Entity entity, @Nullable Entity owner)
    {
        assert entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.BLAZING_JOURNAL_PROJECTION_KEY),
                entity,
                owner
        );
    }

    public static DamageSource guillotine (@NotNull Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.BLAZING_JOURNAL_PROJECTION_KEY),
                owner,
                owner
        );
    }

    public static DamageSource unburnedExplosion (UnburnedEntity unburned)
    {
        assert unburned.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                unburned.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.UNBURNED_KEY),
                unburned
        );
    }

    public static DamageSource hoarfrostGreatswordPoke (Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.HOARFROST_GREATSWORD_KEY),
                owner,
                owner
        );
    }

    public static DamageSource spearOfMoroz (Entity owner)
    {
        assert owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                owner.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.SPEAR_OF_MOROZ_KEY),
                owner,
                owner
        );
    }

    public static DamageSource frostcopperGolemStomp (FrostcopperGolemEntity frostcopperGolemEntity)
    {
        assert frostcopperGolemEntity.level().registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                frostcopperGolemEntity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.FROSTCOPPER_GOLEM_STOMP_KEY),
                frostcopperGolemEntity,
                frostcopperGolemEntity
        );
    }

    public static DamageSource hogtrap (Level level)
    {
        assert level.registryAccess().registry(Registries.DAMAGE_TYPE).isPresent();
        return new DamageSource(
                level.registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DamageTypeDatagen.HOGTRAP_KEY)
        );
    }
}
