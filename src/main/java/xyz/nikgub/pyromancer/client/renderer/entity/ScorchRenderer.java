package xyz.nikgub.pyromancer.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.model.entity.ScorchModel;
import xyz.nikgub.pyromancer.common.entity.ScorchEntity;

public class ScorchRenderer extends MobRenderer<ScorchEntity, ScorchModel<ScorchEntity>>
{
    public ScorchRenderer (EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new ScorchModel<>(renderManager.bakeLayer(ScorchModel.LAYER_LOCATION)), 0.5f);
        this.shadowRadius = 1.0f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation (@NotNull ScorchEntity unburned)
    {
        return new ResourceLocation(PyromancerMod.MOD_ID, "textures/entity/scorch.png");
    }
}
