package net.nikgub.pyromancer.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.nikgub.pyromancer.PyromancerConfig;
import net.nikgub.pyromancer.ember.Ember;
import net.nikgub.pyromancer.ember.EmberUtilities;
import net.nikgub.pyromancer.registries.custom.EmberRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
public class EmberItem extends Item {
    public EmberItem(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        Ember ember = EmberRegistry.getFromItem(itemStack);
        if(ember == null) return;
        if(PyromancerConfig.embersDescriptionKey.getSupplier().get())
        {
            list.add(Component.translatable(ember.getNameId()));
            list.add(Component.translatable(ember.getDescriptionId()));
        }
        else
        {
            list.add(Component.translatable(
                    Component.translatable("pyromancer.ember_hidden_line").getString() + PyromancerConfig.embersDescriptionKey.toString()
            ).withStyle(ChatFormatting.GRAY));
        }
    }
    @Override
    public boolean overrideStackedOnOther(@NotNull ItemStack held, Slot slot, @NotNull ClickAction clickAction, @NotNull Player player) {
        ItemStack inSlot = slot.getItem();
        Ember ember = EmberRegistry.getFromItem(held);
        if(ember == null || clickAction != ClickAction.SECONDARY) return false;
        if(held.getItem() instanceof EmberItem
        && ember.isValidFor(inSlot.getItem())
        && !EmberUtilities.isUniquelyDenied(inSlot)
        || EmberUtilities.isUniquelyAllowed(inSlot))
        {
            ember.applyToItemStack(inSlot);
            return true;
        }
        return false;
    }
}
