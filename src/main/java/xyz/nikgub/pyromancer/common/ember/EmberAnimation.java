package xyz.nikgub.pyromancer.common.ember;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author nikgub_
 */
@NotNull
public abstract class EmberAnimation {

    private final int useTime;
    private final int cooldown;

    public EmberAnimation(int useTime, int cooldown) {
        this.useTime = useTime;
        this.cooldown = cooldown;
    }

    public abstract ThirdPersonAnimation thirdPersonAnimation();

    public abstract FirstPersonAnimation firstPersonAnimation();

    public int useTime() {
        return useTime;
    }

    public int cooldown() {
        return cooldown;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EmberAnimation) obj;
        return Objects.equals(this.thirdPersonAnimation(), that.thirdPersonAnimation()) &&
                Objects.equals(this.firstPersonAnimation(), that.firstPersonAnimation()) &&
                this.useTime == that.useTime &&
                this.cooldown == that.cooldown;
    }

    @Override
    public int hashCode() {
        return Objects.hash(thirdPersonAnimation(), firstPersonAnimation(), useTime, cooldown);
    }

    @Override
    public String toString() {
        return "EmberAnimation[" +
                "thirdPersonAnimation()=" + thirdPersonAnimation() + ", " +
                "firstPersonAnimation()=" + firstPersonAnimation() + ", " +
                "useTime=" + useTime + ", " +
                "cooldown=" + cooldown + ']';
    }


    public interface ThirdPersonAnimation {
        void run(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm);
    }

    public interface FirstPersonAnimation {
        void run(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess);
    }
}
