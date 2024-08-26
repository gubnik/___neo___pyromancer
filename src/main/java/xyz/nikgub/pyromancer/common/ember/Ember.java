package xyz.nikgub.pyromancer.common.ember;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nikgub.pyromancer.PyromancerConfig;
import xyz.nikgub.pyromancer.common.event.EmberEvent;
import xyz.nikgub.pyromancer.common.item.BlazingJournalItem;
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

    private final int costOnFinish;

    private final int costOnTick;

    private final Set<Class<? extends Item>> acceptableItems;

    private final EmberAnimation animation;

    /**
     * @param name              Name of ember
     * @param type              Type of infusion, defines colouring and damage boosts
     * @param acceptableWeapons List of item classes that this ember can be applied to
     * @param animation         Record of third person animation, first person item transforms, use duration and cooldown
     */
    @SafeVarargs
    public Ember (String name, @NotNull EmberType type, int costOnFinish, int costOnTick, @NotNull EmberAnimation animation, Class<? extends Item>... acceptableWeapons)
    {
        this.name = name;
        this.type = type;
        this.costOnFinish = costOnFinish;
        this.costOnTick = costOnTick;
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
        return item instanceof TieredItem || isUniquelyAllowed(item);
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
        return String.format("ember:%s/%s", this.type.getName(), this.name);
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

    public int getCostOnFinish ()
    {
        return costOnFinish;
    }

    public int getCostOnTick ()
    {
        return costOnTick;
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

    public boolean checkBlazeOnTick (@NotNull LivingEntity entity)
    {
        ItemStack journal = BlazingJournalItem.guessJournal(entity);
        if (journal == ItemStack.EMPTY) return false;
        return BlazingJournalItem.getBlaze(entity) >= this.costOnTick;
    }

    public boolean consumeBlazeOnTick (@NotNull LivingEntity entity)
    {
        boolean retVal = checkBlazeOnTick(entity);
        if (retVal)
        {
            BlazingJournalItem.changeBlaze(entity, -this.costOnTick);
            return true;
        }
        return false;
    }

    public boolean checkBlazeOnFinish (@NotNull LivingEntity entity)
    {
        ItemStack journal = BlazingJournalItem.guessJournal(entity);
        if (journal == ItemStack.EMPTY) return false;
        return BlazingJournalItem.getBlaze(entity) >= this.costOnFinish;
    }

    public boolean consumeBlazeOnFinish (@NotNull LivingEntity entity)
    {
        boolean retVal = checkBlazeOnFinish(entity);
        if (retVal)
        {
            BlazingJournalItem.changeBlaze(entity, -this.costOnFinish);
            return true;
        }
        return false;
    }

    @NotNull
    public static EmberEvent getEmberEvent (Player player, Ember ember, ItemStack itemStack, int tick)
    {
        EmberEvent event = new EmberEvent(player, ember, itemStack, tick);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static boolean isUniquelyDenied (ItemStack itemStack)
    {
        return (itemStack.getItem().getClass().isAnnotationPresent(UniqueEmberBehaviour.class)) && itemStack.getItem().getClass().getAnnotation(UniqueEmberBehaviour.class).allow() == UniqueEmberBehaviour.AllowanceModifier.DENY;
    }

    public static boolean isUniquelyDenied (Item item)
    {
        return (item.getClass().isAnnotationPresent(UniqueEmberBehaviour.class)) && item.getClass().getAnnotation(UniqueEmberBehaviour.class).allow() == UniqueEmberBehaviour.AllowanceModifier.DENY;
    }

    public static boolean isUniquelyAllowed (ItemStack itemStack)
    {
        return (itemStack.getItem().getClass().isAnnotationPresent(UniqueEmberBehaviour.class)) && itemStack.getItem().getClass().getAnnotation(UniqueEmberBehaviour.class).allow() == UniqueEmberBehaviour.AllowanceModifier.ALLOW
                && !isUniquelyDenied(itemStack);
    }

    public static boolean isUniquelyAllowed (Item item)
    {
        return (item.getClass().isAnnotationPresent(UniqueEmberBehaviour.class)) && item.getClass().getAnnotation(UniqueEmberBehaviour.class).allow() == UniqueEmberBehaviour.AllowanceModifier.ALLOW
                && !isUniquelyDenied(item);
    }
}
