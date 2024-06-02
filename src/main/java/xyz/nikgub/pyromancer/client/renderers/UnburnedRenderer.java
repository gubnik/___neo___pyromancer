package xyz.nikgub.pyromancer.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import xyz.nikgub.pyromancer.PyromancerMod;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.client.models.entities.UnburnedModel;
import xyz.nikgub.pyromancer.common.entities.unburned.UnburnedEntity;

public class UnburnedRenderer extends MobRenderer<UnburnedEntity, UnburnedModel<UnburnedEntity>> {
    public UnburnedRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new UnburnedModel<>(renderManager.bakeLayer(UnburnedModel.LAYER_LOCATION)), 0.5f);
        this.shadowRadius = 1f;
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull UnburnedEntity unburned){
        return new ResourceLocation(PyromancerMod.MOD_ID, "textures/entity/unburned.png");
    }
}
