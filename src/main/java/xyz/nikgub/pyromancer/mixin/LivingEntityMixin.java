package xyz.nikgub.pyromancer.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.common.item.SpearOfMorozItem;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
    @Inject(method = "tryAddFrost", at = @At("HEAD"), cancellable = true)
    protected void tryAddFrost (CallbackInfo callbackInfo)
    {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;
        ItemStack spear = player.getMainHandItem();
        if (!(spear.getItem() == ItemRegistry.SPEAR_OF_MOROZ.get() && spear.getOrCreateTag().getInt(SpearOfMorozItem.ACTION_TAG) == 1))
            return;
        callbackInfo.cancel();
    }
}
