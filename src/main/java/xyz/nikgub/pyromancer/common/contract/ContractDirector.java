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

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.registry.ContractRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContractDirector
{
    private final List<AccursedContractEntry<?>> available;
    private int credits;

    public ContractDirector (ServerLevel level)
    {
        if (level.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.credits = 0;
            this.available = new ArrayList<>(0);
            return;
        }
        this.credits = PyromancerConfig.defaultContractCredits
                + level.getDifficulty().getId() * 10
                + level.getMoonPhase() * 5
                + ((!level.isDay()) ? 20 : 0);
        List<AccursedContractEntry<?>> available = new ArrayList<>();
        for (AccursedContractEntry<?> entry : ContractRegistry.REGISTRY.get().getValues())
            if (entry.getValueInCredits() <= credits) available.add(entry);
        this.available = available;
    }

    private static <T> T pickRandom (List<T> list)
    {
        Collections.shuffle(list);
        return list.get(0);
    }

    public void run (Player player)
    {
        if (!(player.level() instanceof ServerLevel level)) return;
        List<Entity> summoned = new ArrayList<>();
        while (credits > 0)
        {
            AccursedContractEntry<? extends Entity> randEntry = pickRandom(available);
            summoned.add(randEntry.getCreationFactory().create(level));
            credits -= randEntry.getValueInCredits();
        }
        for (Entity entity : summoned)
        {
            entity.setPos(player.getPosition(0));
            level.addFreshEntity(entity);
        }
    }

}
