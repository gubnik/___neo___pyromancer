package xyz.nikgub.pyromancer.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.model.entity.PyroentModel;
import xyz.nikgub.pyromancer.common.entity.PyroentEntity;

public class PyroentRenderer extends MobRenderer<PyroentEntity, PyroentModel<PyroentEntity>>
{
    public PyroentRenderer (EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new PyroentModel<>(renderManager.bakeLayer(PyroentModel.LAYER_LOCATION)), 0.5f);
        this.shadowRadius = 1.0f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation (@NotNull PyroentEntity unburned)
    {
        return new ResourceLocation(PyromancerMod.MOD_ID, "textures/entity/pyroent.png");
    }
}
