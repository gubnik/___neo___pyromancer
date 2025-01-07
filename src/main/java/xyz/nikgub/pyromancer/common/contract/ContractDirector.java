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

import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.registry.ContractRegistry;
import xyz.nikgub.pyromancer.registry.StyleRegistry;

import javax.annotation.Nullable;
import java.util.*;

public class ContractDirector
{
    public static final String CONTRACT_SPAWN_TAG = "___PYROMANCER_CONTRACT_SPAWN___";
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
            + level.getDifficulty().getId() * 5
            + level.getMoonPhase() * 5
            + ((!level.isDay()) ? 20 : 0);
        List<AccursedContractEntry<?>> available = new ArrayList<>();
        for (AccursedContractEntry<?> entry : ContractRegistry.REGISTRY.get().getValues())
            if (entry.getValueInCredits() <= credits) available.add(entry);
        this.available = available;
    }

    @Nullable
    private static AccursedContractEntry<? extends Entity> pickRandom (List<AccursedContractEntry<? extends Entity>> list)
    {
        if (list == null || list.isEmpty())
        {
            return null;
        }
        double[] exponentials = new double[list.size()];
        double totalExponential = 0.0;
        for (int i = 0; i < list.size(); i++)
        {
            int weight = list.get(i).getValueInCredits();
            double inverseWeight = 1.0 / weight;
            exponentials[i] = Math.exp(inverseWeight);
            totalExponential += exponentials[i];
        }
        double[] probabilities = new double[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            probabilities[i] = exponentials[i] / totalExponential;
        }
        double randomValue = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < list.size(); i++)
        {
            cumulativeProbability += probabilities[i];
            if (randomValue <= cumulativeProbability)
            {
                return list.get(i);
            }
        }
        return null;
    }

    public static float vulnerableToFire (LivingEntity target, DamageSource damageSource)
    {
        if (target.getPersistentData().getBoolean(ContractDirector.CONTRACT_SPAWN_TAG))
        {
            if (damageSource.is(DamageTypeTags.IS_FIRE))
            {
                return 0.2f;
            }
        }
        return 0.0f;
    }

    public boolean run (Player player)
    {
        if (!(player.level() instanceof ServerLevel level)) return false;
        boolean failed = false;
        Map<Entity, Vec3> summoned = new HashMap<>();
        if (credits == 0) return false;
        while (credits > 0)
        {
            AccursedContractEntry<? extends Entity> randEntry = pickRandom(available);
            if (randEntry == null)
            {
                failed = true;
                break;
            }
            PyromancerMod.LOGGER.info("{}", randEntry.getValueInCredits());
            Entity entity = randEntry.getCreationFactory().create(level);
            var posOpt = getAppropriateSpawnPos(entity, player);
            if (posOpt.isEmpty())
            {
                failed = true;
                break;
            }
            summoned.put(entity, posOpt.get());
            credits -= randEntry.getValueInCredits();
        }
        for (var entry : summoned.entrySet())
        {
            Entity entity = entry.getKey();
            entity.setPos(entry.getValue());
            entity.getPersistentData().putBoolean(CONTRACT_SPAWN_TAG, true);
            entity.lookAt(EntityAnchorArgument.Anchor.EYES, player.getPosition(Minecraft.getInstance().getPartialTick()));
            if (entity instanceof Mob mob && !player.isCreative() && !player.isSpectator())
            {
                mob.setTarget(player);
            }
            level.addFreshEntity(entity);
        }
        if (failed && player instanceof ServerPlayer serverPlayer)
        {
            serverPlayer.sendSystemMessage(Component.translatable("pyromancer.contract.failed").withStyle(StyleRegistry.FROST_STYLE));
            return false;
        }
        return true;
    }

    private Optional<Vec3> getAppropriateSpawnPos (Entity entity, Player player)
    {
        final int attempts = 256;
        final double entityW = entity.getBbWidth();
        final double entityH = entity.getBbHeight();
        final Vec3 initialPos = player.getPosition(Minecraft.getInstance().getPartialTick());
        final Level level = player.level();
        for (int i = 0; i < attempts; i++)
        {
            Vec3 partPos = initialPos.offsetRandom(player.getRandom(), 8);
            BlockPos blockPos = BlockPos.containing(partPos);
            boolean solid = !level.getBlockState(blockPos).isAir();
            boolean freeAbove = level.getBlockState(blockPos.above()).isAir();
            if (solid && freeAbove)
            {
                boolean hasSpace = true;
                int halfWidth = (int) (entityW / 2);
                for (int x = -halfWidth; x <= halfWidth; x++)
                {
                    for (int y = 0; y < entityH; y++)
                    {
                        for (int z = -halfWidth; z <= halfWidth; z++)
                        {
                            BlockPos checkPos = blockPos.above().offset(x, y, z);
                            if (!level.getBlockState(checkPos).isAir())
                            {
                                hasSpace = false;
                                break;
                            }
                        }
                        if (!hasSpace)
                        {
                            break;
                        }
                    }
                    if (!hasSpace)
                    {
                        break;
                    }
                }
                if (hasSpace)
                {
                    return Optional.of(partPos.add(0, 1, 0));
                }
            }
        }
        return Optional.empty();
    }

}
