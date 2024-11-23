/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, Nikolay Gubankov (aka nikgub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
