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
package xyz.nikgub.pyromancer.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.model.entity.UnburnedModel;
import xyz.nikgub.pyromancer.common.entity.UnburnedEntity;

public class UnburnedRenderer extends MobRenderer<UnburnedEntity, UnburnedModel<UnburnedEntity>>
{
    public UnburnedRenderer (EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new UnburnedModel<>(renderManager.bakeLayer(UnburnedModel.LAYER_LOCATION)), 0.5f);
        this.shadowRadius = 1f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation (@NotNull UnburnedEntity unburned)
    {
        return new ResourceLocation(PyromancerMod.MOD_ID, "textures/entity/unburned.png");
    }
}
