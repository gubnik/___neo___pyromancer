package xyz.nikgub.pyromancer.common.contract;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.registry.ContractRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    private static <T> T pickRandom (Iterable<T> iterable)
    {
        final List<T> list = new ArrayList<>();
        iterable.forEach((list::add));
        return list.get(
                ThreadLocalRandom.current().nextInt(0, list.size())
        );
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
