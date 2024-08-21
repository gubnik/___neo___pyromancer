package xyz.nikgub.pyromancer.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.client.model.entity.FrostcopperGolemModel;
import xyz.nikgub.pyromancer.common.entity.FrostcopperGolemEntity;

public class FrostcopperGolemRenderer extends MobRenderer<FrostcopperGolemEntity, FrostcopperGolemModel<FrostcopperGolemEntity>>
{
    public FrostcopperGolemRenderer(EntityRendererProvider.Context pContext)
    {
        super(pContext, new FrostcopperGolemModel<>(pContext.bakeLayer(FrostcopperGolemModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FrostcopperGolemEntity pEntity)
    {
        return new ResourceLocation(PyromancerMod.MOD_ID, "textures/entity/frostcopper_golem.png");
    }
}
