package xyz.nikgub.pyromancer.network.c2s;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SymbolOfSunMovementPacket
{
    private final int playerId;

    public SymbolOfSunMovementPacket (int playerId)
    {
        this.playerId = playerId;
    }

    public SymbolOfSunMovementPacket (FriendlyByteBuf buf)
    {
        this.playerId = buf.readInt();
    }

    public void toBytes (FriendlyByteBuf buf)
    {
        buf.writeInt(playerId);
    }

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
            double multCoeff = 3.5D;
            if (movementVector.multiply(1, 0, 1).length() <= 0.125D)
            {
                multCoeff = 4.5D;
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
