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
package xyz.nikgub.pyromancer.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import xyz.nikgub.incandescent.autogen_network.IncandescentPacket;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.util.function.Supplier;

@IncandescentPacket(value = PyromancerMod.MOD_ID, direction = NetworkDirection.PLAY_TO_CLIENT)
public class SymbolOfSunMovementPacket
{
    @IncandescentPacket.Value
    private Integer playerId;

    public static SymbolOfSunMovementPacket create (int playerId)
    {
        SymbolOfSunMovementPacket instance = new SymbolOfSunMovementPacket();
        instance.playerId = playerId;
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
            Vec3 movementVector = player.getDeltaMovement();
            Vec3 directionVector;
            double multCoeff = 2.5D;
            if (movementVector.multiply(1, 0, 1).length() <= 0.125D)
            {
                multCoeff *= 1.5D;
                directionVector = player.getLookAngle().multiply(1, 0, 1).normalize();
            } else if (player.isSwimming())
            {
                multCoeff = 1.25D;
                directionVector = player.getLookAngle();
            } else directionVector = movementVector.multiply(1, 0, 1).normalize();
            Vec3 nVec = new Vec3(movementVector.x + multCoeff * directionVector.x, movementVector.y + multCoeff * directionVector.y, movementVector.z + multCoeff * directionVector.z);
            player.setDeltaMovement(nVec);
        });
        return true;
    }
}
