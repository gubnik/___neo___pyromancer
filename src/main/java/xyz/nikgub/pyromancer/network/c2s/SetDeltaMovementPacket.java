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
