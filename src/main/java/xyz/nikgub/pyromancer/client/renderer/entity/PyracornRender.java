package xyz.nikgub.pyromancer.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.model.entity.PyracornModel;
import xyz.nikgub.pyromancer.common.entity.PyracornEntity;

public class PyracornRender extends MobRenderer<PyracornEntity, PyracornModel<PyracornEntity>>
{
    public PyracornRender (EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new PyracornModel<>(renderManager.bakeLayer(PyracornModel.LAYER_LOCATION)), 0.5f);
        this.shadowRadius = 0.3f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation (@NotNull PyracornEntity unburned)
    {
        return new ResourceLocation(PyromancerMod.MOD_ID, "textures/entity/pyracorn.png");
    }
}
