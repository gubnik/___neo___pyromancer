package xyz.nikgub.pyromancer.common.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.common.entities.projectiles.BombsackProjectile;

import java.util.function.Supplier;

public class BombsackItem extends Item {

    private final Supplier<EntityType<? extends BombsackProjectile>> typeSupplier;

    public BombsackItem(Properties properties, Supplier<EntityType<? extends BombsackProjectile>> typeSupplier) {
        super(properties);
        this.typeSupplier = typeSupplier;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            BombsackProjectile bombsack = typeSupplier.get().create(level);
            if (bombsack == null) return InteractionResultHolder.fail(itemstack);
            bombsack.setItem(itemstack);
            bombsack.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            bombsack.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 0.8f, 0.1f);
            level.addFreshEntity(bombsack);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
