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