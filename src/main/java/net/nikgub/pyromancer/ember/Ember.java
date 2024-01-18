package net.nikgub.pyromancer.ember;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.nikgub.pyromancer.PyromancerConfig;
import net.nikgub.pyromancer.items.MaceItem;
import net.nikgub.pyromancer.registries.custom.EmberRegistry;
import net.nikgub.pyromancer.util.GeneralUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author nikgub_
 */
public class Ember {
    private final String name;
    private final Type type;
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
    public Ember(String name, @NotNull Type type, List<Class<? extends Item>> acceptableItems, @NotNull EmberAnimation animation, BiConsumer<Player, ItemStack> attack)
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
        return String.format("ember:%s/%s", this.name, this.type.name);
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
    public Type getType() {
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
        itemStack.getOrCreateTag().putString(EmberRegistry.TAG_NAME, this.getName());
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

    public static final class Type {
        private final String name;
        private final int color;
        private final Function<Integer, Integer> tickMod;

        public static final Type FLAME = new Type("fire",GeneralUtils.rgbaToColorInteger(64, 56,6,224),
                    (tick)->GeneralUtils.rgbToColorInteger(100+(tick /10)*5,50+(tick /10)*4,100-(tick /20)));

        public static final Type SOULFLAME = new Type("soulflame",GeneralUtils.rgbaToColorInteger(6, 64,56,224),
                    (tick)->GeneralUtils.rgbToColorInteger(100-(tick /40),100+(tick /10)*5,100+(tick /10)*4));

        public static final Type HELLBLAZE = new Type("hellblaze",GeneralUtils.rgbaToColorInteger(64, 32,24,224),
                    (tick)->GeneralUtils.rgbToColorInteger(140+(tick /10)*2,60-(tick /10),80+(tick /10)));

        public Type(String name, int color, Function<Integer, Integer> tickMod) {
            this.name = name;
            this.color = color;
            this.tickMod = tickMod;
        }

        public String getName() {
                return name;
            }

        public int getColor() {
                return color;
            }

        public Function<Integer, Integer> getTextColorFunction() {
            return tickMod;
        }

        public String name() {
            return name;
        }

        public int color() {
            return color;
        }

        public Function<Integer, Integer> tickMod() {
            return tickMod;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Type) obj;
            return Objects.equals(this.name, that.name) &&
                    this.color == that.color &&
                    Objects.equals(this.tickMod, that.tickMod);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, color, tickMod);
        }

        @Override
        public String toString() {
            return "Type[" +
                    "name=" + name + ", " +
                    "color=" + color + ", " +
                    "tickMod=" + tickMod + ']';
        }
    }
}
