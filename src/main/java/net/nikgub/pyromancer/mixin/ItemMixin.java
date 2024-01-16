package net.nikgub.pyromancer.mixin;

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
import net.nikgub.pyromancer.PyromancerConfig;
import net.nikgub.pyromancer.ember.Ember;
import net.nikgub.pyromancer.ember.EmberUtilities;
import net.nikgub.pyromancer.events.EmberEvent;
import net.nikgub.pyromancer.registries.custom.EmberRegistry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
/**
 * This mixin defines custom behaviour of {@link Item}
 * Most methods here are meant to provide {@link net.nikgub.pyromancer.ember.Ember} functionality
 **/
@Mixin(Item.class)
@SuppressWarnings("unused")
public abstract class ItemMixin implements FeatureElement, ItemLike, IForgeItem {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useMixinHead(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callbackInfoReturnable)
    {
        ItemStack itemStack = player.getItemInHand(hand);
        if(Ember.emberItemStackPredicate(itemStack))
        {
            player.startUsingItem(hand);
            callbackInfoReturnable.setReturnValue(InteractionResultHolder.success(itemStack));
        }
    }
    @Inject(method = "onUseTick", at = @At("HEAD"), cancellable = true)
    public void onUseTickMixinHead(Level level, LivingEntity entity, ItemStack itemStack, int tick, CallbackInfo callbackInfo)
    {
        Ember ember = EmberRegistry.getFromItem(itemStack);
        if(ember == null) return;
        if(Ember.emberItemStackPredicate(itemStack) && entity instanceof Player player)
        {
            EmberEvent event = EmberUtilities.getEmberEvent(player, ember, itemStack, tick);
            ember.getAttack().accept(player, itemStack);
        }
    }
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    public void finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> retVal) {
        if(Ember.emberItemStackPredicate(itemStack))
        {
            if(entity instanceof Player player) player.getCooldowns().addCooldown(this.asItem(), EmberRegistry.getFromItem(itemStack).getAnimation().cooldown());
        }
    }
    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    public void getUseAnimationMixinHead(ItemStack itemStack, CallbackInfoReturnable<UseAnim> retVal) {
        if(Ember.emberItemStackPredicate(itemStack))
        {
            retVal.setReturnValue(UseAnim.CUSTOM);
        }
    }
    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    public void getUseDurationMixinHead(ItemStack itemStack, CallbackInfoReturnable<Integer> retVal) {
        if(Ember.emberItemStackPredicate(itemStack))
        {
            retVal.setReturnValue(EmberRegistry.getFromItem(itemStack).getAnimation().useTime());
        }
    }
    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    public void appendHoverTextMixinHead(@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag, CallbackInfo callbackInfo)
    {
        if(Ember.emberItemStackPredicate(itemStack)) {
            Ember ember = EmberRegistry.getFromItem(itemStack);
            if (ember == null) return;
            if (PyromancerConfig.embersDescriptionKey.getSupplier().get()) {
                list.add(Component.translatable(ember.getNameId()));
                list.add(Component.translatable(ember.getDescriptionId()));
            } else {
                list.add(Component.translatable(
                        Component.translatable("pyromancer.ember_hidden_line").getString() + PyromancerConfig.embersDescriptionKey.toString()
                ).withStyle(ChatFormatting.GRAY));
            }
            //callbackInfo.cancel();
        }
    }
}
