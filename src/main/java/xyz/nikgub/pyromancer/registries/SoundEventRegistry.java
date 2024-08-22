package xyz.nikgub.pyromancer.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.nikgub.pyromancer.PyromancerMod;

public class SoundEventRegistry
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PyromancerMod.MOD_ID);

    public static final RegistryObject<SoundEvent> MUSKET_SHOT = SOUNDS.register("musket_shot",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PyromancerMod.MOD_ID, "musket_shot")));
    public static final RegistryObject<SoundEvent> MUSKET_LOAD = SOUNDS.register("musket_load",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PyromancerMod.MOD_ID, "musket_load")));
}
