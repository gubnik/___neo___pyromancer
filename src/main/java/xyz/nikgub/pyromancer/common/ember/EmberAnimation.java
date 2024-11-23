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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author nikgub_
 * <p>
 * Animation that is used for an ember.
 */
@NotNull
public abstract class EmberAnimation
{
    private final int useTime;
    private final int cooldown;

    public EmberAnimation (int useTime, int cooldown)
    {
        this.useTime = useTime;
        this.cooldown = cooldown;
    }

    public abstract ThirdPersonAnimation getThirdPersonAnimation ();

    public abstract FirstPersonAnimation getFirstPersonAnimation ();

    public int getUseTime ()
    {
        return useTime;
    }

    public int getCooldown ()
    {
        return cooldown;
    }

    @FunctionalInterface
    public interface ThirdPersonAnimation
    {
        void run (HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm);
    }

    @FunctionalInterface
    public interface FirstPersonAnimation
    {
        void run (PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess);
    }
}
