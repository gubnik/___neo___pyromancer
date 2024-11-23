/*
    Pyromancer, Minecraft Forge modification
    Copyright (C) 2024, Nikolay Gubankov (aka nikgub)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
import xyz.nikgub.pyromancer.registry.RarityRegistry;

import java.util.function.Consumer;

public class AccursedContractItem extends Item
{
    public AccursedContractItem ()
    {
        super(new Item.Properties().stacksTo(1).rarity(RarityRegistry.FROST_RARITY));
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
