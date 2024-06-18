package xyz.nikgub.pyromancer.common.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.client.item_extensions.CourtOfEmbersClientExtension;
import xyz.nikgub.pyromancer.common.entities.attack_effects.PyronadoEntity;
import xyz.nikgub.pyromancer.common.registries.AttributeRegistry;
import xyz.nikgub.pyromancer.common.registries.EntityTypeRegistry;
import xyz.nikgub.pyromancer.common.util.ItemUtils;

import java.util.function.Consumer;

public class CourtOfEmbersItem extends UsablePyromancyItem {

    public CourtOfEmbersItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getDefaultPyromancyDamage() {
        return 3;
    }

    @Override
    public int getDefaultBlazeCost() {
        return 16;
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new CourtOfEmbersClientExtension());
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity)
    {
        int cost;
        if(!(entity instanceof Player player)
                || ItemUtils.getBlaze(player) - (cost = (int) player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get())) < 0
        ) return itemStack;
        PyronadoEntity pyronado = new PyronadoEntity(EntityTypeRegistry.PYRONADO.get(), level);
        pyronado.setPlayerUuid(player.getUUID());
        pyronado.setSize(1);
        pyronado.setPos(player.position().add(new Vec3(0, pyronado.getBbHeight()/2, 0)));
        level.addFreshEntity(pyronado);
        ItemUtils.changeBlaze(player, -cost);
        this.releaseUsing(itemStack, level, entity, getUseDuration(itemStack));
        return itemStack;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack)
    {
        return 40;
    }

    @Override
    public void compendiumTransforms(PoseStack poseStack, ItemDisplayContext displayContext)
    {
        //poseStack.scale(1.25f, 1.25f, 1.25f);
        if(displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) poseStack.scale(1.33f, 1.33f, 1.33f);
        if(displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) poseStack.rotateAround(Axis.ZP.rotationDegrees(-90), 1f, 0.4f, 0);
        else poseStack.rotateAround(Axis.ZP.rotationDegrees(45), 1f, 0.4f, 0);
        poseStack.translate(0, 0, -1.25F);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        if(!(level instanceof ServerLevel serverLevel)) return;
        final float c = (float)(tick)/(float)this.getUseDuration(itemStack);
        final double R = 2.5 * c;
        final double X = entity.getX();
        final double Y = entity.getY();
        final double Z = entity.getZ();
        final double sinK = R * Math.sin(Math.toRadians(tick * 18));
        final double cosK = R * Math.cos(Math.toRadians(tick * 18));
        serverLevel.sendParticles(ParticleTypes.FLAME, X + sinK, Y + tick * 0.1, Z + cosK, (int)(1 + 5 * c), 0.1, 0.1, 0.1, 0);
        serverLevel.sendParticles(ParticleTypes.FLAME, X - sinK, Y + tick * 0.1, Z - cosK, (int)(1 + 5 * c), 0.1, 0.1, 0.1, 0);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity, int tick)
    {
        if(!(entity instanceof Player player)) return;
        player.getCooldowns().addCooldown(itemStack.getItem(), 40 + this.getUseDuration(itemStack) - tick);
    }
}
