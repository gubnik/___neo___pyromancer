package net.nikgub.pyromancer.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author nikgub_
 */
@NotNull
public record EmberAnimation(TriConsumer<HumanoidModel<?>, LivingEntity, HumanoidArm> thirdPersonAnimation,
                             Consumer<PoseStack> firstPersonAnimation,
                             int useTime,
                             int cooldown) {
    /**
     *
     * @param thirdPersonAnimation      consumer that defines animations for player (humanoid) model
     * @param firstPersonAnimation      consumer that defines transformations for item in first-person view
     * @param useTime                   max use time of item with ember, required to sync animations
     * @param cooldown                  cooldown after the animation plays
     */
    public EmberAnimation(@NotNull TriConsumer<HumanoidModel<?>, LivingEntity, HumanoidArm> thirdPersonAnimation,
                          @NotNull Consumer<PoseStack> firstPersonAnimation,
                          int useTime, int cooldown) {
        this.thirdPersonAnimation = thirdPersonAnimation;
        this.firstPersonAnimation = firstPersonAnimation;
        this.useTime = useTime;
        this.cooldown = cooldown;
    }
}
