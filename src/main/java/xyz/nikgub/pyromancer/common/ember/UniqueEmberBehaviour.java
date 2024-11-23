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
package xyz.nikgub.pyromancer.common.ember;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for overriding default mixin behaviour
 * Should be used for ensure proper behaviour with unique weaponry
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface UniqueEmberBehaviour
{
    /**
     * @return Modifier overriding default logic
     */
    AllowanceModifier allow ();

    /**
     * @return Additive modifier applied to the cost of one ember usage
     */
    int costModifier () default 1;

    enum AllowanceModifier
    {
        /**
         * Overriding default behaviour
         */
        DENY,
        ALLOW,
        NONE
    }
}
