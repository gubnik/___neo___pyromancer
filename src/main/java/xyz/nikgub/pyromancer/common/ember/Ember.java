package xyz.nikgub.pyromancer.common.ember;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.common.item.EmberItem;
import xyz.nikgub.pyromancer.registries.EmberRegistry;

import java.util.Set;

/**
 * @author nikgub_
 */
public class Ember
{
    private final String name;

    private final EmberType type;

    private final Set<Class<? extends Item>> acceptableItems;

    private final EmberAnimation animation;

    /**
     * @param name              Name of ember
     * @param type              Type of infusion, defines colouring and damage boosts
     * @param acceptableWeapons List of item classes that this ember can be applied to
     * @param animation         Record of third person animation, first person item transforms, use duration and cooldown
     */
    @SafeVarargs
    public Ember (String name, @NotNull EmberType type, @NotNull EmberAnimation animation, Class<? extends Item>... acceptableWeapons)
    {
        this.name = name;
        this.type = type;
        this.acceptableItems = Set.of(acceptableWeapons);
        this.animation = animation;
    }

    public static boolean emberItemPredicate (Item item)
    {
        for (String id : PyromancerConfig.emberBlacklist)
        {
            if (id.equals(item.toString()))
            {
                return false;
            }
        }
        for (String id : PyromancerConfig.emberAdditionalItems)
        {
            if (id.equals(item.toString()))
            {
                return true;
            }
        }
        return item instanceof TieredItem || EmberUtilities.isUniquelyAllowed(item);
    }

    @Nullable
    public static Ember getFromItem (ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof EmberItem)
            return EmberRegistry.getEmberByName(itemStack.getOrCreateTag().getString(EmberRegistry.EMBER_TAG));
        if (!emberItemPredicate(itemStack.getItem())) return null;
        return EmberRegistry.getEmberByName(itemStack.getOrCreateTag().getString(EmberRegistry.EMBER_TAG));
    }

    @Override
    public String toString ()
    {
        return String.format("ember:%s/%s", this.name, this.type.getName());
    }

    public String getName ()
    {
        return name;
    }

    public String getDescriptionId ()
    {
        return "ember." + this.name + ".description";
    }

    public String getNameId ()
    {
        return "ember." + this.name + ".name";
    }

    public EmberType getType ()
    {
        return type;
    }

    public Set<Class<? extends Item>> getAcceptableItems ()
    {
        return acceptableItems;
    }

    @NotNull
    public EmberAnimation getAnimation ()
    {
        return animation;
    }

    public ItemStack applyToItemStack (ItemStack itemStack)
    {
        itemStack.getOrCreateTag().putString(EmberRegistry.EMBER_TAG, this.getName());
        return itemStack;
    }

    public boolean isValidFor (Item weapon)
    {
        return emberItemPredicate(weapon) && this.acceptableItems.contains(weapon.getClass());
    }

    public void tickEvent (Level level, LivingEntity entity, ItemStack itemStack, int tick)
    {

    }

    public void finishEvent (ItemStack itemStack, Level level, LivingEntity entity)
    {

    }
}
