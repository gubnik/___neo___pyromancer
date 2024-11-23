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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SetDeltaMovementPacket
{
    private final int entityId;
    private final Vector3f movementVec;

    public SetDeltaMovementPacket (int entityId, Vector3f movementVec)
    {
        this.entityId = entityId;
        this.movementVec = movementVec;
    }

    public SetDeltaMovementPacket (FriendlyByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.movementVec = buf.readVector3f();
    }

    public void toBytes (FriendlyByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeVector3f(movementVec);
    }

    public boolean handle (Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() ->
        {
            Minecraft instance = Minecraft.getInstance();
            assert instance.level != null;
            Entity entity = instance.level.getEntity(entityId);
            assert entity != null;
            entity.setDeltaMovement(new Vec3(movementVec));
        });
        return true;
    }
}
