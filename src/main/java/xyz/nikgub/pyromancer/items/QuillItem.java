package xyz.nikgub.pyromancer.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import xyz.nikgub.pyromancer.items.capabilities.BlazingJournalCapability;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * General class for quills. <p>
 * Quills are special items that can be inserted into {@link BlazingJournalItem}, consult {@link BlazingJournalCapability} <p>
 * Effects of quills are called when Blazing Journal activates an additional attack as per its defined enchantments
 */
public class QuillItem extends Item {
    public static final String QUILL_RENDER_TAG = "PYROMANCER_QUILL_SMART_RENDER_TAG";
    private final TriConsumer<Player, ItemStack, ItemStack> attack;
    private final TriFunction<Player, ItemStack, ItemStack, Boolean> condition;

    /**
     * @param properties    Regular item properties, constructor always makes the stack size to 1
     * @param attack        TriConsumer that defines the attack that is launched per condition <p>
     *                      Consumes the following: <p>
     *                      player who is responsible for the attack, <p>
     *                      itemstack by which the attack was made, <p>
     *                      itemstack of {@link BlazingJournalItem} on which the quill is equipped
     * @param condition     bool TriFunction that is responsible for the condition of launching associated attack <p>
     *                      Accepts the following: <p>
     *                      player who is responsible for the attack, <p>
     *                      itemstack by which the attack was made, <p>
     *                      itemstack of {@link BlazingJournalItem} on which the quill is equipped
     */
    public QuillItem(Properties properties, TriConsumer<Player, ItemStack, ItemStack> attack, TriFunction<Player, ItemStack, ItemStack, Boolean> condition) {
        super(properties.stacksTo(1));
        this.attack = attack;
        this.condition = condition;
    }

    public TriConsumer<Player, ItemStack, ItemStack> getAttack() {
        return attack;
    }
    public TriFunction<Player, ItemStack, ItemStack, Boolean> getCondition() {
        return condition;
    }
    public Pair<Integer, Float> getPyromancyModifiers() {
        return Pair.of(0,0f);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @javax.annotation.Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag)
    {
    }
}
