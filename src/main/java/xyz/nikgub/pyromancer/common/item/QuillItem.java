package xyz.nikgub.pyromancer.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.incandescent.common.item.IExtensibleTooltipItem;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.common.item_capability.BlazingJournalCapability;

import java.util.List;

/**
 * General class for quills. <p>
 * Quills are special items that can be inserted into {@link BlazingJournalItem}, consult {@link BlazingJournalCapability} <p>
 * Effects of quills are called when Blazing Journal activates an additional attack as per its defined enchantments
 */
public abstract class QuillItem extends Item implements IExtensibleTooltipItem
{
    public static final String QUILL_RENDER_TAG = "PYROMANCER_QUILL_SMART_RENDER_TAG";

    public QuillItem(Properties properties)
	{
        super(properties.stacksTo(1));
    }

    public abstract void    getAttack    (Player player, ItemStack weaponStack, ItemStack journalStack);
    public abstract boolean getCondition (Player player, ItemStack weaponStack, ItemStack journalStack);

    public abstract float getDefaultPyromancyDamageBonus ();
    public abstract int getDefaultBlazeCostBonus ();

    @Override
    public final void appendHoverText(@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
        gatherTooltipLines(list, "pyromancer.pyromancy_hidden_line", "desc", PyromancerConfig.quillDescriptionKey);
    }
}
