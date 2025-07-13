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
package xyz.nikgub.pyromancer.network.c2s;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;
import xyz.nikgub.incandescent.autogen_network.IncandescentPacket;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.function.Supplier;

@IncandescentPacket(value = PyromancerMod.MOD_ID, direction = NetworkDirection.PLAY_TO_SERVER)
public class FlammenklingeMovementPacket
{
    @IncandescentPacket.Value
    private Integer playerId;

    @IncandescentPacket.Value
    private Vector3f multipliers;

    public static FlammenklingeMovementPacket create(int playerId, Vector3f multipliers)
    {
        var instance = new FlammenklingeMovementPacket();
        instance.playerId = playerId;
        instance.multipliers = multipliers;
        return instance;
    }

    @IncandescentPacket.Handler
    public boolean handle (Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() ->
        {
            Minecraft instance = Minecraft.getInstance();
            assert instance.level != null;
            var player = instance.level.getEntity(playerId);
            assert player != null;
            final Vec3 srcVec = player.getDeltaMovement();
            final Vec3 nVec = srcVec.multiply(new Vec3(multipliers)).add(0, 1.2, 0);
            player.setDeltaMovement(nVec);
        });
        return true;
    }
}
