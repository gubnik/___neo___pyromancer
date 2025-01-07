package xyz.nikgub.pyromancer.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.model.entity.RimegazerModel;
import xyz.nikgub.pyromancer.common.entity.RimegazerEntity;

public class RimegazerRenderer extends MobRenderer<RimegazerEntity, RimegazerModel<RimegazerEntity>>
{
    public RimegazerRenderer (EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new RimegazerModel<>(renderManager.bakeLayer(RimegazerModel.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation (@NotNull RimegazerEntity unburned)
    {
        return new ResourceLocation(PyromancerMod.MOD_ID, "textures/entity/rimegazer.png");
    }
}