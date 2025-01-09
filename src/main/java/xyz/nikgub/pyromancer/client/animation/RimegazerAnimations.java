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
package xyz.nikgub.pyromancer.client.animation;

import net.minecraft.client.animation.AnimationDefinition;
import xyz.nikgub.incandescent.client.animations.from_text.Pyranim;

public class RimegazerAnimations
{
    public static final AnimationDefinition ATTACK = Pyranim.ofEntity("data/pyromancer/entity_animation/rimegazer_attack.pyranim");
    public static final AnimationDefinition SPIN = Pyranim.ofEntity("data/pyromancer/entity_animation/rimegazer_spin.pyranim");
}