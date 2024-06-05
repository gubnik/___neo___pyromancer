package xyz.nikgub.pyromancer.common.ember;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.common.items.EmberItem;
import xyz.nikgub.pyromancer.common.items.MaceItem;
import xyz.nikgub.pyromancer.common.registries.EmberRegistry;

import java.util.List;

/**
 * @author nikgub_
 */
public class Ember {

    public static final List<Class<? extends Item>> GENERAL_WEAPONS = List.of(SwordItem.class, AxeItem.class, MaceItem.class);
    public static final List<Class<? extends Item>> SWORDS = List.of(SwordItem.class);
    public static final List<Class<? extends Item>> AXES = List.of(AxeItem.class);
    public static final List<Class<? extends Item>> MACES = List.of(MaceItem.class);

    private final String name;
    private final EmberType type;
    private final List<Class<? extends Item>> acceptableItems;
    private final EmberAnimation animation;

    /**
     *
     * @param name              Name of ember
     * @param type              Type of infusion, defines colouring and damage boosts
     * @param acceptableItems   List of item classes that this ember can be applied to
     * @param animation         Record of third person animation, first person item transforms, use duration and cooldown
     */
    public Ember(String name, @NotNull EmberType type, List<Class<? extends Item>> acceptableItems, @NotNull EmberAnimation animation)
    {
        this.name = name;
        this.type = type;
        this.acceptableItems = acceptableItems;
        this.animation = animation;
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
        return emberItemPredicate(weapon) && this.acceptableItems.contains(weapon.getClass());
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

    @Nullable
    public static Ember getFromItem(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof EmberItem)
            return EmberRegistry.getEmberByName(itemStack.getOrCreateTag().getString(EmberRegistry.EMBER_TAG));
        if (!emberItemPredicate(itemStack.getItem())) return null;
        return EmberRegistry.getEmberByName(itemStack.getOrCreateTag().getString(EmberRegistry.EMBER_TAG));
    }


    public void tickEvent (Level level, LivingEntity entity, ItemStack itemStack, int tick)
    {

    }

    public void finishEvent (ItemStack itemStack, Level level, LivingEntity entity)
    {

    }
}
