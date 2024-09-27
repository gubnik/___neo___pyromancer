package xyz.nikgub.pyromancer.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikgub.pyromancer.registry.ItemRegistry;

import java.util.UUID;

@SuppressWarnings("unused")
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity
{
    @Shadow
    private UUID thrower;

    public ItemEntityMixin (EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Shadow
    public abstract ItemStack getItem ();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickMixin (CallbackInfo callbackInfo)
    {
        if (this.getItem().getItem() == ItemRegistry.EVENBURNING_HEART.get())
        {
            //this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.01D, 0.0D);
        }
    }
}
