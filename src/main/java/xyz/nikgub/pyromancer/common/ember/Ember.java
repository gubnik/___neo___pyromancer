package xyz.nikgub.pyromancer.common.ember;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.common.items.MaceItem;
import xyz.nikgub.pyromancer.common.registries.custom.EmberRegistry;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author nikgub_
 */
public class Ember {
    private final String name;
    private final EmberType type;
    private final List<Class<? extends Item>> acceptableItems;
    private final EmberAnimation animation;
    private final BiConsumer<Player, ItemStack> attack;
    public static List<Class<? extends Item>> GENERAL_WEAPONS = List.of(SwordItem.class, AxeItem.class, MaceItem.class);
    /**
     *
     * @param name              Name of ember
     * @param type              Type of infusion, defines colouring and damage boosts
     * @param acceptableItems   List of item classes that this ember can be applied to
     * @param animation         Record of third person animation, first person item transforms, use duration and cooldown
     */
    public Ember(String name, @NotNull EmberType type, List<Class<? extends Item>> acceptableItems, @NotNull EmberAnimation animation, BiConsumer<Player, ItemStack> attack)
    {
        this.name = name;
        this.type = type;
        this.acceptableItems = acceptableItems;
        this.animation = animation;
        this.attack = attack;
    }
    @Override
    public String toString()
    {
        return String.format("ember:%s/%s", this.name, this.type.getName());
    }
    public String getName() {
        return name;
    }
    public String getDescriptionId()
    {
        return "ember." + this.name + ".description";
    }
    public String getNameId()
    {
        return "ember." + this.name + ".name";
    }
    public EmberType getType() {
        return type;
    }
    public List<Class<? extends Item>> getAcceptableItems() {
        return acceptableItems;
    }
    @NotNull
    public EmberAnimation getAnimation() {
        return animation;
    }
    public ItemStack applyToItemStack(ItemStack itemStack)
    {
        itemStack.getOrCreateTag().putString(EmberRegistry.EMBER_TAG, this.getName());
        return itemStack;
    }
    public boolean isValidFor(Item weapon)
    {
        return this.acceptableItems.contains(weapon.getClass());
    }
    public static boolean emberItemStackPredicate(ItemStack itemStack)
    {
        return (emberItemPredicate(itemStack.getItem()) && EmberRegistry.getFromItem(itemStack) != null);
    }
    public static boolean emberItemPredicate(Item item)
    {
        for (String id : PyromancerConfig.emberBlacklist)
        {
            if(id.equals(item.toString()))
            {
                return false;
            }
        }
        for (String id : PyromancerConfig.emberAdditionalItems)
        {
            if(id.equals(item.toString()))
            {
                return true;
            }
        }
        return item instanceof TieredItem || EmberUtilities.isUniquelyAllowed(item) && !EmberUtilities.isUniquelyDenied(item);
    }

    public BiConsumer<Player, ItemStack> getAttack() {
        return attack;
    }
}
