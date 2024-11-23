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
