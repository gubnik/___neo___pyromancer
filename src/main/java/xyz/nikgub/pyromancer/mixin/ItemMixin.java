package xyz.nikgub.pyromancer.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.common.ember.Ember;
import xyz.nikgub.pyromancer.common.ember.EmberUtilities;
import xyz.nikgub.pyromancer.common.event.EmberEvent;

import java.util.List;

/**
 * This mixin defines custom behaviour of {@link Item}
 * Most methods here are meant to provide {@link Ember} functionality
 **/
@Mixin(Item.class)
@SuppressWarnings("unused")
public abstract class ItemMixin implements FeatureElement, ItemLike, IForgeItem
{
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useMixinHead (Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callbackInfoReturnable)
    {
        ItemStack itemStack = player.getItemInHand(hand);
        Ember ember = Ember.getFromItem(itemStack);
        if (ember == null || !ember.isValidFor(itemStack.getItem())) return;
        player.startUsingItem(hand);
        callbackInfoReturnable.setReturnValue(InteractionResultHolder.success(itemStack));
    }

    @Inject(method = "onUseTick", at = @At("HEAD"))
    public void onUseTickMixinHead (Level level, LivingEntity entity, ItemStack itemStack, int tick, CallbackInfo callbackInfo)
    {
        Ember ember = Ember.getFromItem(itemStack);
        if (ember == null || !ember.isValidFor(itemStack.getItem())) return;
        if (!(entity instanceof Player player)) return;
        EmberEvent event = EmberUtilities.getEmberEvent(player, ember, itemStack, tick);
        ember.tickEvent(level, entity, itemStack, tick);
    }

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void finishUsingItem (ItemStack itemStack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> retVal)
    {
        Ember ember = Ember.getFromItem(itemStack);
        if (ember == null || !ember.isValidFor(itemStack.getItem())) return;
        ember.finishEvent(itemStack, level, entity);
        if (entity instanceof Player player)
            player.getCooldowns().addCooldown(this.asItem(), Ember.getFromItem(itemStack).getAnimation().getCooldown());
    }

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    public void getUseAnimationMixinHead (ItemStack itemStack, CallbackInfoReturnable<UseAnim> retVal)
    {
        Ember ember = Ember.getFromItem(itemStack);
        if (ember == null || !ember.isValidFor(itemStack.getItem())) return;
        retVal.setReturnValue(UseAnim.CUSTOM);
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    public void getUseDurationMixinHead (ItemStack itemStack, CallbackInfoReturnable<Integer> retVal)
    {
        Ember ember = Ember.getFromItem(itemStack);
        if (ember == null || !ember.isValidFor(itemStack.getItem())) return;
        retVal.setReturnValue(Ember.getFromItem(itemStack).getAnimation().getUseTime());
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void appendHoverTextMixinHead (@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag, CallbackInfo callbackInfo)
    {
        Ember ember = Ember.getFromItem(itemStack);
        if (ember == null) return;
        if (PyromancerConfig.embersDescriptionKey.getSupplier().get())
        {
            list.add(Component.translatable(ember.getNameId()));
            list.add(Component.translatable(ember.getDescriptionId()));
        } else
        {
            list.add(Component.translatable(
                    Component.translatable("pyromancer.ember_hidden_line").getString() + PyromancerConfig.embersDescriptionKey.toString()
            ).withStyle(ChatFormatting.GRAY));
        }
        //callbackInfo.cancel();
    }
}
