package xyz.nikgub.pyromancer.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.client.item_extension.AccursedContractClientExtension;
import xyz.nikgub.pyromancer.common.contract.ContractDirector;

import java.util.function.Consumer;

public class AccursedContractItem extends Item
{
    public AccursedContractItem (Properties properties)
    {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem (@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity)
    {
        if (livingEntity instanceof Player player)
        {
            player.getCooldowns().addCooldown(this, 200);
            if (!(player.level() instanceof ServerLevel level1)) return itemStack;
            ContractDirector director = new ContractDirector(level1);
            director.run(player);
        }
        itemStack.shrink(1);
        return itemStack;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use (@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        player.startUsingItem(hand);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public @NotNull UseAnim getUseAnimation (@NotNull ItemStack itemStack)
    {
        return UseAnim.CUSTOM;
    }

    @Override
    public int getUseDuration (@NotNull ItemStack itemStack)
    {
        return 40;
    }

    @Override
    public void initializeClient (@NotNull Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new AccursedContractClientExtension());
    }
}
