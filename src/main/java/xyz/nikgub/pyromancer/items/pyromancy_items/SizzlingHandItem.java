package xyz.nikgub.pyromancer.items.pyromancy_items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.animations.ClientSizzlingHandExtension;
import xyz.nikgub.pyromancer.entities.projectiles.SizzlingHandFireball;
import xyz.nikgub.pyromancer.items.UsablePyromancyItem;
import xyz.nikgub.pyromancer.registries.vanila.AttributeRegistry;
import xyz.nikgub.pyromancer.util.ItemUtils;

import java.util.function.Consumer;

public class SizzlingHandItem extends UsablePyromancyItem {
    public SizzlingHandItem(Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        if(ItemUtils.getBlaze(player) > player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()))
        {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int tick)
    {
        SizzlingHandFireball fireball = new SizzlingHandFireball(EntityType.SMALL_FIREBALL, entity.level(), (float) entity.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()), 10);
        fireball.setOwner(entity);
        fireball.setPos(entity.getEyePosition());
        fireball.setDeltaMovement(entity.getLookAngle().multiply(2d, 2d, 2d));
        if(entity.level().addFreshEntity(fireball) && entity instanceof Player player)
        {
            ItemUtils.changeBlaze(player, -1 * (int) player.getAttributeValue(AttributeRegistry.BLAZE_CONSUMPTION.get()));
        }
    }
    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity, int tick)
    {
        if(!(entity instanceof Player player)) return;
        player.getCooldowns().addCooldown(itemStack.getItem(), 40 + this.getUseDuration(itemStack) - tick);
        SizzlingHandFireball fireball = new SizzlingHandFireball(EntityType.SMALL_FIREBALL, entity.level(), (float) entity.getAttributeValue(AttributeRegistry.PYROMANCY_DAMAGE.get()), 10);
        fireball.collisionEffect(level);
        entity.stopUsingItem();
    }
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity)
    {
        this.releaseUsing(itemStack, level, entity, this.getUseDuration(itemStack));
        return itemStack;
    }
    @Override
    public int getUseDuration(@NotNull ItemStack itemStack)
    {
        return 10;
    }
    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new ClientSizzlingHandExtension());
    }
    @Override
    public Pair<Integer, Float> getPyromancyModifiers() {
        return Pair.of(1, 2.5f);
    }
    @Override
    public void compendiumTransforms(PoseStack poseStack)
    {
    }
}
