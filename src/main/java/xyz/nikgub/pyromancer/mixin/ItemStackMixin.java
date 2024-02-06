package xyz.nikgub.pyromancer.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nikgub.pyromancer.PyromancerMod;
import xyz.nikgub.pyromancer.ember.Ember;
import xyz.nikgub.pyromancer.items.EmberItem;
import xyz.nikgub.pyromancer.registries.custom.EmberRegistry;

import java.util.function.Function;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements net.minecraftforge.common.extensions.IForgeItemStack {
    @Shadow
    private static final Component DISABLED_ITEM_TOOLTIP = Component.translatable("item.disabled").withStyle(ChatFormatting.RED);
    @Shadow
    private static final Style LORE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true);
    @Shadow
    public abstract CompoundTag getTagElement(String string);
    @Shadow
    public abstract Item getItem();
    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    public void getHoverNameMixinHead(CallbackInfoReturnable<Component> retVal) {
        ItemStack self = (ItemStack) (Object) this;
        Function<Integer, Integer> colorFunction;
        Ember ember = EmberRegistry.getFromItem(self);
        if(Ember.emberItemStackPredicate(self) || self.getItem() instanceof EmberItem)
        {
            if(ember == null) return;
            colorFunction = ember.getType().getTextColorFunction();
        }
        else return;
        CompoundTag compoundtag = this.getTagElement("display");
        if (compoundtag != null && compoundtag.contains("Name", 8))
        {
            try {
                MutableComponent component = Component.Serializer.fromJson(compoundtag.getString("Name"));
                if (component != null) {
                    component = component.withStyle(component.getStyle().withColor(colorFunction.apply((PyromancerMod.clientTick))));
                    retVal.setReturnValue(component);
                }
                compoundtag.remove("Name");
            } catch (Exception exception) {
                compoundtag.remove("Name");
            }
        }
        MutableComponent defaultComponent = this.getItem().getName(self).copy();
        defaultComponent = defaultComponent.withStyle(defaultComponent.getStyle().withColor(colorFunction.apply(PyromancerMod.clientTick)));
        retVal.setReturnValue(defaultComponent);
    }
}
