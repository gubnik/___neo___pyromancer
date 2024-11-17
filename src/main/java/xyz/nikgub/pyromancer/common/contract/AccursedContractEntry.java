package xyz.nikgub.pyromancer.common.contract;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AccursedContractEntry<T extends Entity>
{
    private final EntityType<T> entityToSummon;
    private final Factory<T> creationFactory;
    private final int valueInCredits;

    public AccursedContractEntry (EntityType<T> entityType, Factory<T> factory, int valueInCredits)
    {
        this.entityToSummon = entityType;
        this.creationFactory = factory;
        this.valueInCredits = valueInCredits;
    }

    public AccursedContractEntry (EntityType<T> entityType, int valueInCredits)
    {
        this.entityToSummon = entityType;
        this.creationFactory = entityType::create;
        this.valueInCredits = valueInCredits;
    }

    public EntityType<T> getEntityToSummon ()
    {
        return entityToSummon;
    }

    public Factory<T> getCreationFactory ()
    {
        return creationFactory;
    }

    public int getValueInCredits ()
    {
        return valueInCredits;
    }

    @FunctionalInterface
    public interface Factory<T extends Entity>
    {
        T create (Level level);
    }
}
