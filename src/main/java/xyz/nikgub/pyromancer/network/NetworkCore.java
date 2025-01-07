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
package xyz.nikgub.pyromancer.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.network.c2s.FlammenklingeMovementPacket;
import xyz.nikgub.pyromancer.network.c2s.SetDeltaMovementPacket;
import xyz.nikgub.pyromancer.network.c2s.SymbolOfSunMovementPacket;
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

        INSTANCE.messageBuilder(SymbolOfSunMovementPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SymbolOfSunMovementPacket::new)
            .encoder(SymbolOfSunMovementPacket::toBytes)
            .consumerMainThread(SymbolOfSunMovementPacket::handle)
            .add();

        INSTANCE.messageBuilder(FlammenklingeMovementPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .decoder(FlammenklingeMovementPacket::new)
            .encoder(FlammenklingeMovementPacket::toBytes)
            .consumerMainThread(FlammenklingeMovementPacket::handle)
            .add();

        //INSTANCE.messageBuilder(FlammenklingeLeapPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
        //        .decoder(FlammenklingeLeapPacket::new)
        //        .encoder(FlammenklingeLeapPacket::toBytes)
        //        .consumerMainThread(FlammenklingeLeapPacket::handle)
        //        .add();
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
