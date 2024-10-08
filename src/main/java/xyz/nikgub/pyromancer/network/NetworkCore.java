package xyz.nikgub.pyromancer.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.network.c2s.SetDeltaMovementPacket;
import xyz.nikgub.pyromancer.network.key.SwapPyromancyKeyMessage;
import xyz.nikgub.pyromancer.registry.KeyBindsRegistry;

/**
 * Related:
 * {@link SwapPyromancyKeyMessage}, {@link KeyBindsRegistry}
 */
public class NetworkCore
{
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(PyromancerMod.MOD_ID, "messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

    private static int id = 0;

    public static void register ()
    {
        INSTANCE.messageBuilder(SwapPyromancyKeyMessage.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SwapPyromancyKeyMessage::new)
                .encoder(SwapPyromancyKeyMessage::toBytes)
                .consumerMainThread(SwapPyromancyKeyMessage::handle)
                .add();

        INSTANCE.messageBuilder(SetDeltaMovementPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetDeltaMovementPacket::new)
                .encoder(SetDeltaMovementPacket::toBytes)
                .consumerMainThread(SetDeltaMovementPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer (MSG message)
    {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer (MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersNearby (MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersNearbyAndSelf (MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
    }

    public static <MSG> void sendToAll (MSG message)
    {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
