package xyz.nikgub.pyromancer.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.common.ember.Ember;

import java.util.Arrays;
import java.util.List;
public class EmberItem extends Item {
    public EmberItem(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        Ember ember = Ember.getFromItem(itemStack);
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
        Ember ember = Ember.getFromItem(held);
        if(ember == null || clickAction != ClickAction.SECONDARY) return false;
        try {
            Arrays.stream(inSlot.getItem().getClass().getMethods()).filter(m -> Arrays.equals(m.getParameterTypes(), new Class<?>[]{Level.class, Player.class, InteractionHand.class}) &&
                    m.getReturnType() == InteractionResultHolder.class && m.getDeclaringClass() == Item.class).findFirst().orElseThrow(() -> new NoSuchMethodException("Use method not found"));
        }
        catch (NoSuchMethodException exception)
        {
            return false;
        }
        if(held.getItem() instanceof EmberItem
        && ember.isValidFor(inSlot.getItem())
        )
        {
            ember.applyToItemStack(inSlot);
            return true;
        }
        return false;
    }
}
