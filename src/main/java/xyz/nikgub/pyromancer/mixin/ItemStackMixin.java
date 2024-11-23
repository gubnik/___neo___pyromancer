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
import xyz.nikgub.pyromancer.common.ember.Ember;
import xyz.nikgub.pyromancer.common.item.EmberItem;

import java.util.function.Function;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements net.minecraftforge.common.extensions.IForgeItemStack
{
    @Shadow
    private static final Component DISABLED_ITEM_TOOLTIP = Component.translatable("item.disabled").withStyle(ChatFormatting.RED);

    @Shadow
    private static final Style LORE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true);

    @Shadow
    public abstract CompoundTag getTagElement (String string);

    @Shadow
    public abstract Item getItem ();

    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    public void getHoverNameMixinHead (CallbackInfoReturnable<Component> retVal)
    {
        ItemStack self = (ItemStack) (Object) this;
        Function<Integer, Integer> colorFunction;
        Ember ember = Ember.getFromItem(self);
        if (ember != null || self.getItem() instanceof EmberItem)
        {
            if (ember == null) return;
            colorFunction = ember.getType().getTextColorFunction();
        } else return;
        CompoundTag compoundtag = this.getTagElement("display");
        if (compoundtag != null && compoundtag.contains("Name", 8))
        {
            try
            {
                MutableComponent component = Component.Serializer.fromJson(compoundtag.getString("Name"));
                if (component != null)
                {
                    component = component.withStyle(component.getStyle().withColor(colorFunction.apply((PyromancerMod.clientTick))));
                    retVal.setReturnValue(component);
                }
                compoundtag.remove("Name");
            } catch (Exception exception)
            {
                compoundtag.remove("Name");
            }
        }
        MutableComponent defaultComponent = this.getItem().getName(self).copy();
        defaultComponent = defaultComponent.withStyle(defaultComponent.getStyle().withColor(colorFunction.apply(PyromancerMod.clientTick)));
        retVal.setReturnValue(defaultComponent);
    }
}
