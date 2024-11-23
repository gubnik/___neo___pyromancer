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
package xyz.nikgub.pyromancer.registry;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import xyz.nikgub.pyromancer.network.NetworkCore;
import xyz.nikgub.pyromancer.network.key.SwapPyromancyKeyMessage;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
@SuppressWarnings("unused")
public class KeyBindsRegistry
{
    public static final KeyMapping COMPENDIUM_SWAP_PYROMANCY = new KeyMapping("key.pyromancer.swap_pyromancy", GLFW.GLFW_KEY_Z, "key.categories.misc")
    {
        private boolean isDownOld = false;

        @Override
        public void setDown (boolean isDown)
        {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown)
            {
                NetworkCore.sendToServer(new SwapPyromancyKeyMessage(0, 0));
                assert Minecraft.getInstance().player != null;
                SwapPyromancyKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0);
            }
            isDownOld = isDown;
        }
    };

    @SubscribeEvent
    public static void registerKeyMappings (RegisterKeyMappingsEvent event)
    {
        event.register(COMPENDIUM_SWAP_PYROMANCY);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener
    {
        @SubscribeEvent
        public static void onClientTick (TickEvent.ClientTickEvent event)
        {
            if (Minecraft.getInstance().screen == null)
            {
                COMPENDIUM_SWAP_PYROMANCY.consumeClick();
            }
        }
    }
}
